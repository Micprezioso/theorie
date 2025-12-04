package test;

import modele.*;

/**
 * Classe utilitaire pour créer des graphes de test adaptés au Thème 1.
 */
public class GraphesTest {
    
    /**
     * Crée un graphe simple pour tester le plus court chemin (P1.1).
     * Graphe 1 : Plus court chemin simple
     */
    public static Graphe creerGraphe1() {
        Graphe graphe = new Graphe(TypeGraphe.NON_ORIENTE);
        
        Sommet c = new Sommet("C", TypeSommet.CENTRE_TRAITEMENT);
        Sommet a = new Sommet("A", TypeSommet.INTERSECTION);
        Sommet b = new Sommet("B", TypeSommet.INTERSECTION);
        Sommet d = new Sommet("D", TypeSommet.INTERSECTION);
        Sommet e = new Sommet("E", TypeSommet.INTERSECTION);
        
        graphe.ajouterSommet(c);
        graphe.ajouterSommet(a);
        graphe.ajouterSommet(b);
        graphe.ajouterSommet(d);
        graphe.ajouterSommet(e);
        
        graphe.ajouterArete(new Arete(c, a, 100, "Rue C-A"));
        graphe.ajouterArete(new Arete(c, b, 150, "Rue C-B"));
        graphe.ajouterArete(new Arete(a, b, 80, "Rue A-B"));
        graphe.ajouterArete(new Arete(a, d, 120, "Rue A-D"));
        graphe.ajouterArete(new Arete(b, e, 90, "Rue B-E"));
        graphe.ajouterArete(new Arete(d, e, 110, "Rue D-E"));
        
        return graphe;
    }
    
    /**
     * Crée un graphe pour tester une tournée de 3 encombrants (P1.2).
     * Graphe 2 : Tournée TSP
     */
    public static Graphe creerGraphe2() {
        Graphe graphe = new Graphe(TypeGraphe.NON_ORIENTE);
        
        Sommet c = new Sommet("C", TypeSommet.CENTRE_TRAITEMENT);
        Sommet a = new Sommet("A", TypeSommet.INTERSECTION);
        Sommet b = new Sommet("B", TypeSommet.INTERSECTION);
        Sommet d = new Sommet("D", TypeSommet.INTERSECTION);
        
        graphe.ajouterSommet(c);
        graphe.ajouterSommet(a);
        graphe.ajouterSommet(b);
        graphe.ajouterSommet(d);
        
        graphe.ajouterArete(new Arete(c, a, 50, "Rue C-A"));
        graphe.ajouterArete(new Arete(c, b, 60, "Rue C-B"));
        graphe.ajouterArete(new Arete(c, d, 70, "Rue C-D"));
        graphe.ajouterArete(new Arete(a, b, 30, "Rue A-B"));
        graphe.ajouterArete(new Arete(a, d, 40, "Rue A-D"));
        graphe.ajouterArete(new Arete(b, d, 35, "Rue B-D"));
        
        return graphe;
    }
    
    /**
     * Crée un graphe eulérien pour tester le circuit eulérien (P2.1).
     * Graphe 3 : Circuit eulérien (tous sommets de degré pair)
     */
    public static Graphe creerGraphe3() {
        Graphe graphe = new Graphe(TypeGraphe.NON_ORIENTE);
        
        Sommet c = new Sommet("C", TypeSommet.CENTRE_TRAITEMENT);
        Sommet a = new Sommet("A", TypeSommet.INTERSECTION);
        Sommet b = new Sommet("B", TypeSommet.INTERSECTION);
        Sommet d = new Sommet("D", TypeSommet.INTERSECTION);
        Sommet e = new Sommet("E", TypeSommet.INTERSECTION);
        
        graphe.ajouterSommet(c);
        graphe.ajouterSommet(a);
        graphe.ajouterSommet(b);
        graphe.ajouterSommet(d);
        graphe.ajouterSommet(e);
        
        graphe.ajouterArete(new Arete(c, a, 100, "Rue C-A"));
        graphe.ajouterArete(new Arete(a, b, 80, "Rue A-B"));
        graphe.ajouterArete(new Arete(b, c, 120, "Rue B-C"));
        graphe.ajouterArete(new Arete(c, d, 90, "Rue C-D"));
        graphe.ajouterArete(new Arete(d, e, 70, "Rue D-E"));
        graphe.ajouterArete(new Arete(e, c, 110, "Rue E-C"));
        
        return graphe;
    }
    
