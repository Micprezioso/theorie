package theme2;

import graph.*;

import java.util.*;

/**
 * Resout le probleme du voyageur de commerce (TSP) en utilisant l'approche MST.
 * 
 * Principe :
 * 1. Construire un arbre couvrant de poids minimum (MST)
 * 2. Parcourir l'arbre en profondeur (DFS) pour obtenir une tournee
 * 3. Appliquer du "shortcutting" pour eviter de revisiter les sommets
 * 4. Calculer le cout reel de la tournee sur le graphe complet
 * 
 * Cette approche garantit une solution a au plus 2 fois l'optimal.
 */
public class MstTspSolver {
    
    /**
     * Resultat de l'algorithme MST-TSP.
     */
    public static class Result {
        private final List<Vertex> tour;
        private final double totalDistance;
        private final double mstWeight;

        public Result(List<Vertex> tour, double totalDistance, double mstWeight) {
            this.tour = tour;
            this.totalDistance = totalDistance;
            this.mstWeight = mstWeight;
        }

        public List<Vertex> getTour() {
            return tour;
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        public double getMstWeight() {
            return mstWeight;
        }
    }

    /**
     * Resout le TSP avec l'approche MST.
     * 
     * @param graph Le graphe non oriente complet (doit etre connecte)
     * @param depot Le sommet de depart (depot)
     * @return Un objet Result contenant la tournee et la distance totale
     */
    public static Result solve(UndirectedGraph graph, Vertex depot) {
        if (graph.isEmpty()) {
            throw new IllegalArgumentException("Le graphe est vide");
        }
        
        if (!graph.getVertices().contains(depot)) {
            throw new IllegalArgumentException("Le dépôt n'existe pas dans le graphe");
        }

        // Etape 1 : Construire le MST
        UndirectedGraph mst = MstBuilder.buildMst(graph, depot);
        double mstWeight = MstBuilder.calculateMstWeight(mst);
        
        // Etape 2 : Parcourir l'arbre en profondeur
        List<Vertex> dfsTour = dfsTraversal(mst, depot);
        
        // Etape 3 : Appliquer le shortcutting
        List<Vertex> shortcutTour = applyShortcutting(dfsTour);
        
        // S'assurer que la tournee commence et finit au depot
        if (!shortcutTour.get(0).equals(depot)) {
            shortcutTour.add(0, depot);
        }
        if (!shortcutTour.get(shortcutTour.size() - 1).equals(depot)) {
            shortcutTour.add(depot);
        }
        
        // Etape 4 : Calculer le cout reel sur le graphe complet
        double totalDistance = calculateTourDistance(graph, shortcutTour);
        
        return new Result(shortcutTour, totalDistance, mstWeight);
    }

    /**
     * Parcourt l'arbre MST en profondeur (DFS) a partir du depot.
     */
    private static List<Vertex> dfsTraversal(UndirectedGraph mst, Vertex start) {
        List<Vertex> tour = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        
        dfsHelper(mst, start, visited, tour);
        
        return tour;
    }

    /**
     * Helper recursif pour le DFS.
     */
    private static void dfsHelper(UndirectedGraph mst, Vertex current, Set<Vertex> visited, List<Vertex> tour) {
        visited.add(current);
        tour.add(current);
        
        for (Vertex neighbor : mst.getNeighbors(current)) {
            if (!visited.contains(neighbor)) {
                dfsHelper(mst, neighbor, visited, tour);
                // Revenir au sommet parent apres avoir visite un sous-arbre
                tour.add(current);
            }
        }
    }

    /**
     * Applique le shortcutting : si un sommet est revisite, ne le garder qu'a la premiere visite.
     */
    private static List<Vertex> applyShortcutting(List<Vertex> tour) {
        List<Vertex> shortcutTour = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        
        for (Vertex v : tour) {
            if (!visited.contains(v)) {
                shortcutTour.add(v);
                visited.add(v);
            }
        }
        
        return shortcutTour;
    }

    /**
     * Calcule la distance totale d'une tournee sur le graphe complet.
     */
    private static double calculateTourDistance(UndirectedGraph graph, List<Vertex> tour) {
        if (tour.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            Vertex from = tour.get(i);
            Vertex to = tour.get(i + 1);
            double weight = graph.getWeight(from, to);
            
            if (weight == Double.POSITIVE_INFINITY) {
                throw new IllegalArgumentException("Arete manquante entre " + from + " et " + to + 
                    ". Le graphe doit etre complet pour cette approche.");
            }
            
            totalDistance += weight;
        }
        return totalDistance;
    }

    /**
     * Decoupe une tournee en plusieurs sous-tournees respectant une capacite maximale.
     * 
     * @param tour La tournee complete (commence et finit au depot)
     * @param quantities Dictionnaire {sommet -> quantite de dechets a collecter}
     * @param maxCapacity Capacite maximale du camion
     * @return Liste de sous-tournees, chacune commencant et finissant au depot
     */
    public static List<List<Vertex>> splitTourByCapacity(
            List<Vertex> tour, 
            Map<Vertex, Double> quantities, 
            double maxCapacity) {
        
        List<List<Vertex>> subTours = new ArrayList<>();
        List<Vertex> currentSubTour = new ArrayList<>();
        double currentLoad = 0.0;
        Vertex depot = tour.get(0);
        
        // Commencer au depot
        currentSubTour.add(depot);
        
        // Parcourir la tournee (en excluant le depot initial et final)
        for (int i = 1; i < tour.size() - 1; i++) {
            Vertex vertex = tour.get(i);
            double quantity = quantities.getOrDefault(vertex, 0.0);
            
            if (currentLoad + quantity > maxCapacity) {
                // Retourner au depot pour decharger
                currentSubTour.add(depot);
                subTours.add(new ArrayList<>(currentSubTour));
                
                // Commencer une nouvelle sous-tournee
                currentSubTour.clear();
                currentSubTour.add(depot);
                currentLoad = 0.0;
            }
            
            // Ajouter ce sommet a la sous-tournee actuelle
            currentSubTour.add(vertex);
            currentLoad += quantity;
        }
        
        // Ajouter la derniere sous-tournee
        if (currentSubTour.size() > 1) {
            currentSubTour.add(depot);
            subTours.add(currentSubTour);
        }
        
        return subTours;
    }
}

