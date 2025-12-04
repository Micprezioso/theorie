package algorithme;

import modele.*;

import java.util.*;

/**
 * Implémente l'algorithme de Dijkstra pour trouver le plus court chemin.
 */
public class AlgorithmePlusCourtChemin {
    private Graphe graphe;
    
    /**
     * Constructeur.
     * @param graphe Le graphe sur lequel appliquer l'algorithme
     */
    public AlgorithmePlusCourtChemin(Graphe graphe) {
        this.graphe = graphe;
    }
    
    /**
     * Calcule le plus court chemin entre deux sommets.
     * @param source Sommet de départ
     * @param destination Sommet d'arrivée
     * @return Liste des sommets formant le chemin, ou null si aucun chemin n'existe
     */
    public List<Sommet> calculer(Sommet source, Sommet destination) {
        Map<Sommet, Double> distances = new HashMap<>();
        Map<Sommet, Sommet> predecesseurs = new HashMap<>();
        Set<Sommet> nonVisites = new HashSet<>();
        
        // Initialisation
        for (Sommet sommet : graphe.getTousSommets()) {
            distances.put(sommet, Double.MAX_VALUE);
            nonVisites.add(sommet);
        }
        distances.put(source, 0.0);
        
        // Algorithme de Dijkstra
        while (!nonVisites.isEmpty()) {
            Sommet u = trouverSommetMinimal(nonVisites, distances);
            if (u == null) {
                break; // Plus de sommets accessibles
            }
            nonVisites.remove(u);
            
            if (u.equals(destination)) {
                return reconstruireChemin(predecesseurs, source, destination);
            }
            
            for (Sommet voisin : graphe.getVoisins(u)) {
                if (nonVisites.contains(voisin)) {
                    double distance = graphe.getDistance(u, voisin);
                    if (distance > 0) {
                        double nouvelleDistance = distances.get(u) + distance;
                        if (nouvelleDistance < distances.get(voisin)) {
                            distances.put(voisin, nouvelleDistance);
                            predecesseurs.put(voisin, u);
                        }
                    }
                }
            }
        }
        
        return null; // Aucun chemin trouvé
    }
    
    /**
     * Calcule la distance du plus court chemin entre deux sommets.
     * @param source Sommet de départ
     * @param destination Sommet d'arrivée
     * @return La distance, ou -1 si aucun chemin n'existe
     */
    public double calculerDistance(Sommet source, Sommet destination) {
        List<Sommet> chemin = calculer(source, destination);
        if (chemin == null) {
            return -1;
        }
        double distance = 0.0;
        for (int i = 0; i < chemin.size() - 1; i++) {
            distance += graphe.getDistance(chemin.get(i), chemin.get(i + 1));
        }
        return distance;
    }
    
    /**
     * Trouve le sommet non visité avec la distance minimale.
     */
    private Sommet trouverSommetMinimal(Set<Sommet> nonVisites, Map<Sommet, Double> distances) {
        Sommet minimal = null;
        double distanceMin = Double.MAX_VALUE;
        for (Sommet sommet : nonVisites) {
            double dist = distances.get(sommet);
            if (dist < distanceMin) {
                distanceMin = dist;
                minimal = sommet;
            }
        }
        return minimal;
    }
    
    /**
     * Reconstruit le chemin à partir des prédécesseurs.
     */
    private List<Sommet> reconstruireChemin(Map<Sommet, Sommet> predecesseurs, 
                                           Sommet source, Sommet destination) {
        List<Sommet> chemin = new ArrayList<>();
        Sommet courant = destination;
        while (courant != null) {
            chemin.add(0, courant);
            courant = predecesseurs.get(courant);
        }
        // Vérifier que le chemin commence bien par la source
        if (!chemin.isEmpty() && chemin.get(0).equals(source)) {
            return chemin;
        }
        return null;
    }
}

