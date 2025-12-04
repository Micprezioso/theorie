package modele;

/**
 * Représente le centre de traitement des déchets.
 */
public class CentreTraitement {
    private Sommet sommet;
    
    /**
     * Constructeur du centre de traitement.
     * @param sommet Le sommet du graphe correspondant au centre
     */
    public CentreTraitement(Sommet sommet) {
        this.sommet = sommet;
    }
    
    public Sommet getSommet() {
        return sommet;
    }
}

