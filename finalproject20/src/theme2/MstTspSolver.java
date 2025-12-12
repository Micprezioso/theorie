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

    /**
     * Optimise le decoupage d'une tournee en sous-tournees pour minimiser la distance totale.
     * Essaie toutes les combinaisons possibles de points pour chaque tournee.
     * 
     * @param graph Le graphe complet
     * @param tour La tournee complete (commence et finit au depot)
     * @param quantities Dictionnaire {sommet -> quantite de dechets a collecter}
     * @param maxCapacity Capacite maximale du camion
     * @return Liste optimisee de sous-tournees
     */
    public static List<List<Vertex>> optimizeSplitByCapacity(
            UndirectedGraph graph,
            List<Vertex> tour, 
            Map<Vertex, Double> quantities, 
            double maxCapacity) {
        
        Vertex depot = tour.get(0);
        List<Vertex> allPoints = new ArrayList<>();
        for (int i = 1; i < tour.size() - 1; i++) {
            allPoints.add(tour.get(i));
        }
        
        // Generer toutes les partitions possibles en 2 groupes
        List<List<Vertex>> bestSubTours = null;
        double bestTotalDistance = Double.POSITIVE_INFINITY;
        
        int n = allPoints.size();
        // Generer toutes les combinaisons de points pour la premiere tournee
        for (long mask = 1; mask < (1L << n) - 1; mask++) {
            List<Vertex> group1 = new ArrayList<>();
            List<Vertex> group2 = new ArrayList<>();
            double load1 = 0.0;
            double load2 = 0.0;
            
            for (int i = 0; i < n; i++) {
                if ((mask & (1L << i)) != 0) {
                    Vertex v = allPoints.get(i);
                    group1.add(v);
                    load1 += quantities.getOrDefault(v, 0.0);
                } else {
                    Vertex v = allPoints.get(i);
                    group2.add(v);
                    load2 += quantities.getOrDefault(v, 0.0);
                }
            }
            
            // Verifier que les deux groupes respectent la capacite
            if (load1 > maxCapacity || load2 > maxCapacity) {
                continue;
            }
            
            // Optimiser l'ordre de chaque groupe avec plus proche voisin
            List<Vertex> tour1 = optimizeTourOrder(graph, depot, group1);
            List<Vertex> tour2 = optimizeTourOrder(graph, depot, group2);
            
            // Calculer les distances
            double dist1 = calculateTourDistance(graph, tour1);
            double dist2 = calculateTourDistance(graph, tour2);
            double totalDist = dist1 + dist2;
            
            if (totalDist < bestTotalDistance) {
                bestTotalDistance = totalDist;
                bestSubTours = new ArrayList<>();
                bestSubTours.add(tour1);
                bestSubTours.add(tour2);
            }
        }
        
        // Si aucune solution en 2 tournees, utiliser le decoupage simple
        if (bestSubTours == null) {
            return splitTourByCapacity(tour, quantities, maxCapacity);
        }
        
        return bestSubTours;
    }
    
    /**
     * Optimise l'ordre des points dans une tournee en utilisant plus proche voisin.
     */
    private static List<Vertex> optimizeTourOrder(UndirectedGraph graph, Vertex depot, List<Vertex> points) {
        if (points.isEmpty()) {
            List<Vertex> result = new ArrayList<>();
            result.add(depot);
            result.add(depot);
            return result;
        }
        
        List<Vertex> optimized = new ArrayList<>();
        optimized.add(depot);
        Set<Vertex> remaining = new HashSet<>(points);
        Vertex current = depot;
        
        while (!remaining.isEmpty()) {
            Vertex nearest = null;
            double minDist = Double.POSITIVE_INFINITY;
            
            for (Vertex candidate : remaining) {
                double dist = graph.getWeight(current, candidate);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = candidate;
                }
            }
            
            if (nearest != null) {
                optimized.add(nearest);
                remaining.remove(nearest);
                current = nearest;
            } else {
                break;
            }
        }
        
        optimized.add(depot);
        return optimized;
    }
}

