package modele;

/**
 * Représente une demande de collecte d'encombrant.
 */
public class DemandeCollecte {
    private String id;
    private Sommet adresse;
    private TypeCollecte type;
    
    /**
     * Constructeur d'une demande de collecte.
     * @param id Identifiant unique de la demande
     * @param adresse Adresse (sommet) où se trouve le déchet
     * @param type Type de collecte (ENCOMBRANT, POUBELLE)
     */
    public DemandeCollecte(String id, Sommet adresse, TypeCollecte type) {
        this.id = id;
        this.adresse = adresse;
        this.type = type;
    }
    
    public String getId() {
        return id;
    }
    
    public Sommet getAdresse() {
        return adresse;
    }
    
    public TypeCollecte getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "Demande " + id + " (" + type + ") à " + adresse;
    }
}

