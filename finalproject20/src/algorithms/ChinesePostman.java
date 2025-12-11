package algorithms;

import graph.*;

import java.util.*;

/**
 * Algorithme du Postier chinois pour trouver la tournee minimale couvrant toutes les aretes
 * dans un graphe non oriente avec des sommets de degre impair.
 * 
 * Principe :
 * 1. Identifier tous les sommets de degre impair
 * 2. Calculer les plus courts chemins entre toutes les paires de sommets impairs
 * 3. Trouver un appariement parfait de cout minimal entre ces sommets
 * 4. Dupliquer virtuellement les aretes des chemins de l'appariement
 * 5. Le graphe devient eulerien, on peut alors construire une tournee eulerienne
 * 
 * Note importante : on ne modifie PAS le graphe reel, on simule juste les passages supplementaires.
 */
public class ChinesePostman {
    
    /**
     * Resultat de l'algorithme du Postier chinois.
     */
    public static class Result {
        private final List<Vertex> tour;
        private final double totalDistance;
        private final List<Edge> duplicatedEdges; // Arêtes qui doivent être parcourues plusieurs fois

        public Result(List<Vertex> tour, double totalDistance, List<Edge> duplicatedEdges) {
            this.tour = tour;
            this.totalDistance = totalDistance;
            this.duplicatedEdges = duplicatedEdges;
        }

        public List<Vertex> getTour() {
            return tour;
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        public List<Edge> getDuplicatedEdges() {
            return duplicatedEdges;
        }
    }

    /**
     * Resout le probleme du Postier chinois.
     * 
     * @param graph Le graphe (peut avoir des sommets impairs)
     * @param depot Le sommet de depart (depot)
     * @return Un objet Result contenant la tournee et la distance totale
     */
    public static Result solve(UndirectedGraph graph, Vertex depot) {
        if (graph.isEmpty()) {
            throw new IllegalArgumentException("Le graphe est vide");
        }

        // Etape 1 : Identifier les sommets impairs
        List<Vertex> oddVertices = EulerianTour.getOddDegreeVertices(graph);
        
        if (oddVertices.isEmpty()) {
            // Cas ideal : tous les sommets sont pairs, tournee eulerienne simple
            List<Vertex> tour = EulerianTour.findEulerianTour(graph, depot);
            double distance = EulerianTour.calculateTourDistance(graph, tour);
            return new Result(tour, distance, new ArrayList<>());
        }

        if (oddVertices.size() % 2 != 0) {
            throw new IllegalArgumentException(
                "Le nombre de sommets impairs doit etre pair. Trouve : " + oddVertices.size());
        }

        // Etape 2 : Calculer les distances minimales entre toutes les paires de sommets impairs
        Map<Pair<Vertex>, Double> distances = new HashMap<>();
        Map<Pair<Vertex>, List<Vertex>> paths = new HashMap<>();
        
        for (int i = 0; i < oddVertices.size(); i++) {
            Vertex v1 = oddVertices.get(i);
            Dijkstra.Result dijkstraResult = Dijkstra.shortestPaths(graph, v1);
            
            for (int j = i + 1; j < oddVertices.size(); j++) {
                Vertex v2 = oddVertices.get(j);
                double dist = dijkstraResult.getDistance(v2);
                List<Vertex> path = dijkstraResult.getPath(v2);
                
                distances.put(new Pair<>(v1, v2), dist);
                paths.put(new Pair<>(v1, v2), path);
            }
        }

        // Etape 3 : Trouver un appariement parfait de cout minimal
        // Version simplifiee : algorithme glouton (peut ne pas etre optimal mais fonctionne bien)
        List<Pair<Vertex>> matching = findMinimalMatching(oddVertices, distances);

        // Etape 4 : Creer un graphe eulerise (avec les aretes dupliquees)
        UndirectedGraph eulerizedGraph = new UndirectedGraph();
        // Copier toutes les arêtes originales
        for (Vertex v : graph.getVertices()) {
            eulerizedGraph.addVertex(v);
        }
        for (Edge e : graph.getAllEdges()) {
            eulerizedGraph.addEdge(e.getFrom(), e.getTo(), e.getWeight());
        }
        
        // Dupliquer les arêtes des chemins de l'appariement
        List<Edge> duplicatedEdges = new ArrayList<>();
        for (Pair<Vertex> pair : matching) {
            List<Vertex> path = paths.get(pair);
            if (path == null || path.size() < 2) {
                // Essayer dans l'autre sens
                Pair<Vertex> reversePair = new Pair<>(pair.second, pair.first);
                path = paths.get(reversePair);
            }
            
            if (path != null && path.size() >= 2) {
                // Dupliquer chaque arête du chemin
                for (int i = 0; i < path.size() - 1; i++) {
                    Vertex from = path.get(i);
                    Vertex to = path.get(i + 1);
                    double weight = graph.getWeight(from, to);
                    eulerizedGraph.addEdge(from, to, weight);
                    duplicatedEdges.add(new Edge(from, to, weight));
                }
            }
        }

        // Etape 5 : Construire la tournee eulerienne sur le graphe eulerise
        List<Vertex> tour = EulerianTour.findEulerianTour(eulerizedGraph, depot);
        
        // Calculer la distance totale
        double totalDistance = EulerianTour.calculateTourDistance(eulerizedGraph, tour);

        return new Result(tour, totalDistance, duplicatedEdges);
    }

    /**
     * Trouve un appariement parfait de cout minimal entre les sommets impairs.
     * Version simplifiee : algorithme glouton qui choisit toujours la paire de cout minimal.
     * 
     * Note : Pour un appariement optimal, il faudrait utiliser l'algorithme de Blossom,
     * mais cette version gloutonne donne de bons resultats dans la plupart des cas.
     */
    private static List<Pair<Vertex>> findMinimalMatching(
            List<Vertex> oddVertices, 
            Map<Pair<Vertex>, Double> distances) {
        
        List<Pair<Vertex>> matching = new ArrayList<>();
        Set<Vertex> matched = new HashSet<>();
        
        // Créer une liste de toutes les paires triées par distance croissante
        List<PairWithDistance> allPairs = new ArrayList<>();
        for (Map.Entry<Pair<Vertex>, Double> entry : distances.entrySet()) {
            allPairs.add(new PairWithDistance(entry.getKey(), entry.getValue()));
        }
        allPairs.sort(Comparator.comparingDouble(p -> p.distance));

        // Algorithme glouton : prendre les paires de coût minimal qui ne se chevauchent pas
        for (PairWithDistance pairDist : allPairs) {
            Pair<Vertex> pair = pairDist.pair;
            if (!matched.contains(pair.first) && !matched.contains(pair.second)) {
                matching.add(pair);
                matched.add(pair.first);
                matched.add(pair.second);
            }
        }

        if (matched.size() != oddVertices.size()) {
            throw new IllegalStateException("Impossible de trouver un appariement parfait");
        }

        return matching;
    }

    /**
     * Classe auxiliaire pour représenter une paire de sommets.
     */
    private static class Pair<T> {
        final T first;
        final T second;

        Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair<?> pair = (Pair<?>) obj;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }

    /**
     * Classe auxiliaire pour une paire avec sa distance.
     */
    private static class PairWithDistance {
        final Pair<Vertex> pair;
        final double distance;

        PairWithDistance(Pair<Vertex> pair, double distance) {
            this.pair = pair;
            this.distance = distance;
        }
    }
}

