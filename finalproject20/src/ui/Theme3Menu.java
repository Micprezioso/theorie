package ui;

import graph.*;
import theme3.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Menu console interactif pour le Thème 3 - Planification des jours de collecte.
 */
public class Theme3Menu {
    private Scanner scanner;
    private UndirectedGraph currentGraph;
    private Map<Vertex, Double> currentQuantities;
    private static final double DEFAULT_MAX_CAPACITY = 10.0;
    
    public Theme3Menu(Scanner scanner) {
        this.scanner = scanner;
        this.currentGraph = null;
        this.currentQuantities = null;
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
                    hypothesis1();
                    break;
                case 3:
                    hypothesis2();
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
        System.out.println("  THÈME 3 - PLANIFICATION DES JOURS DE COLLECTE");
        System.out.println("=".repeat(60));
        System.out.println("1) Charger un graphe depuis un fichier");
        System.out.println("2) Hypothèse 1 : Voisinage uniquement (coloration)");
        System.out.println("3) Hypothèse 2 : Voisinage + capacité (rééquilibrage)");
        System.out.println("0) Retour au menu principal");
        System.out.println("=".repeat(60));
    }
    
    private void loadGraph() {
        System.out.println("\n--- Chargement d'un graphe ---");
        
        // Lister les fichiers .txt disponibles
        List<File> txtFiles = findTxtFiles();
        
        String filePath;
        if (!txtFiles.isEmpty()) {
            System.out.println("\nFichiers disponibles :");
            for (int i = 0; i < txtFiles.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + txtFiles.get(i).getName());
            }
            System.out.println("  0) Saisir un chemin manuellement");
            System.out.println();
            
            int fileChoice = readInt("Sélectionnez un fichier (0 pour chemin manuel) : ");
            
            if (fileChoice == 0) {
                System.out.print("Chemin du fichier : ");
                filePath = scanner.nextLine().trim();
            } else if (fileChoice >= 1 && fileChoice <= txtFiles.size()) {
                filePath = txtFiles.get(fileChoice - 1).getPath();
                System.out.println("Fichier sélectionné : " + txtFiles.get(fileChoice - 1).getName());
            } else {
                System.out.println("Choix invalide. Utilisation du chemin manuel.");
                System.out.print("Chemin du fichier : ");
                filePath = scanner.nextLine().trim();
            }
        } else {
            System.out.print("Aucun fichier .txt trouvé. Chemin du fichier : ");
            filePath = scanner.nextLine().trim();
        }
        
        try {
            Theme3GraphLoader.LoadResult result = Theme3GraphLoader.loadGraphWithQuantities(filePath);
            currentGraph = result.getGraph();
            currentQuantities = result.getQuantities();
            
            System.out.println("\nGraphe chargé avec succès !");
            System.out.println("Nombre de quartiers : " + currentGraph.getVertexCount());
            System.out.println("Nombre de voisinages : " + currentGraph.getEdgeCount());
            System.out.println("\nQuartiers et quantités :");
            for (Vertex v : currentGraph.getVertices()) {
                double qty = currentQuantities.getOrDefault(v, 0.0);
                System.out.println("  " + v.getId() + " : " + qty + " tonnes");
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de format : " + e.getMessage());
        }
    }
    
    private void hypothesis1() {
        if (currentGraph == null) {
            System.out.println("Erreur : Aucun graphe chargé.");
            return;
        }
        
        System.out.println("\n--- Hypothèse 1 : Voisinage uniquement (coloration) ---");
        System.out.println("Planification des jours de collecte en respectant uniquement");
        System.out.println("la contrainte de voisinage (deux quartiers voisins ne peuvent");
        System.out.println("pas être collectés le même jour).\n");
        
        System.out.println("Application de l'algorithme de Welsh & Powell...\n");
        
        WelshPowellColoring.ColoringResult result = WelshPowellColoring.colorGraph(currentGraph);
        
        // Afficher les résultats
        System.out.println("--- PLANIFICATION INITIALE (H1) ---\n");
        
        Map<Integer, List<Vertex>> colorToVertices = result.getColorToVertices();
        List<Integer> sortedDays = new ArrayList<>(colorToVertices.keySet());
        Collections.sort(sortedDays);
        
        for (Integer day : sortedDays) {
            List<Vertex> vertices = colorToVertices.get(day);
            System.out.print("Jour " + day + " : ");
            for (int i = 0; i < vertices.size(); i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(vertices.get(i).getId());
            }
            System.out.println();
        }
        
        System.out.println("\nNombre de jours utilisés : " + result.getChromaticNumber());
        System.out.println("Nombre chromatique trouvé : " + result.getChromaticNumber());
    }
    
