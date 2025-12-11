package graph;

import java.util.*;

/**
 * Représente un graphe non orienté pondéré.
 * Les arêtes peuvent être parcourues dans les deux sens.
 */
public class UndirectedGraph implements Graph {
    private final Map<String, Vertex> vertices;
    private final Map<Vertex, List<Edge>> adjacencyList;
    private int edgeCount;

    public UndirectedGraph() {
        this.vertices = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.edgeCount = 0;
    }

    /**
     * Ajoute un sommet au graphe.
     */
    @Override
    public void addVertex(Vertex vertex) {
        if (!vertices.containsKey(vertex.getId())) {
            vertices.put(vertex.getId(), vertex);
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }

    /**
     * Ajoute une arête entre deux sommets.
     * Si les sommets n'existent pas, ils sont créés automatiquement.
     */
    public void addEdge(Vertex from, Vertex to, double weight) {
        addVertex(from);
        addVertex(to);
        
        Edge edge = new Edge(from, to, weight);
        adjacencyList.get(from).add(edge);
        adjacencyList.get(to).add(edge);
        edgeCount++;
    }

    /**
     * Retourne toutes les arêtes incidentes à un sommet.
     */
    public List<Edge> getEdges(Vertex vertex) {
        return new ArrayList<>(adjacencyList.getOrDefault(vertex, new ArrayList<>()));
    }

    /**
     * Retourne toutes les arêtes du graphe.
     */
    public List<Edge> getAllEdges() {
        Set<Edge> edgeSet = new HashSet<>();
        for (List<Edge> edges : adjacencyList.values()) {
            edgeSet.addAll(edges);
        }
        return new ArrayList<>(edgeSet);
    }

    /**
     * Retourne le poids de l'arête entre deux sommets, ou Double.POSITIVE_INFINITY s'il n'y en a pas.
     */
    public double getWeight(Vertex from, Vertex to) {
        List<Edge> edges = adjacencyList.get(from);
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.getOther(from).equals(to)) {
                    return edge.getWeight();
                }
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Set<Vertex> getVertices() {
        return new HashSet<>(vertices.values());
    }

    @Override
    public Vertex getVertex(String id) {
        return vertices.get(id);
    }

    @Override
    public int getVertexCount() {
        return vertices.size();
    }

    @Override
    public int getEdgeCount() {
        return edgeCount;
    }

    /**
     * Retourne le degré d'un sommet (nombre d'arêtes incidentes).
     */
    @Override
    public int getDegree(Vertex vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>()).size();
    }

    @Override
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    /**
     * Retourne tous les voisins d'un sommet.
     */
    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<>();
        List<Edge> edges = adjacencyList.get(vertex);
        if (edges != null) {
            for (Edge edge : edges) {
                neighbors.add(edge.getOther(vertex));
            }
        }
        return neighbors;
    }

    /**
     * Retourne une représentation textuelle du graphe pour le débogage.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graphe non orienté : ").append(getVertexCount()).append(" sommets, ")
          .append(getEdgeCount()).append(" arêtes\n");
        for (Vertex v : vertices.values()) {
            sb.append(v).append(" : ");
            List<Vertex> neighbors = getNeighbors(v);
            for (int i = 0; i < neighbors.size(); i++) {
                if (i > 0) sb.append(", ");
                Vertex neighbor = neighbors.get(i);
                sb.append(neighbor.getId()).append("(")
                  .append(getWeight(v, neighbor)).append(")");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

