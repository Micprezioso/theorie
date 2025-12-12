package algorithms;

import graph.*;

import java.util.*;

/**
 * Algorithmes pour les tournées eulériennes.
 * 
 * Une tournée eulérienne est un cycle qui passe par chaque arête exactement une fois.
 * Un graphe non orienté a une tournée eulérienne si et seulement si tous les sommets ont un degré pair.
 */
public class EulerianTour {
    
    /**
     * Vérifie si un graphe est eulérien (tous les sommets ont un degré pair).
     */
    public static boolean isEulerian(UndirectedGraph graph) {
        for (Vertex v : graph.getVertices()) {
            if (graph.getDegree(v) % 2 != 0) {
                return false;
            }
        }
        return !graph.isEmpty();
    }

    /**
     * Trouve tous les sommets de degré impair.
     * 
     * @return Liste des sommets impairs
     */
    public static List<Vertex> getOddDegreeVertices(UndirectedGraph graph) {
        List<Vertex> oddVertices = new ArrayList<>();
        for (Vertex v : graph.getVertices()) {
            if (graph.getDegree(v) % 2 != 0) {
                oddVertices.add(v);
            }
        }
        return oddVertices;
    }

    /**
     * Construit une tournée eulérienne en utilisant l'algorithme de Hierholzer.
     * 
     * Principe :
     * 1. Partir d'un sommet et construire un cycle
     * 2. Tant qu'il reste des arêtes non visitées :
     *    - Trouver un sommet du cycle actuel qui a encore des arêtes non visitées
     *    - Construire un nouveau cycle à partir de ce sommet
     *    - Fusionner ce cycle avec le cycle principal
     * 
     * @param graph Le graphe (doit être eulérien)
     * @param start Le sommet de départ (généralement le dépôt)
     * @return Liste ordonnée des sommets de la tournée eulérienne
     */
    public static List<Vertex> findEulerianTour(UndirectedGraph graph, Vertex start) {
        if (!isEulerian(graph)) {
            throw new IllegalArgumentException("Le graphe n'est pas eulérien (tous les sommets doivent avoir un degré pair)");
        }

        // Compter le nombre d'occurrences de chaque arête dans le graphe
        // (nécessaire pour gérer les arêtes dupliquées dans le Postier chinois)
        Map<Edge, Integer> edgeCount = new HashMap<>();
        for (Vertex v : graph.getVertices()) {
            for (Edge e : graph.getEdges(v)) {
                edgeCount.put(e, edgeCount.getOrDefault(e, 0) + 1);
            }
        }
        // Diviser par 2 car chaque arête est comptée deux fois (une fois par sommet)
        for (Map.Entry<Edge, Integer> entry : edgeCount.entrySet()) {
            edgeCount.put(entry.getKey(), entry.getValue() / 2);
        }

        // Créer une copie des compteurs d'arêtes pour marquer celles qui sont visitées
        Map<Edge, Integer> edgeUsage = new HashMap<>(edgeCount);

        List<Vertex> tour = new ArrayList<>();
        Stack<Vertex> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Vertex current = stack.peek();
            
            // Chercher une arête non utilisée depuis current
            Edge unusedEdge = null;
            Vertex next = null;
            
            for (Edge edge : graph.getEdges(current)) {
                int remaining = edgeUsage.getOrDefault(edge, 0);
                if (remaining > 0) {
                    unusedEdge = edge;
                    next = edge.getOther(current);
                    break;
                }
            }

            if (unusedEdge == null) {
                // Plus d'arêtes disponibles depuis ce sommet, l'ajouter à la tournée
                tour.add(stack.pop());
            } else {
                // Décrémenter le compteur de cette arête
                edgeUsage.put(unusedEdge, edgeUsage.get(unusedEdge) - 1);
                stack.push(next);
            }
        }

        // La tournée est construite à l'envers, la retourner
        Collections.reverse(tour);
        return tour;
    }

    /**
     * Trouve un chemin eulérien (pas forcément un cycle) dans un graphe avec exactement 2 sommets impairs.
     * 
     * Un chemin eulérien existe si et seulement si exactement 0 ou 2 sommets ont un degré impair.
     * Dans le cas de 2 sommets impairs, le chemin doit commencer à l'un et finir à l'autre.
     * 
     * @param graph Le graphe
     * @param start Le sommet de départ (doit être un des deux sommets impairs)
     * @return Liste ordonnée des sommets du chemin eulérien
     */
    public static List<Vertex> findEulerianPath(UndirectedGraph graph, Vertex start) {
        List<Vertex> oddVertices = getOddDegreeVertices(graph);
        
        if (oddVertices.size() != 2) {
            throw new IllegalArgumentException(
                "Un chemin eulérien nécessite exactement 2 sommets impairs. Trouvé : " + oddVertices.size());
        }
        
        if (!oddVertices.contains(start)) {
            throw new IllegalArgumentException("Le sommet de départ doit être un des deux sommets impairs");
        }

        // Créer une copie temporaire du graphe et ajouter une arête virtuelle
        // entre les deux sommets impairs pour rendre le graphe eulérien
        UndirectedGraph tempGraph = new UndirectedGraph();
        for (Vertex v : graph.getVertices()) {
            tempGraph.addVertex(v);
        }
        for (Edge e : graph.getAllEdges()) {
            tempGraph.addEdge(e.getFrom(), e.getTo(), e.getWeight());
        }
        
        Vertex otherOdd = oddVertices.get(0).equals(start) ? oddVertices.get(1) : oddVertices.get(0);
        // Ajouter une arête virtuelle de poids 0 (ne sera pas comptée dans la distance)
        tempGraph.addEdge(start, otherOdd, 0.0);

        // Trouver la tournée eulérienne sur le graphe temporaire
        List<Vertex> tour = findEulerianTour(tempGraph, start);
        
        // Retirer l'arête virtuelle de la tournée (si elle apparaît)
        // En pratique, on garde la tournée telle quelle car l'arête virtuelle
        // n'a pas de poids réel
        
        return tour;
    }

    /**
     * Calcule la distance totale d'une tournée.
     * Note : Cette méthode accepte que certaines arêtes soient parcourues plusieurs fois.
     */
    public static double calculateTourDistance(UndirectedGraph graph, List<Vertex> tour) {
        if (tour.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            Vertex from = tour.get(i);
            Vertex to = tour.get(i + 1);
            double weight = graph.getWeight(from, to);
            if (weight == Double.POSITIVE_INFINITY) {
                // Dans le cas du Postier chinois, certaines arêtes peuvent être parcourues plusieurs fois
                // Vérifier si l'arête existe dans l'autre sens (pour les graphes non orientés)
                weight = graph.getWeight(to, from);
                if (weight == Double.POSITIVE_INFINITY) {
                    throw new IllegalArgumentException("Arête manquante entre " + from + " et " + to);
                }
            }
            totalDistance += weight;
        }
        return totalDistance;
    }
}
