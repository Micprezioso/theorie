package ui;

import graph.*;
import io.GraphLoader;
import theme2.*;

import java.io.IOException;
import java.util.*;

/**
 * Menu console interactif pour le Thème 2 - Optimiser les ramassages des points de collecte.
 */
public class Theme2Menu {
    private Scanner scanner;
    private UndirectedGraph currentGraph;

    public Theme2Menu(Scanner scanner) {
        this.scanner = scanner;
        this.currentGraph = null;
    }

    /**
     * Affiche le menu et gère les interactions utilisateur.
     */
    public void run() {
        boolean running = true;
        
        while (running) {
            printMenu();
            int choice = readInt("Votre choix : ");
            
            switch (choice) {
                case 1:
                    approachNearestNeighbor();
                    break;
                case 2:
                    approachMst();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
            
            if (running && choice != 0) {
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
    }

    private void printMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  THÈME 2 : OPTIMISER LES RAMASSAGES DES POINTS DE COLLECTE");
        System.out.println("=".repeat(60));
        System.out.println("1) Approche 1 : Plus proche voisin (PPV)");
        System.out.println("2) Approche 2 : MST (arbre couvrant de poids minimum)");
        System.out.println("0) Retour au menu principal");
        System.out.println("=".repeat(60));
    }

    private void approachNearestNeighbor() {
        System.out.println("\n--- Approche 1 : Plus proche voisin ---");
        System.out.println("Charge le graphe et construit une tournée TSP avec l'algorithme");
        System.out.println("du plus proche voisin.\n");
        
        // Charger le graphe
        try {
            System.out.println("Chargement du graphe theme2_ppv_graph.txt...");
            currentGraph = GraphLoader.loadUndirectedGraph("theme2_ppv_graph.txt");
            System.out.println("Graphe chargé avec succès !");
            displayGraphSummary(currentGraph);
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de format : " + e.getMessage());
            return;
        }

        // Sélectionner le dépôt
        Vertex depot = selectVertex("Sélectionnez le sommet DÉPÔT", currentGraph);

        System.out.println("\nApplication de l'algorithme du plus proche voisin...");
        NearestNeighborSolver.Result result = NearestNeighborSolver.solve(currentGraph, depot);

        System.out.println("\n--- RÉSULTATS ---");
        System.out.println("Dépôt : " + depot.getId());
        System.out.println("Tournée plus proche voisin :");
        printPath(result.getTour());
        System.out.println("Distance TOTALE : " + result.getTotalDistance());
        System.out.println("\nNote : Cette approche ne garantit pas la solution optimale,");
        System.out.println("mais elle est rapide et donne généralement de bons résultats.");
    }

    private void approachMst() {
        System.out.println("\n--- Approche 2 : MST (arbre couvrant de poids minimum) ---");
        System.out.println("Construit un MST, le parcourt pour obtenir une tournée approchée,");
        System.out.println("et peut découper en sous-tournées selon la capacité.\n");
        
        // Constantes
        final double MAX_CAPACITY = 20.0;
        
        // Charger le graphe complet
        try {
            System.out.println("Chargement du graphe theme2_mst_graph_complete.txt...");
            currentGraph = GraphLoader.loadUndirectedGraph("theme2_mst_graph_complete.txt");
            System.out.println("Graphe chargé avec succès !");
            System.out.println("Dans cette méthode, nous prenons en compte la charge maximale du camion et la contenance de chaque point de collecte.");
            displayGraphSummaryForMst(currentGraph);
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de format : " + e.getMessage());
            return;
        }

        // Dépôt fixé à H
        Vertex depot = currentGraph.getVertex("H");
        if (depot == null) {
            System.out.println("Erreur : Le sommet H (dépôt) n'existe pas dans le graphe.");
            return;
        }
        System.out.println("\nDans cette approche, le dépôt est fixé au point H.");

        // Définir les contenances des points de collecte
        Map<Vertex, Double> capacities = new HashMap<>();
        capacities.put(currentGraph.getVertex("A"), 4.0);
        capacities.put(currentGraph.getVertex("B"), 6.0);
        capacities.put(currentGraph.getVertex("C"), 5.0);
        capacities.put(currentGraph.getVertex("D"), 3.0);
        capacities.put(currentGraph.getVertex("E"), 4.0);
        capacities.put(currentGraph.getVertex("F"), 2.0);
        capacities.put(currentGraph.getVertex("G"), 5.0);
        capacities.put(depot, 0.0); // H est le dépôt, pas de collecte

        System.out.println("\nConstruction de l'arbre couvrant de poids minimum (MST)...");
        MstTspSolver.Result result = MstTspSolver.solve(currentGraph, depot);

        // Optimiser le découpage de la tournée globale en sous-tournées pour minimiser la distance totale
        List<List<Vertex>> subTours = MstTspSolver.optimizeSplitByCapacity(currentGraph, result.getTour(), capacities, MAX_CAPACITY);

        // Afficher les résultats
        System.out.println("\n--- RÉSULTATS ---");
        System.out.println("Résultat (Approche MST / tournée capacitaire) :");
        System.out.println("Le camion effectue " + subTours.size() + " tournée(s) pour respecter la charge maximale et visiter tous les points de collecte.");
        System.out.println();

        double totalDistance = 0.0;
        double totalLoad = 0.0;

        for (int i = 0; i < subTours.size(); i++) {
            List<Vertex> subTour = subTours.get(i);
            
            // Calculer la charge de cette tournée
            double tourLoad = 0.0;
            for (int j = 1; j < subTour.size() - 1; j++) {
                tourLoad += capacities.getOrDefault(subTour.get(j), 0.0);
            }
            totalLoad += tourLoad;
            
            // Calculer la distance de cette tournée
            double tourDistance = 0.0;
            for (int j = 0; j < subTour.size() - 1; j++) {
                tourDistance += currentGraph.getWeight(subTour.get(j), subTour.get(j + 1));
            }
            totalDistance += tourDistance;
            
            // Afficher la tournée
            System.out.print("Tournée " + (i + 1) + " : ");
            for (int j = 0; j < subTour.size(); j++) {
                if (j > 0) {
                    System.out.print(" -> ");
                }
                System.out.print(subTour.get(j).getId());
            }
            System.out.println();
            System.out.println("Charge de la tournée " + (i + 1) + " : " + (int)tourLoad);
            System.out.println("Distance de la tournée " + (i + 1) + " : " + String.format("%.1f", tourDistance));
            System.out.println();
        }

        System.out.println("Distance totale de toutes les tournées : " + String.format("%.1f", totalDistance));
        System.out.println();
        System.out.println("La tournée globale a été construite à partir de l'arbre couvrant minimal (MST), puis découpée en tournées respectant la capacité du camion.");
    }

    private void splitByCapacity(List<Vertex> tour, Vertex depot) {
        System.out.println("\nDéfinition des quantités de déchets à collecter :");
        
        // Créer un dictionnaire de quantités (exemple simple)
        Map<Vertex, Double> quantities = new HashMap<>();
        for (Vertex v : currentGraph.getVertices()) {
            if (!v.equals(depot)) {
                System.out.print("Quantité pour " + v + " (défaut: 1.0) : ");
                String input = scanner.nextLine().trim();
                double qty;
                if (input.isEmpty()) {
                    qty = 1.0;
                } else {
                    try {
                        qty = Double.parseDouble(input);
                    } catch (NumberFormatException e) {
                        qty = 1.0;
                        System.out.println("Valeur invalide, utilisation de 1.0");
                    }
                }
                quantities.put(v, qty);
            }
        }
        
        System.out.print("\nCapacité maximale du camion : ");
        double maxCapacity = readDouble("");
        
        List<List<Vertex>> subTours = MstTspSolver.splitTourByCapacity(tour, quantities, maxCapacity);
        
        System.out.println("\n--- DÉCOUPAGE EN SOUS-TOURNÉES ---");
        System.out.println("Nombre de sous-tournées : " + subTours.size());
        
        for (int i = 0; i < subTours.size(); i++) {
            List<Vertex> subTour = subTours.get(i);
            double load = 0.0;
            for (int j = 1; j < subTour.size() - 1; j++) {
                load += quantities.getOrDefault(subTour.get(j), 0.0);
            }
            
            System.out.println("\nSous-tournée " + (i + 1) + " (charge : " + load + " / " + maxCapacity + ") :");
            printPath(subTour);
            
            // Calculer la distance de cette sous-tournée
            double distance = 0.0;
            for (int j = 0; j < subTour.size() - 1; j++) {
                distance += currentGraph.getWeight(subTour.get(j), subTour.get(j + 1));
            }
            System.out.println("Distance : " + distance);
        }
    }

    private void displayGraphSummary(Graph graph) {
        System.out.println("\n--- Résumé du graphe ---");
        System.out.println("Nombre de sommets : " + graph.getVertexCount());
        System.out.println("Nombre d'arêtes : " + graph.getEdgeCount());
    }

    private void displayGraphSummaryForMst(Graph graph) {
        System.out.println("\n--- Résumé du graphe ---");
        System.out.println("Nombre de sommets : " + graph.getVertexCount());
        System.out.println("Nombre d'arêtes : " + graph.getEdgeCount());
        System.out.println("Charge maximale du camion : 20");
        System.out.println("Contenances des points de collecte : A=4, B=6, C=5, D=3, E=4, F=2, G=5");
    }

    private Vertex selectVertex(String prompt, Graph graph) {
        System.out.println("\n" + prompt + " :");
        List<Vertex> vertices = new ArrayList<>(graph.getVertices());
        for (int i = 0; i < vertices.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + vertices.get(i));
        }
        
        int choice;
        do {
            choice = readInt("Numéro du sommet : ");
            if (choice < 1 || choice > vertices.size()) {
                System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choice < 1 || choice > vertices.size());
        
        return vertices.get(choice - 1);
    }

    private void printPath(List<Vertex> path) {
        if (path.isEmpty()) {
            System.out.println("  (chemin vide)");
            return;
        }
        
        System.out.print("  ");
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) {
                System.out.print(" → ");
            }
            System.out.print(path.get(i).getId());
        }
        System.out.println();
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre entier : ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne
        return value;
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Veuillez entrer un nombre : ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consommer le retour à la ligne
        return value;
    }
}


