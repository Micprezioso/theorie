package graph;

/**
 * Représente une arête dans un graphe non orienté.
 * Une arête relie deux sommets avec un poids (distance).
 */
public class Edge {
    private final Vertex from;
    private final Vertex to;
    private final double weight;

    public Edge(Vertex from, Vertex to, double weight) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Les sommets ne peuvent pas être null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Le poids doit être positif ou nul");
        }
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    /**
     * Vérifie si cette arête connecte le sommet donné.
     * Pour une arête non orientée, l'ordre n'a pas d'importance.
     */
    public boolean connects(Vertex vertex) {
        return from.equals(vertex) || to.equals(vertex);
    }

    /**
     * Retourne l'autre sommet de l'arête si l'un est donné.
     */
    public Vertex getOther(Vertex vertex) {
        if (from.equals(vertex)) {
            return to;
        } else if (to.equals(vertex)) {
            return from;
        }
        throw new IllegalArgumentException("Le sommet donné n'est pas dans cette arête");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        // Pour une arête non orientée, l'ordre n'a pas d'importance
        return (from.equals(edge.from) && to.equals(edge.to)) ||
               (from.equals(edge.to) && to.equals(edge.from));
    }

    @Override
    public int hashCode() {
        // Hash symétrique pour les arêtes non orientées
        return from.hashCode() + to.hashCode();
    }

    @Override
    public String toString() {
        return from.getId() + " --" + weight + "--> " + to.getId();
    }
}