    /**
     * Crée un graphe avec 2 sommets impairs pour tester le chemin eulérien (P2.2).
     * Graphe 4 : Chemin eulérien
     */
    public static Graphe creerGraphe4() {
        Graphe graphe = new Graphe(TypeGraphe.NON_ORIENTE);
        
        Sommet c = new Sommet("C", TypeSommet.CENTRE_TRAITEMENT);
        Sommet a = new Sommet("A", TypeSommet.INTERSECTION);
        Sommet b = new Sommet("B", TypeSommet.INTERSECTION);
        Sommet d = new Sommet("D", TypeSommet.INTERSECTION);
        Sommet e = new Sommet("E", TypeSommet.INTERSECTION);
        
        graphe.ajouterSommet(c);
        graphe.ajouterSommet(a);
        graphe.ajouterSommet(b);
        graphe.ajouterSommet(d);
        graphe.ajouterSommet(e);
        
        graphe.ajouterArete(new Arete(c, a, 100, "Rue C-A"));
        graphe.ajouterArete(new Arete(c, b, 80, "Rue C-B"));
        graphe.ajouterArete(new Arete(c, d, 90, "Rue C-D"));
        graphe.ajouterArete(new Arete(b, e, 70, "Rue B-E"));
        graphe.ajouterArete(new Arete(e, d, 60, "Rue E-D"));
        
        return graphe;
    }
    
    /**
     * Crée un graphe pour tester le postier chinois (P2.3).
     * Graphe 5 : Postier chinois (plusieurs sommets impairs)
     */
    public static Graphe creerGraphe5() {
        Graphe graphe = new Graphe(TypeGraphe.NON_ORIENTE);
        
        Sommet c = new Sommet("C", TypeSommet.CENTRE_TRAITEMENT);
        Sommet a = new Sommet("A", TypeSommet.INTERSECTION);
        Sommet b = new Sommet("B", TypeSommet.INTERSECTION);
        Sommet d = new Sommet("D", TypeSommet.INTERSECTION);
        Sommet e = new Sommet("E", TypeSommet.INTERSECTION);
        
        graphe.ajouterSommet(c);
        graphe.ajouterSommet(a);
        graphe.ajouterSommet(b);
        graphe.ajouterSommet(d);
        graphe.ajouterSommet(e);
        
        graphe.ajouterArete(new Arete(c, a, 50, "Rue C-A"));
        graphe.ajouterArete(new Arete(c, b, 60, "Rue C-B"));
        graphe.ajouterArete(new Arete(a, d, 40, "Rue A-D"));
        graphe.ajouterArete(new Arete(a, e, 30, "Rue A-E"));
        graphe.ajouterArete(new Arete(d, e, 20, "Rue D-E"));
        
        return graphe;
    }
    
    /**
     * Crée un graphe orienté simple pour tester HO2.
     * Graphe 6 : Graphe orienté
     */
    public static Graphe creerGraphe6() {
        Graphe graphe = new Graphe(TypeGraphe.ORIENTE);
        
        Sommet c = new Sommet("C", TypeSommet.CENTRE_TRAITEMENT);
        Sommet a = new Sommet("A", TypeSommet.INTERSECTION);
        Sommet b = new Sommet("B", TypeSommet.INTERSECTION);
        Sommet d = new Sommet("D", TypeSommet.INTERSECTION);
        
        graphe.ajouterSommet(c);
        graphe.ajouterSommet(a);
        graphe.ajouterSommet(b);
        graphe.ajouterSommet(d);
        
        graphe.ajouterArc(new Arc(c, a, 100, "Rue C->A"));
        graphe.ajouterArc(new Arc(a, b, 80, "Rue A->B"));
        graphe.ajouterArc(new Arc(b, c, 120, "Rue B->C"));
        graphe.ajouterArc(new Arc(c, d, 90, "Rue C->D"));
        graphe.ajouterArc(new Arc(d, a, 70, "Rue D->A"));
        
        return graphe;
    }
}

