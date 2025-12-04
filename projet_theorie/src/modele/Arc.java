package modele;

/**
 * Représente un arc orienté (rue à sens unique pour HO2/HO3).
 */
public class Arc {
    private Sommet source;
    private Sommet destination;
    private double distance;
    private String nomRue;
    
    /**
     * Constructeur d'un arc.
     * @param source Sommet source
     * @param destination Sommet destination
     * @param distance Distance en mètres
     * @param nomRue Nom de la rue (optionnel)
     */
    public Arc(Sommet source, Sommet destination, double distance, String nomRue) {
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
    
    @Override
    public String toString() {
        return source + "->" + destination + " (" + distance + "m)";
    }
}

