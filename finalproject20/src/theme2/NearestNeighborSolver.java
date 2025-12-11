package theme2;

import graph.*;

import java.util.*;

/**
 * Resout le probleme du voyageur de commerce (TSP) en utilisant l'approche
 * du plus proche voisin (Nearest Neighbor).
 * 
 * Principe :
 * - Partir du depot
 * - A chaque etape, aller vers le point de collecte non visite le plus proche
 * - Repeter jusqu'a ce que tous les sommets soient visites
 * - Revenir au depot
 * 
 * Limites : Cette approche ne garantit pas la solution optimale, mais elle est
 * rapide et donne generalement de bons resultats pour des graphes de taille moyenne.
 */
public class NearestNeighborSolver {
    
    /**
     * Resultat de l'algorithme du plus proche voisin.
     */
    public static class Result {
        private final List<Vertex> tour;
        private final double totalDistance;

        public Result(List<Vertex> tour, double totalDistance) {
            this.tour = tour;
            this.totalDistance = totalDistance;
        }

        public List<Vertex> getTour() {
            return tour;
        }

        public double getTotalDistance() {
            return totalDistance;
        }
    }

    /**
     * Resout le TSP avec l'approche du plus proche voisin.
     * 
     * @param graph Le graphe non oriente (doit etre connecte)
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

        List<Vertex> tour = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        
        // Commencer au depot
        Vertex current = depot;
        tour.add(current);
        visited.add(current);
        
        // Visiter tous les autres sommets
        while (visited.size() < graph.getVertexCount()) {
            Vertex nearest = findNearestUnvisited(graph, current, visited);
            
            if (nearest == null) {
                // Cas ou le graphe n'est pas connecte
                throw new IllegalStateException("Le graphe n'est pas connecte");
            }
            
            tour.add(nearest);
            visited.add(nearest);
            current = nearest;
        }
        
        // Revenir au depot
        tour.add(depot);
        
        // Calculer la distance totale
        double totalDistance = calculateTourDistance(graph, tour);
        
        return new Result(tour, totalDistance);
    }

    /**
     * Trouve le sommet non visite le plus proche du sommet courant.
     */
    private static Vertex findNearestUnvisited(UndirectedGraph graph, Vertex current, Set<Vertex> visited) {
        Vertex nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        
        for (Vertex neighbor : graph.getNeighbors(current)) {
            if (!visited.contains(neighbor)) {
                double distance = graph.getWeight(current, neighbor);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = neighbor;
                }
            }
        }
        
        // Si aucun voisin direct n'est disponible, chercher parmi tous les sommets non visites
        // (necessaire si le graphe n'est pas complet)
        if (nearest == null) {
            for (Vertex v : graph.getVertices()) {
                if (!visited.contains(v) && !v.equals(current)) {
                    // Utiliser un algorithme de plus court chemin (Dijkstra simplifie)
                    double distance = findShortestPathDistance(graph, current, v, visited);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest = v;
                    }
                }
            }
        }
        
        return nearest;
    }

    /**
     * Trouve la distance du plus court chemin entre deux sommets.
     * Version simplifiee pour le cas ou les voisins directs ne suffisent pas.
     * 
     * Note : On peut passer par des sommets visites comme intermediaires,
     * mais on ne peut pas les utiliser comme destination finale.
     */
    private static double findShortestPathDistance(UndirectedGraph graph, Vertex from, Vertex to, Set<Vertex> visited) {
        // BFS simple pour trouver le plus court chemin
        // On peut passer par des sommets visites, mais on ne peut pas les utiliser comme destination
        Queue<Vertex> queue = new LinkedList<>();
        Map<Vertex, Double> distances = new HashMap<>();
        Set<Vertex> explored = new HashSet<>();
        
        queue.add(from);
        distances.put(from, 0.0);
        explored.add(from);
        
        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            double currentDist = distances.get(current);
            
            if (current.equals(to)) {
                return currentDist;
            }
            
            for (Vertex neighbor : graph.getNeighbors(current)) {
                // On peut explorer un sommet meme s'il est visite (pour passer par lui)
                // mais on ne peut pas l'utiliser comme destination finale
                if (!explored.contains(neighbor)) {
                    double newDist = currentDist + graph.getWeight(current, neighbor);
                    distances.put(neighbor, newDist);
                    explored.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Calcule la distance totale d'une tournee.
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
                // Si pas d'arete directe, calculer le plus court chemin
                weight = findShortestPathDistance(graph, from, to, new HashSet<>());
                if (weight == Double.POSITIVE_INFINITY) {
                    throw new IllegalArgumentException("Impossible de calculer la distance entre " + from + " et " + to);
                }
            }
            
            totalDistance += weight;
        }
        return totalDistance;
    }
}
