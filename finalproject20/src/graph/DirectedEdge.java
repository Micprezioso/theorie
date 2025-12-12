package graph;

/**
 * Représente un arc (arête orientée) dans un graphe orienté.
 * Un arc a une direction : de 'from' vers 'to', avec un poids.
 */
public class DirectedEdge {
    private final Vertex from;
    private final Vertex to;
    private final double weight;

    public DirectedEdge(Vertex from, Vertex to, double weight) {
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
     * Vérifie si cet arc part du sommet donné.
     */
    public boolean startsFrom(Vertex vertex) {
        return from.equals(vertex);
    }

    /**
     * Vérifie si cet arc arrive au sommet donné.
     */
    public boolean arrivesTo(Vertex vertex) {
        return to.equals(vertex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DirectedEdge that = (DirectedEdge) obj;
        // Pour un arc orienté, l'ordre est important
        return from.equals(that.from) && to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return from.hashCode() * 31 + to.hashCode();
    }

    @Override
    public String toString() {
        return from.getId() + " -[" + weight + "]-> " + to.getId();
    }
}


