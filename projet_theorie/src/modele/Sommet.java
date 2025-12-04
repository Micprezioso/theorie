package modele;

/**
 * Représente un sommet du graphe (intersection ou carrefour).
 */
public class Sommet {
    private String id;
    private TypeSommet type;
    
    /**
     * Constructeur d'un sommet.
     * @param id Identifiant unique du sommet
     * @param type Type du sommet (INTERSECTION, CENTRE_TRAITEMENT, etc.)
     */
    public Sommet(String id, TypeSommet type) {
        this.id = id;
        this.type = type;
    }
    
    public String getId() {
        return id;
    }
    
    public TypeSommet getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sommet sommet = (Sommet) obj;
        return id.equals(sommet.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return id;
    }
}

