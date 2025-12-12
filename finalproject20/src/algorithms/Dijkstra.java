package algorithms;

import graph.*;

import java.util.*;

/**
 * Implementation de l'algorithme de Dijkstra pour trouver les plus courts chemins
 * dans un graphe pondere a poids positifs.
 * 
 * Pourquoi Dijkstra ?
 * - Efficace pour les graphes a poids positifs (complexite O(V²) ou O(E log V) avec une file de priorite)
 * - Plus simple que Bellman-Ford pour ce cas
 * - Plus precis que BFS qui ne prend pas en compte les poids
 * 
 * L'algorithme garantit de trouver le plus court chemin depuis une source vers tous les autres sommets
 * dans un graphe a poids positifs.
 */
public class Dijkstra {
    
    /**
     * Resultat d'une execution de Dijkstra : distances et chemins.
     */
    public static class Result {
        private final Map<Vertex, Double> distances;
        private final Map<Vertex, Vertex> predecessors;
        private final Vertex source;

        public Result(Vertex source, Map<Vertex, Double> distances, Map<Vertex, Vertex> predecessors) {
            this.source = source;
            this.distances = distances;
            this.predecessors = predecessors;
        }

        public double getDistance(Vertex target) {
            return distances.getOrDefault(target, Double.POSITIVE_INFINITY);
        }

        public List<Vertex> getPath(Vertex target) {
            List<Vertex> path = new ArrayList<>();
            if (distances.getOrDefault(target, Double.POSITIVE_INFINITY) == Double.POSITIVE_INFINITY) {
                return path; // Pas de chemin
            }

            Vertex current = target;
            while (current != null) {
                path.add(0, current);
                current = predecessors.get(current);
            }
            return path;
        }

        public Vertex getSource() {
            return source;
        }
    }

    /**
     * Execute l'algorithme de Dijkstra sur un graphe non oriente.
     * 
     * @param graph Le graphe non oriente
     * @param source Le sommet source
     * @return Un objet Result contenant les distances et les chemins
     */
    public static Result shortestPaths(UndirectedGraph graph, Vertex source) {
        if (!graph.getVertices().contains(source)) {
            throw new IllegalArgumentException("Le sommet source n'existe pas dans le graphe");
        }

        Map<Vertex, Double> distances = new HashMap<>();
        Map<Vertex, Vertex> predecessors = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();
        
        // Initialisation : distance infinie pour tous sauf la source
        for (Vertex v : graph.getVertices()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);

        // File de priorité : (distance, sommet)
        PriorityQueue<VertexDistance> queue = new PriorityQueue<>();
        queue.add(new VertexDistance(source, 0.0));

        while (!queue.isEmpty()) {
            VertexDistance current = queue.poll();
            Vertex u = current.vertex;
            
            if (visited.contains(u)) {
                continue;
            }
            visited.add(u);

            // Examiner tous les voisins de u
            for (Vertex v : graph.getNeighbors(u)) {
                if (visited.contains(v)) {
                    continue;
                }

                double edgeWeight = graph.getWeight(u, v);
                double newDistance = distances.get(u) + edgeWeight;

                if (newDistance < distances.get(v)) {
                    distances.put(v, newDistance);
                    predecessors.put(v, u);
                    queue.add(new VertexDistance(v, newDistance));
                }
            }
        }

        return new Result(source, distances, predecessors);
    }

    /**
     * Execute l'algorithme de Dijkstra sur un graphe oriente.
     * 
     * @param graph Le graphe oriente
     * @param source Le sommet source
     * @return Un objet Result contenant les distances et les chemins
     */
    public static Result shortestPaths(DirectedGraph graph, Vertex source) {
        if (!graph.getVertices().contains(source)) {
            throw new IllegalArgumentException("Le sommet source n'existe pas dans le graphe");
        }

        Map<Vertex, Double> distances = new HashMap<>();
        Map<Vertex, Vertex> predecessors = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();
        
        // Initialisation : distance infinie pour tous sauf la source
        for (Vertex v : graph.getVertices()) {
            distances.put(v, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);

        // File de priorité : (distance, sommet)
        PriorityQueue<VertexDistance> queue = new PriorityQueue<>();
        queue.add(new VertexDistance(source, 0.0));

        while (!queue.isEmpty()) {
            VertexDistance current = queue.poll();
            Vertex u = current.vertex;
            
            if (visited.contains(u)) {
                continue;
            }
            visited.add(u);

            // Examiner tous les successeurs de u (via les arcs sortants)
            for (Vertex v : graph.getNeighbors(u)) {
                if (visited.contains(v)) {
                    continue;
                }

                double edgeWeight = graph.getWeight(u, v);
                double newDistance = distances.get(u) + edgeWeight;

                if (newDistance < distances.get(v)) {
                    distances.put(v, newDistance);
                    predecessors.put(v, u);
                    queue.add(new VertexDistance(v, newDistance));
                }
            }
        }

        return new Result(source, distances, predecessors);
    }

    /**
     * Classe auxiliaire pour la file de priorité.
     */
    private static class VertexDistance implements Comparable<VertexDistance> {
        final Vertex vertex;
        final double distance;

        VertexDistance(Vertex vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(VertexDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }
}

