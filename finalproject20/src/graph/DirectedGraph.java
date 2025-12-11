package graph;

import java.util.*;

/**
 * Représente un graphe orienté pondéré.
 * Les arcs ont une direction : de 'from' vers 'to'.
 */
public class DirectedGraph implements Graph {
    private final Map<String, Vertex> vertices;
    private final Map<Vertex, List<DirectedEdge>> outgoingEdges;
    private final Map<Vertex, List<DirectedEdge>> incomingEdges;
    private int edgeCount;

    public DirectedGraph() {
        this.vertices = new HashMap<>();
        this.outgoingEdges = new HashMap<>();
        this.incomingEdges = new HashMap<>();
        this.edgeCount = 0;
    }

    /**
     * Ajoute un sommet au graphe.
     */
    @Override
    public void addVertex(Vertex vertex) {
        if (!vertices.containsKey(vertex.getId())) {
            vertices.put(vertex.getId(), vertex);
            outgoingEdges.put(vertex, new ArrayList<>());
            incomingEdges.put(vertex, new ArrayList<>());
        }
    }

    /**
     * Ajoute un arc du sommet 'from' vers le sommet 'to'.
     * Si les sommets n'existent pas, ils sont créés automatiquement.
     */
    public void addEdge(Vertex from, Vertex to, double weight) {
        addVertex(from);
        addVertex(to);
        
        DirectedEdge edge = new DirectedEdge(from, to, weight);
        outgoingEdges.get(from).add(edge);
        incomingEdges.get(to).add(edge);
        edgeCount++;
    }

    /**
     * Retourne tous les arcs sortants d'un sommet.
     */
    public List<DirectedEdge> getOutgoingEdges(Vertex vertex) {
        return new ArrayList<>(outgoingEdges.getOrDefault(vertex, new ArrayList<>()));
    }

    /**
     * Retourne tous les arcs entrants d'un sommet.
     */
    public List<DirectedEdge> getIncomingEdges(Vertex vertex) {
        return new ArrayList<>(incomingEdges.getOrDefault(vertex, new ArrayList<>()));
    }

    /**
     * Retourne tous les arcs du graphe.
     */
    public List<DirectedEdge> getAllEdges() {
        List<DirectedEdge> allEdges = new ArrayList<>();
        for (List<DirectedEdge> edges : outgoingEdges.values()) {
            allEdges.addAll(edges);
        }
        return allEdges;
    }

    /**
     * Retourne le poids de l'arc de 'from' vers 'to', ou Double.POSITIVE_INFINITY s'il n'existe pas.
     */
    public double getWeight(Vertex from, Vertex to) {
        List<DirectedEdge> edges = outgoingEdges.get(from);
        if (edges != null) {
            for (DirectedEdge edge : edges) {
                if (edge.getTo().equals(to)) {
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
     * Retourne le degré total d'un sommet (entrant + sortant).
     * Pour un graphe orienté, on peut aussi utiliser getInDegree() et getOutDegree().
     */
    @Override
    public int getDegree(Vertex vertex) {
        return getInDegree(vertex) + getOutDegree(vertex);
    }

    /**
     * Retourne le degré entrant (nombre d'arcs arrivant au sommet).
     */
    public int getInDegree(Vertex vertex) {
        return incomingEdges.getOrDefault(vertex, new ArrayList<>()).size();
    }

    /**
     * Retourne le degré sortant (nombre d'arcs partant du sommet).
     */
    public int getOutDegree(Vertex vertex) {
        return outgoingEdges.getOrDefault(vertex, new ArrayList<>()).size();
    }

    @Override
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    /**
     * Retourne les successeurs d'un sommet (voisins accessibles via les arcs sortants).
     */
    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<>();
        List<DirectedEdge> edges = outgoingEdges.get(vertex);
        if (edges != null) {
            for (DirectedEdge edge : edges) {
                neighbors.add(edge.getTo());
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
        sb.append("Graphe orienté : ").append(getVertexCount()).append(" sommets, ")
          .append(getEdgeCount()).append(" arcs\n");
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

