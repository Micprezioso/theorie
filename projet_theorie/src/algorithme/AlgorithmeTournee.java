package algorithme;

import modele.*;

import java.util.*;

/**
 * Implémente les algorithmes de tournée (TSP).
 */
public class AlgorithmeTournee {
    private Graphe graphe;
    private Sommet centre;
    private AlgorithmePlusCourtChemin algoChemin;
    
    /**
     * Constructeur.
     * @param graphe Le graphe
     * @param centre Le centre de traitement (point de départ/arrivée)
     */
    public AlgorithmeTournee(Graphe graphe, Sommet centre) {
        this.graphe = graphe;
        this.centre = centre;
        this.algoChemin = new AlgorithmePlusCourtChemin(graphe);
    }
    
    /**
     * Calcule une tournée en utilisant l'heuristique du plus proche voisin.
     * @param domiciles Liste des domiciles à visiter
     * @return La tournée optimisée
     */
    public Tournee tourneePlusProcheVoisin(List<Sommet> domiciles) {
        Tournee tournee = new Tournee(graphe);
        tournee.ajouterSommet(centre);
        
        List<Sommet> domicilesRestants = new ArrayList<>(domiciles);
        Sommet positionActuelle = centre;
        
        while (!domicilesRestants.isEmpty()) {
            Sommet plusProche = null;
            double distanceMin = Double.MAX_VALUE;
            
            for (Sommet domicile : domicilesRestants) {
                double distance = algoChemin.calculerDistance(positionActuelle, domicile);
                if (distance > 0 && distance < distanceMin) {
                    distanceMin = distance;
                    plusProche = domicile;
                }
            }
            
            if (plusProche != null) {
                // Ajouter le chemin complet jusqu'au plus proche
                List<Sommet> chemin = algoChemin.calculer(positionActuelle, plusProche);
                if (chemin != null && chemin.size() > 1) {
                    // Ajouter tous les sommets du chemin sauf le premier (déjà dans la tournée)
                    for (int i = 1; i < chemin.size(); i++) {
                        tournee.ajouterSommet(chemin.get(i));
                    }
                } else {
                    tournee.ajouterSommet(plusProche);
                }
                domicilesRestants.remove(plusProche);
                positionActuelle = plusProche;
            } else {
                break; // Impossible de continuer
            }
        }
        
        // Retour au centre
        List<Sommet> cheminRetour = algoChemin.calculer(positionActuelle, centre);
        if (cheminRetour != null && cheminRetour.size() > 1) {
            for (int i = 1; i < cheminRetour.size(); i++) {
                tournee.ajouterSommet(cheminRetour.get(i));
            }
        }
        
        return tournee;
    }
    
    /**
     * Calcule une tournée optimale par force brute (pour petit nombre de domiciles).
     * @param domiciles Liste des domiciles à visiter (max 10 recommandé)
     * @return La tournée optimale
     */
    public Tournee tourneeForceBrute(List<Sommet> domiciles) {
        if (domiciles.isEmpty()) {
            Tournee tournee = new Tournee(graphe);
            tournee.ajouterSommet(centre);
            tournee.ajouterSommet(centre);
            return tournee;
        }
        
        if (domiciles.size() > 10) {
            System.out.println("Attention : force brute limitée à 10 domiciles, utilisation de l'heuristique");
            return tourneePlusProcheVoisin(domiciles);
        }
        
        Tournee meilleureTournee = null;
        double distanceMinimale = Double.MAX_VALUE;
        
        // Générer toutes les permutations
        List<List<Sommet>> permutations = genererPermutations(domiciles);
        
        for (List<Sommet> permutation : permutations) {
            // Calculer la distance totale pour cette permutation
            double distance = 0.0;
            
            // Distance du centre au premier domicile
            distance += algoChemin.calculerDistance(centre, permutation.get(0));
            
            // Distances entre les domiciles
            for (int i = 0; i < permutation.size() - 1; i++) {
                distance += algoChemin.calculerDistance(permutation.get(i), permutation.get(i + 1));
            }
            
            // Distance du dernier domicile au centre
            distance += algoChemin.calculerDistance(permutation.get(permutation.size() - 1), centre);
            
            if (distance < distanceMinimale && distance > 0) {
                distanceMinimale = distance;
                // Construire la tournée
                meilleureTournee = new Tournee(graphe);
                meilleureTournee.ajouterSommet(centre);
                meilleureTournee.ajouterSommets(permutation);
                meilleureTournee.ajouterSommet(centre);
            }
        }
        
        return meilleureTournee != null ? meilleureTournee : tourneePlusProcheVoisin(domiciles);
    }
    
    /**
     * Génère toutes les permutations d'une liste.
     */
    private List<List<Sommet>> genererPermutations(List<Sommet> liste) {
        List<List<Sommet>> resultat = new ArrayList<>();
        genererPermutationsRecursif(liste, 0, resultat);
        return resultat;
    }
    
    private void genererPermutationsRecursif(List<Sommet> liste, int debut, List<List<Sommet>> resultat) {
        if (debut == liste.size() - 1) {
            resultat.add(new ArrayList<>(liste));
            return;
        }
        
        for (int i = debut; i < liste.size(); i++) {
            Collections.swap(liste, debut, i);
            genererPermutationsRecursif(liste, debut + 1, resultat);
            Collections.swap(liste, debut, i);
        }
    }
}

