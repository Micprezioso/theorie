package modele;

import java.util.*;

/**
 * Représente le réseau routier comme un multigraphe pondéré.
 */
public class Graphe {
    private Map<String, Sommet> sommets;
    private List<Arete> aretes;  // Pour HO1 (graphe non orienté)
    private List<Arc> arcs;      // Pour HO2/HO3 (graphe orienté)
    private TypeGraphe type;
    private Map<Sommet, List<Sommet>> listeAdjacence;
    
    /**
     * Constructeur d'un graphe.
     * @param type Type de graphe (NON_ORIENTE, ORIENTE, MIXTE)
     */
    public Graphe(TypeGraphe type) {
        this.type = type;
        this.sommets = new HashMap<>();
        this.aretes = new ArrayList<>();
        this.arcs = new ArrayList<>();
        this.listeAdjacence = new HashMap<>();
    }
    
    /**
     * Ajoute un sommet au graphe.
     */
    public void ajouterSommet(Sommet sommet) {
        sommets.put(sommet.getId(), sommet);
        listeAdjacence.put(sommet, new ArrayList<>());
    }
    
    /**
     * Ajoute une arête non orientée (pour HO1).
     */
    public void ajouterArete(Arete arete) {
        aretes.add(arete);
        Sommet s1 = arete.getSource();
        Sommet s2 = arete.getDestination();
        listeAdjacence.get(s1).add(s2);
        listeAdjacence.get(s2).add(s1);
    }
    
    /**
     * Ajoute un arc orienté (pour HO2/HO3).
     */
    public void ajouterArc(Arc arc) {
        arcs.add(arc);
        Sommet source = arc.getSource();
        Sommet destination = arc.getDestination();
        listeAdjacence.get(source).add(destination);
    }
    
    /**
     * Retourne les voisins d'un sommet.
     */
    public List<Sommet> getVoisins(Sommet sommet) {
        return new ArrayList<>(listeAdjacence.get(sommet));
    }
    
    /**
     * Retourne la distance entre deux sommets adjacents.
     * Retourne -1 si les sommets ne sont pas adjacents.
     */
    public double getDistance(Sommet s1, Sommet s2) {
        // Chercher dans les arêtes (non orienté)
        for (Arete arete : aretes) {
            if (arete.connecte(s1, s2)) {
                return arete.getDistance();
            }
        }
        // Chercher dans les arcs (orienté)
        for (Arc arc : arcs) {
            if (arc.getSource().equals(s1) && arc.getDestination().equals(s2)) {
                return arc.getDistance();
            }
        }
        return -1;
    }
    
    /**
     * Calcule le degré d'un sommet (nombre de voisins).
     */
    public int getDegre(Sommet sommet) {
        return listeAdjacence.get(sommet).size();
    }
    
    /**
     * Vérifie si le graphe est eulérien (tous les sommets de degré pair).
     */
    public boolean estEulerien() {
        for (Sommet sommet : sommets.values()) {
            if (getDegre(sommet) % 2 != 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retourne la liste des sommets de degré impair.
     */
    public List<Sommet> getSommetsImpairs() {
        List<Sommet> impairs = new ArrayList<>();
        for (Sommet sommet : sommets.values()) {
            if (getDegre(sommet) % 2 != 0) {
                impairs.add(sommet);
            }
        }
        return impairs;
    }
    
    public Collection<Sommet> getTousSommets() {
        return sommets.values();
    }
    
    public Sommet getSommet(String id) {
        return sommets.get(id);
    }
    
    public TypeGraphe getType() {
        return type;
    }
    
    public List<Arete> getAretes() {
        return new ArrayList<>(aretes);
    }
    
    public List<Arc> getArcs() {
        return new ArrayList<>(arcs);
    }
}

