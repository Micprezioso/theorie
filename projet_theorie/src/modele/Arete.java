package modele;

/**
 * Représente une arête non orientée (rue à double sens pour HO1).
 */
public class Arete {
    private Sommet source;
    private Sommet destination;
    private double distance;
    private String nomRue;
    
    /**
     * Constructeur d'une arête.
     * @param source Sommet source
     * @param destination Sommet destination
     * @param distance Distance en mètres
     * @param nomRue Nom de la rue (optionnel)
     */
    public Arete(Sommet source, Sommet destination, double distance, String nomRue) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.nomRue = nomRue;
    }
    
    public Sommet getSource() {
        return source;
    }
    
    public Sommet getDestination() {
        return destination;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public String getNomRue() {
        return nomRue;
    }
    
    /**
     * Vérifie si cette arête connecte deux sommets donnés (dans un sens ou l'autre).
     */
    public boolean connecte(Sommet s1, Sommet s2) {
        return (source.equals(s1) && destination.equals(s2)) ||
               (source.equals(s2) && destination.equals(s1));
    }
    
    @Override
    public String toString() {
        return source + "-" + destination + " (" + distance + "m)";
    }
}

