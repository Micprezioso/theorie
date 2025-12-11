package ui;

import algorithms.*;
import graph.*;
import io.GraphLoader;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Menu console interactif pour le Thème 1 - Collecte des déchets.
 */
public class Theme1Menu {
    private Scanner scanner;
    private UndirectedGraph currentUndirectedGraph;
    private DirectedGraph currentDirectedGraph;
    private boolean isUndirected;

    public Theme1Menu(Scanner scanner) {
        this.scanner = scanner;
        this.currentUndirectedGraph = null;
        this.currentDirectedGraph = null;
        this.isUndirected = true;
    }

    /**
     * Affiche le menu principal et gère les interactions utilisateur.
     */
    public void run() {
        boolean running = true;
        
        while (running) {
            printMenu();
            int choice = readInt("Votre choix : ");
            
            switch (choice) {
                case 1:
                    loadGraph();
                    break;
                case 2:
                    problem1Hypothesis1();
                    break;
                case 3:
                    problem1Hypothesis2();
                    break;
                case 4:
                    problem2IdealCase();
                    break;
                case 5:
                    problem2TwoOddVertices();
                    break;
                case 6:
                    problem2GeneralCase();
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
        System.out.println("  THÈME 1 - RAMASSAGE DES ENCOMBRANTS ET TOURNÉES DE RUES");
        System.out.println("=".repeat(60));
        System.out.println("1) Charger un graphe depuis un fichier");
        System.out.println("2) T1 - P1 - H1 : Plus court chemin (graphe non orienté)");
        System.out.println("3) T1 - P1 - H2 : Plus court chemin (graphe orienté)");
        System.out.println("4) T1 - P2 - Cas idéal : Tous les sommets pairs");
        System.out.println("5) T1 - P2 - Cas 2 sommets impairs");
        System.out.println("6) T1 - P2 - Cas général : Postier chinois");
        System.out.println("0) Retour au menu principal");
        System.out.println("=".repeat(60));
    }

    private void loadGraph() {
        System.out.println("\n--- Chargement d'un graphe ---");
        System.out.print("Chemin du fichier : ");
        String filePath = scanner.nextLine().trim();
        
        System.out.print("Type de graphe (1=non orienté, 2=orienté) : ");
        int type = readInt("");
        
        try {
            if (type == 1) {
                currentUndirectedGraph = GraphLoader.loadUndirectedGraph(filePath);
                isUndirected = true;
                displayGraphSummary(currentUndirectedGraph);
            } else if (type == 2) {
                currentDirectedGraph = GraphLoader.loadDirectedGraph(filePath);
                isUndirected = false;
                displayGraphSummary(currentDirectedGraph);
            } else {
                System.out.println("Type invalide. Utilisation du type non orienté par défaut.");
                currentUndirectedGraph = GraphLoader.loadUndirectedGraph(filePath);
                isUndirected = true;
                displayGraphSummary(currentUndirectedGraph);
            }
            System.out.println("Graphe chargé avec succès !");
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de format : " + e.getMessage());
        }
    }

    private void displayGraphSummary(Graph graph) {
        System.out.println("\n--- Résumé du graphe ---");
        System.out.println("Nombre de sommets : " + graph.getVertexCount());
        System.out.println("Nombre d'arêtes/arcs : " + graph.getEdgeCount());
        System.out.println("\nDegrés des sommets :");
        for (Vertex v : graph.getVertices()) {
            System.out.println("  " + v + " : degré " + graph.getDegree(v));
        }
    }

    private void problem1Hypothesis1() {
        if (currentUndirectedGraph == null) {
            System.out.println("Erreur : Aucun graphe non orienté chargé.");
            return;
        }

        System.out.println("\n--- T1 P1 H1 : Plus court chemin (non orienté) ---");
        System.out.println("Détermine l'itinéraire le plus court pour aller du dépôt");
        System.out.println("chez un particulier puis revenir au dépôt.\n");

        Vertex depot = selectVertex("Sélectionnez le sommet DÉPÔT", currentUndirectedGraph);
        Vertex particulier = selectVertex("Sélectionnez le sommet PARTICULIER", currentUndirectedGraph);

        if (depot.equals(particulier)) {
            System.out.println("Le dépôt et le particulier sont identiques. Distance : 0");
            return;
        }

        // Calculer le chemin aller : dépôt → particulier
        Dijkstra.Result resultAller = Dijkstra.shortestPaths(currentUndirectedGraph, depot);
        List<Vertex> pathAller = resultAller.getPath(particulier);
        double distanceAller = resultAller.getDistance(particulier);

        // Calculer le chemin retour : particulier → dépôt
        // Pour un graphe non orienté, c'est le même chemin à l'envers
        Dijkstra.Result resultRetour = Dijkstra.shortestPaths(currentUndirectedGraph, particulier);
        List<Vertex> pathRetour = resultRetour.getPath(depot);
        double distanceRetour = resultRetour.getDistance(depot);

        double distanceTotale = distanceAller + distanceRetour;

        System.out.println("\n--- RÉSULTATS ---");
        System.out.println("Chemin ALLER (Dépôt → Particulier) :");
        printPath(pathAller);
        System.out.println("Distance aller : " + distanceAller);

        System.out.println("\nChemin RETOUR (Particulier → Dépôt) :");
        printPath(pathRetour);
        System.out.println("Distance retour : " + distanceRetour);

        System.out.println("\nDistance TOTALE : " + distanceTotale);
    }

    private void problem1Hypothesis2() {
        if (currentDirectedGraph == null) {
            System.out.println("Erreur : Aucun graphe orienté chargé.");
            return;
        }

        System.out.println("\n--- T1 P1 H2 : Plus court chemin (orienté) ---");
        System.out.println("Détermine l'itinéraire le plus court avec prise en compte");
        System.out.println("des sens uniques (graphe orienté).\n");

        Vertex depot = selectVertex("Sélectionnez le sommet DÉPÔT", currentDirectedGraph);
        Vertex particulier = selectVertex("Sélectionnez le sommet PARTICULIER", currentDirectedGraph);

        if (depot.equals(particulier)) {
            System.out.println("Le dépôt et le particulier sont identiques. Distance : 0");
            return;
        }

        // Calculer le chemin aller : dépôt → particulier
        Dijkstra.Result resultAller = Dijkstra.shortestPaths(currentDirectedGraph, depot);
        List<Vertex> pathAller = resultAller.getPath(particulier);
        double distanceAller = resultAller.getDistance(particulier);

        // Calculer le chemin retour : particulier → dépôt
        // Pour un graphe orienté, le chemin retour peut être différent !
        Dijkstra.Result resultRetour = Dijkstra.shortestPaths(currentDirectedGraph, particulier);
        List<Vertex> pathRetour = resultRetour.getPath(depot);
        double distanceRetour = resultRetour.getDistance(depot);

        double distanceTotale = distanceAller + distanceRetour;

        System.out.println("\n--- RÉSULTATS ---");
        System.out.println("Chemin ALLER (Dépôt → Particulier) :");
        if (distanceAller == Double.POSITIVE_INFINITY) {
            System.out.println("  IMPOSSIBLE (aucun chemin trouvé)");
        } else {
            printPath(pathAller);
            System.out.println("Distance aller : " + distanceAller);
        }

        System.out.println("\nChemin RETOUR (Particulier → Dépôt) :");
        if (distanceRetour == Double.POSITIVE_INFINITY) {
            System.out.println("  IMPOSSIBLE (aucun chemin trouvé)");
        } else {
            printPath(pathRetour);
            System.out.println("Distance retour : " + distanceRetour);
        }

        if (distanceAller != Double.POSITIVE_INFINITY && distanceRetour != Double.POSITIVE_INFINITY) {
            System.out.println("\nDistance TOTALE : " + distanceTotale);
        } else {
            System.out.println("\nAttention : Le trajet complet n'est pas possible avec les sens uniques actuels.");
        }
    }

    private void problem2IdealCase() {
        if (currentUndirectedGraph == null) {
            System.out.println("Erreur : Aucun graphe non orienté chargé.");
            return;
        }

        System.out.println("\n--- T1 P2 - Cas idéal : Tous les sommets pairs ---");
        System.out.println("Tournée eulérienne : passe dans toutes les rues exactement une fois.\n");

        // Vérifier si tous les sommets sont pairs
        List<Vertex> oddVertices = EulerianTour.getOddDegreeVertices(currentUndirectedGraph);
        if (!oddVertices.isEmpty()) {
            System.out.println("ATTENTION : Ce graphe n'est PAS eulérien.");
            System.out.println("Sommets de degré impair trouvés : " + oddVertices.size());
            for (Vertex v : oddVertices) {
                System.out.println("  - " + v + " (degré " + currentUndirectedGraph.getDegree(v) + ")");
            }
            System.out.println("\nUtilisez l'option 6 (Postier chinois) pour ce graphe.");
            return;
        }

        Vertex depot = selectVertex("Sélectionnez le sommet DÉPÔT", currentUndirectedGraph);

        System.out.println("\nTous les sommets ont un degré pair. Construction de la tournée eulérienne...");
        List<Vertex> tour = EulerianTour.findEulerianTour(currentUndirectedGraph, depot);
        double distance = EulerianTour.calculateTourDistance(currentUndirectedGraph, tour);

        System.out.println("\n--- RÉSULTATS ---");
        System.out.println("Tournée eulérienne (chaque arête parcourue exactement une fois) :");
        printPath(tour);
        System.out.println("\nDistance TOTALE : " + distance);
        System.out.println("Nombre de sommets visités : " + tour.size());
    }

    private void problem2TwoOddVertices() {
        if (currentUndirectedGraph == null) {
            System.out.println("Erreur : Aucun graphe non orienté chargé.");
            return;
        }

        System.out.println("\n--- T1 P2 - Cas 2 sommets impairs ---");
        System.out.println("Chemin eulérien : passe dans toutes les rues exactement une fois,");
        System.out.println("mais ne revient pas forcément au point de départ.\n");

        List<Vertex> oddVertices = EulerianTour.getOddDegreeVertices(currentUndirectedGraph);
        
        if (oddVertices.size() != 2) {
            System.out.println("ATTENTION : Ce graphe n'a pas exactement 2 sommets impairs.");
            System.out.println("Sommets impairs trouvés : " + oddVertices.size());
            for (Vertex v : oddVertices) {
                System.out.println("  - " + v + " (degré " + currentUndirectedGraph.getDegree(v) + ")");
            }
            if (oddVertices.isEmpty()) {
                System.out.println("\nUtilisez l'option 4 (cas idéal) pour ce graphe.");
            } else {
                System.out.println("\nUtilisez l'option 6 (Postier chinois) pour ce graphe.");
            }
            return;
        }

        System.out.println("Les deux sommets impairs sont :");
        for (int i = 0; i < oddVertices.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + oddVertices.get(i));
        }

        Vertex start = selectVertex("Sélectionnez le sommet de DÉPART (doit être un des deux impairs)", currentUndirectedGraph);
        
        if (!oddVertices.contains(start)) {
            System.out.println("Erreur : Le sommet de départ doit être un des deux sommets impairs.");
            return;
        }

        System.out.println("\nConstruction du chemin eulérien...");
        List<Vertex> path = EulerianTour.findEulerianPath(currentUndirectedGraph, start);
        double distance = EulerianTour.calculateTourDistance(currentUndirectedGraph, path);

        System.out.println("\n--- RÉSULTATS ---");
        System.out.println("Chemin eulérien (chaque arête parcourue exactement une fois) :");
        printPath(path);
        System.out.println("\nDistance TOTALE : " + distance);
        System.out.println("Point de départ : " + path.get(0));
        System.out.println("Point d'arrivée : " + path.get(path.size() - 1));
        System.out.println("\nNote : Ce chemin ne revient pas au point de départ.");
        System.out.println("Pour un camion qui doit revenir au dépôt, utilisez l'option 6 (Postier chinois).");
    }

    private void problem2GeneralCase() {
        if (currentUndirectedGraph == null) {
            System.out.println("Erreur : Aucun graphe non orienté chargé.");
            return;
        }

        System.out.println("\n--- T1 P2 - Cas général : Postier chinois ---");
        System.out.println("Tournée minimale couvrant toutes les rues, même avec des sommets impairs.\n");

        List<Vertex> oddVertices = EulerianTour.getOddDegreeVertices(currentUndirectedGraph);
        
        if (oddVertices.isEmpty()) {
            System.out.println("Ce graphe est eulérien (tous les sommets sont pairs).");
            System.out.println("Utilisez l'option 4 (cas idéal) pour une solution plus simple.");
            return;
        }

        System.out.println("Sommets de degré impair détectés : " + oddVertices.size());
        for (Vertex v : oddVertices) {
            System.out.println("  - " + v + " (degré " + currentUndirectedGraph.getDegree(v) + ")");
        }
        System.out.println("\nApplication de l'algorithme du Postier chinois...");
        System.out.println("(Recherche de l'appariement optimal et duplication minimale d'arêtes)");

        Vertex depot = selectVertex("Sélectionnez le sommet DÉPÔT", currentUndirectedGraph);

        ChinesePostman.Result result = ChinesePostman.solve(currentUndirectedGraph, depot);

        System.out.println("\n--- RÉSULTATS ---");
        System.out.println("Tournée complète (certaines arêtes peuvent être parcourues plusieurs fois) :");
        printPath(result.getTour());
        System.out.println("\nDistance TOTALE : " + result.getTotalDistance());
        System.out.println("Nombre de sommets visités : " + result.getTour().size());
        
        if (!result.getDuplicatedEdges().isEmpty()) {
            System.out.println("\nArêtes parcourues plusieurs fois (duplications nécessaires) : " + 
                             result.getDuplicatedEdges().size());
            // Afficher quelques exemples
            int maxDisplay = Math.min(5, result.getDuplicatedEdges().size());
            for (int i = 0; i < maxDisplay; i++) {
                Edge e = result.getDuplicatedEdges().get(i);
                System.out.println("  - " + e.getFrom().getId() + " ↔ " + e.getTo().getId() + 
                                 " (poids " + e.getWeight() + ")");
            }
            if (result.getDuplicatedEdges().size() > maxDisplay) {
                System.out.println("  ... et " + (result.getDuplicatedEdges().size() - maxDisplay) + " autres");
            }
        } else {
            System.out.println("\nAucune duplication nécessaire (graphe déjà eulérien).");
        }
    }

    private Vertex selectVertex(String prompt, Graph graph) {
        System.out.println("\n" + prompt + " :");
        List<Vertex> vertices = new java.util.ArrayList<>(graph.getVertices());
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
}