    private void hypothesis2() {
        if (currentGraph == null) {
            System.out.println("Erreur : Aucun graphe chargé.");
            return;
        }
        
        System.out.println("\n--- Hypothèse 2 : Voisinage + capacité (rééquilibrage) ---");
        System.out.println("Planification avec contraintes de voisinage ET de capacité");
        System.out.println("journalière des camions.\n");
        
        // Demander la capacité maximale
        System.out.print("Capacité maximale journalière (Qmax) en tonnes [" + DEFAULT_MAX_CAPACITY + "] : ");
        String input = scanner.nextLine().trim();
        double maxCapacity;
        if (input.isEmpty()) {
            maxCapacity = DEFAULT_MAX_CAPACITY;
        } else {
            try {
                maxCapacity = Double.parseDouble(input);
                if (maxCapacity <= 0) {
                    System.out.println("Capacité invalide. Utilisation de la valeur par défaut : " + DEFAULT_MAX_CAPACITY);
                    maxCapacity = DEFAULT_MAX_CAPACITY;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valeur invalide. Utilisation de la valeur par défaut : " + DEFAULT_MAX_CAPACITY);
                maxCapacity = DEFAULT_MAX_CAPACITY;
            }
        }
        
        System.out.println("\n=== PHASE 1 : Planification initiale (H1) ===\n");
        
        // Appliquer H1
        WelshPowellColoring.ColoringResult initialColoring = WelshPowellColoring.colorGraph(currentGraph);
        
        Map<Integer, List<Vertex>> colorToVertices = initialColoring.getColorToVertices();
        List<Integer> sortedDays = new ArrayList<>(colorToVertices.keySet());
        Collections.sort(sortedDays);
        
        for (Integer day : sortedDays) {
            List<Vertex> vertices = colorToVertices.get(day);
            System.out.print("Jour " + day + " : ");
            for (int i = 0; i < vertices.size(); i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(vertices.get(i).getId());
            }
            System.out.println();
        }
        
        System.out.println("\nNombre de jours utilisés : " + initialColoring.getChromaticNumber());
        
        // PHASE 2 : Calcul des charges journalières
        System.out.println("\n=== PHASE 2 : Calcul des charges journalières ===\n");
        
        Map<Integer, Double> dayToTotalQuantity = new HashMap<>();
        for (Map.Entry<Vertex, Integer> entry : initialColoring.getVertexToColor().entrySet()) {
            int day = entry.getValue();
            double quantity = currentQuantities.getOrDefault(entry.getKey(), 0.0);
            dayToTotalQuantity.put(day, dayToTotalQuantity.getOrDefault(day, 0.0) + quantity);
        }
        
        // Inverser l'ordre pour l'affichage
        Collections.reverse(sortedDays);
        
        for (Integer day : sortedDays) {
            List<Vertex> vertices = colorToVertices.get(day);
            double total = dayToTotalQuantity.get(day);
            System.out.print("Jour " + day + " (");
            for (int i = 0; i < vertices.size(); i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(vertices.get(i).getId());
            }
            System.out.print(") : " + String.format("%.1f", total) + " tonnes");
            if (total > maxCapacity) {
                System.out.println(" → > " + maxCapacity + " tonnes → Surchargé");
            } else {
                System.out.println(" → OK");
            }
        }
        
        // PHASE 3 : Détection des jours surchargés
        System.out.println("\n=== PHASE 3 : Détection des jours surchargés ===\n");
        
        List<Integer> overloadedDays = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : dayToTotalQuantity.entrySet()) {
            if (entry.getValue() > maxCapacity) {
                overloadedDays.add(entry.getKey());
                System.out.println("Jour " + entry.getKey() + " surchargé : " + 
                                 String.format("%.1f", entry.getValue()) + " > " + 
                                 maxCapacity + " tonnes. Rééquilibrage nécessaire…");
            }
        }
        
        if (overloadedDays.isEmpty()) {
            System.out.println("Aucun jour surchargé. Le planning initial respecte toutes les contraintes.");
            return;
        }
        
        // PHASE 4 : Rééquilibrage
        System.out.println("\n=== PHASE 4 : Rééquilibrage des quartiers ===\n");
        
        CapacityRebalancer.RebalancingResult rebalanced = CapacityRebalancer.rebalance(
            currentGraph, initialColoring, currentQuantities, maxCapacity);
        
        // Afficher les déplacements
        List<CapacityRebalancer.MoveMessage> moves = rebalanced.getMoveMessages();
        if (moves.isEmpty()) {
            System.out.println("Aucun déplacement nécessaire.\n");
        } else {
            for (CapacityRebalancer.MoveMessage move : moves) {
                if (move.isNewDay()) {
                    System.out.println("Aucun jour disponible pour le quartier " + move.getVertex().getId() + 
                                     " → création d'un nouveau jour (Jour " + move.getToDay() + ").");
                } else {
                    System.out.println("Quartier " + move.getVertex().getId() + " déplacé du Jour " + 
                                     move.getFromDay() + " au Jour " + move.getToDay() + ".");
                }
            }
            System.out.println();
        }
        
        // PHASE 5 : Planning final
        System.out.println("=== PHASE 5 : Planning final corrigé ===\n");
        
        Map<Integer, List<Vertex>> finalDayToVertices = rebalanced.getDayToVertices();
        Map<Integer, Double> finalDayToTotal = rebalanced.getDayToTotalQuantity();
        List<Integer> finalSortedDays = new ArrayList<>(finalDayToVertices.keySet());
        Collections.sort(finalSortedDays);
        
        for (Integer day : finalSortedDays) {
            List<Vertex> vertices = finalDayToVertices.get(day);
            double total = finalDayToTotal.get(day);
            System.out.print("Jour " + day + " : ");
            for (int i = 0; i < vertices.size(); i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(vertices.get(i).getId());
            }
            System.out.println(" (" + String.format("%.1f", total) + " t)");
        }
        
        System.out.println("\nToutes les contraintes de voisinage et de capacité sont respectées.");
        System.out.println("Nombre total de jours utilisés : " + rebalanced.getTotalDays());
    }
    
    /**
     * Trouve tous les fichiers .txt dans le répertoire courant.
     */
    private List<File> findTxtFiles() {
        List<File> txtFiles = new ArrayList<>();
        try {
            File currentDir = new File(System.getProperty("user.dir"));
            File[] files = currentDir.listFiles();
            
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                        txtFiles.add(file);
                    }
                }
            }
        } catch (Exception e) {
            // En cas d'erreur, on retourne une liste vide
        }
        return txtFiles;
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

