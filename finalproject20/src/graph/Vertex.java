package graph;

/**
 * Représente un sommet (nœud) dans un graphe.
 * Chaque sommet a un identifiant unique et un nom optionnel pour l'affichage.
 */
public class Vertex {
    private final String id;
    private final String name;

    public Vertex(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertex vertex = (Vertex) obj;
        return id.equals(vertex.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return name != null && !name.isEmpty() ? name + " (" + id + ")" : id;
    }
}

