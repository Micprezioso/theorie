package theme2;

import graph.*;

import java.util.*;

/**
 * Construit un arbre couvrant de poids minimum (MST) en utilisant l'algorithme de Prim.
 * 
 * Principe de Prim :
 * - Partir d'un sommet arbitraire
 * - A chaque etape, ajouter l'arete de poids minimal qui connecte un sommet
 *   de l'arbre a un sommet hors de l'arbre
 * - Repeter jusqu'a ce que tous les sommets soient dans l'arbre
 */
public class MstBuilder {
    
    /**
     * Represente une arete avec son poids, utilisee pour la construction du MST.
     */
    private static class EdgeWithWeight implements Comparable<EdgeWithWeight> {
        final Vertex from;
        final Vertex to;
        final double weight;

        EdgeWithWeight(Vertex from, Vertex to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(EdgeWithWeight other) {
            return Double.compare(this.weight, other.weight);
        }
    }

    /**
     * Construit un MST en utilisant l'algorithme de Prim.
     * 
     * @param graph Le graphe non oriente (doit etre connecte)
     * @param start Le sommet de depart (peut etre n'importe quel sommet)
     * @return Un graphe non oriente representant le MST (arbre)
     */
    public static UndirectedGraph buildMst(UndirectedGraph graph, Vertex start) {
        if (graph.isEmpty()) {
            throw new IllegalArgumentException("Le graphe est vide");
        }
        
        if (!graph.getVertices().contains(start)) {
            throw new IllegalArgumentException("Le sommet de départ n'existe pas dans le graphe");
        }

        UndirectedGraph mst = new UndirectedGraph();
        Set<Vertex> inMst = new HashSet<>();
        PriorityQueue<EdgeWithWeight> edgeQueue = new PriorityQueue<>();
        
        // Initialiser avec le sommet de depart
        mst.addVertex(start);
        inMst.add(start);
        
        // Ajouter toutes les aretes partant du sommet de depart
        for (Vertex neighbor : graph.getNeighbors(start)) {
            double weight = graph.getWeight(start, neighbor);
            edgeQueue.add(new EdgeWithWeight(start, neighbor, weight));
        }
        
        // Construire le MST
        while (inMst.size() < graph.getVertexCount() && !edgeQueue.isEmpty()) {
            EdgeWithWeight minEdge = edgeQueue.poll();
            
            // Verifier si cette arete connecte un nouveau sommet
            Vertex newVertex = null;
            if (inMst.contains(minEdge.from) && !inMst.contains(minEdge.to)) {
                newVertex = minEdge.to;
            } else if (inMst.contains(minEdge.to) && !inMst.contains(minEdge.from)) {
                newVertex = minEdge.from;
            }
            
            if (newVertex != null) {
                // Ajouter cette arete au MST
                mst.addEdge(minEdge.from, minEdge.to, minEdge.weight);
                inMst.add(newVertex);
                
                // Ajouter toutes les aretes partant du nouveau sommet
                for (Vertex neighbor : graph.getNeighbors(newVertex)) {
                    if (!inMst.contains(neighbor)) {
                        double weight = graph.getWeight(newVertex, neighbor);
                        edgeQueue.add(new EdgeWithWeight(newVertex, neighbor, weight));
                    }
                }
            }
        }
        
        if (inMst.size() < graph.getVertexCount()) {
            throw new IllegalStateException("Le graphe n'est pas connecte");
        }
        
        return mst;
    }

    /**
     * Calcule le poids total d'un MST.
     */
    public static double calculateMstWeight(UndirectedGraph mst) {
        double totalWeight = 0.0;
        Set<Edge> countedEdges = new HashSet<>();
        
        for (Vertex v : mst.getVertices()) {
            for (Edge e : mst.getEdges(v)) {
                if (!countedEdges.contains(e)) {
                    totalWeight += e.getWeight();
                    countedEdges.add(e);
                }
            }
        }
        
        return totalWeight;
    }
}

