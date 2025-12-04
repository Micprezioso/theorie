package modele;

import java.util.*;

/**
 * Représente une tournée de collecte (séquence de sommets à visiter).
 */
public class Tournee {
    private List<Sommet> sommets;
    private double distanceTotale;
    private Graphe graphe;
    
    /**
     * Constructeur d'une tournée vide.
     */
    public Tournee(Graphe graphe) {
        this.graphe = graphe;
        this.sommets = new ArrayList<>();
        this.distanceTotale = 0.0;
    }
    
    /**
     * Ajoute un sommet à la tournée et met à jour la distance totale.
     */
    public void ajouterSommet(Sommet sommet) {
        if (!sommets.isEmpty()) {
            Sommet dernier = sommets.get(sommets.size() - 1);
            double distance = graphe.getDistance(dernier, sommet);
            if (distance > 0) {
                distanceTotale += distance;
            }
        }
        sommets.add(sommet);
    }
    
    /**
     * Ajoute une liste de sommets à la tournée.
     */
    public void ajouterSommets(List<Sommet> nouveauxSommets) {
        for (Sommet sommet : nouveauxSommets) {
            ajouterSommet(sommet);
        }
    }
    
    /**
     * Retourne la distance totale de la tournée.
     */
    public double getDistanceTotale() {
        return distanceTotale;
    }
    
    /**
     * Retourne la liste des sommets de la tournée.
     */
    public List<Sommet> getSommets() {
        return new ArrayList<>(sommets);
    }
    
    /**
     * Affiche la tournée.
     */
    public void afficher() {
        System.out.println("Tournée (" + String.format("%.2f", distanceTotale) + " m) :");
        for (int i = 0; i < sommets.size(); i++) {
            System.out.print(sommets.get(i).getId());
            if (i < sommets.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tournée (").append(String.format("%.2f", distanceTotale)).append(" m): ");
        for (int i = 0; i < sommets.size(); i++) {
            sb.append(sommets.get(i).getId());
            if (i < sommets.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
}

