import modele.*;
import algorithme.*;
import test.GraphesTest;

import java.util.*;

/**
 * Point d'entrée du programme pour tester les fonctionnalités du Thème 1.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== PROJET THÉORIE DES GRAPHES - THÈME 1 ===\n");
        
        // Test 1 : Plus court chemin pour un encombrant (P1.1)
        System.out.println("--- Test 1 : Plus court chemin pour un encombrant ---");
        testPlusCourtChemin();
        
        // Test 2 : Tournée d'encombrants (P1.2)
        System.out.println("\n--- Test 2 : Tournée d'encombrants ---");
        testTourneeEncombrants();
        
        // Test 3 : Circuit eulérien (P2.1)
        System.out.println("\n--- Test 3 : Circuit eulérien (cas idéal) ---");
        testCircuitEulerien();
        
        // Test 4 : Chemin eulérien (P2.2)
        System.out.println("\n--- Test 4 : Chemin eulérien (2 sommets impairs) ---");
        testCheminEulerien();
        
        // Test 5 : Postier chinois (P2.3)
        System.out.println("\n--- Test 5 : Postier chinois (cas général) ---");
        testPostierChinois();
    }
    
    /**
     * Test du plus court chemin pour un seul encombrant.
     */
    private static void testPlusCourtChemin() {
        Graphe graphe = GraphesTest.creerGraphe1();
        Sommet centre = graphe.getSommet("C");
        Sommet domicile = graphe.getSommet("D");
        
        AlgorithmePlusCourtChemin algo = new AlgorithmePlusCourtChemin(graphe);
        List<Sommet> chemin = algo.calculer(centre, domicile);
        
        if (chemin != null) {
            System.out.println("Chemin de " + centre + " vers " + domicile + " :");
            for (int i = 0; i < chemin.size(); i++) {
                System.out.print(chemin.get(i));
                if (i < chemin.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            double distance = algo.calculerDistance(centre, domicile);
            System.out.println("\nDistance totale : " + String.format("%.2f", distance) + " m");
        } else {
            System.out.println("Aucun chemin trouvé");
        }
    }
    
    /**
     * Test d'une tournée d'encombrants.
     */
    private static void testTourneeEncombrants() {
        Graphe graphe = GraphesTest.creerGraphe2();
        Sommet centre = graphe.getSommet("C");
        
        List<Sommet> domiciles = Arrays.asList(
            graphe.getSommet("A"),
            graphe.getSommet("B"),
            graphe.getSommet("D")
        );
        
        System.out.println("Domiciles à visiter : " + domiciles);
        
        AlgorithmeTournee algoTournee = new AlgorithmeTournee(graphe, centre);
        
        // Test avec heuristique du plus proche voisin
        System.out.println("\nTournée (plus proche voisin) :");
        Tournee tourneeHeuristique = algoTournee.tourneePlusProcheVoisin(domiciles);
        tourneeHeuristique.afficher();
        
        // Test avec force brute (pour petit nombre de domiciles)
        System.out.println("\nTournée (force brute - solution optimale) :");
        Tournee tourneeOptimale = algoTournee.tourneeForceBrute(domiciles);
        tourneeOptimale.afficher();
    }
    
    /**
     * Test du circuit eulérien (tous sommets de degré pair).
     */
    private static void testCircuitEulerien() {
        Graphe graphe = GraphesTest.creerGraphe3();
        Sommet centre = graphe.getSommet("C");
        
        System.out.println("Le graphe est eulérien : " + graphe.estEulerien());
        System.out.println("Sommets impairs : " + graphe.getSommetsImpairs().size());
        
        AlgorithmeEulerien algoEulerien = new AlgorithmeEulerien(graphe);
        List<Sommet> circuit = algoEulerien.circuitEulerien(centre);
        
        if (!circuit.isEmpty()) {
            System.out.println("Circuit eulérien trouvé (" + circuit.size() + " sommets) :");
            for (int i = 0; i < circuit.size(); i++) {
                System.out.print(circuit.get(i));
                if (i < circuit.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
            
            // Calculer la distance totale
            double distanceTotale = 0.0;
            for (int i = 0; i < circuit.size() - 1; i++) {
                distanceTotale += graphe.getDistance(circuit.get(i), circuit.get(i + 1));
            }
            System.out.println("Distance totale : " + String.format("%.2f", distanceTotale) + " m");
        } else {
            System.out.println("Aucun circuit eulérien trouvé");
        }
    }
    
    /**
     * Test du chemin eulérien (2 sommets impairs).
     */
    private static void testCheminEulerien() {
        Graphe graphe = GraphesTest.creerGraphe4();
        Sommet centre = graphe.getSommet("C");
        
        List<Sommet> sommetsImpairs = graphe.getSommetsImpairs();
        System.out.println("Sommets impairs : " + sommetsImpairs);
        System.out.println("Nombre de sommets impairs : " + sommetsImpairs.size());
        
        if (sommetsImpairs.size() == 2) {
            Sommet depart = sommetsImpairs.contains(centre) ? centre : sommetsImpairs.get(0);
            Sommet arrivee = sommetsImpairs.get(0).equals(depart) ? sommetsImpairs.get(1) : sommetsImpairs.get(0);
            
            AlgorithmeEulerien algoEulerien = new AlgorithmeEulerien(graphe);
            List<Sommet> chemin = algoEulerien.cheminEulerien(depart, arrivee);
            
            if (!chemin.isEmpty()) {
                System.out.println("Chemin eulérien trouvé (" + chemin.size() + " sommets) :");
                for (int i = 0; i < chemin.size(); i++) {
                    System.out.print(chemin.get(i));
                    if (i < chemin.size() - 1) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
                
                // Si le chemin ne se termine pas au centre, ajouter le retour
                if (!chemin.get(chemin.size() - 1).equals(centre)) {
                    AlgorithmePlusCourtChemin algoChemin = new AlgorithmePlusCourtChemin(graphe);
                    List<Sommet> retour = algoChemin.calculer(chemin.get(chemin.size() - 1), centre);
                    if (retour != null && retour.size() > 1) {
                        System.out.print(" + retour au centre : ");
                        for (int i = 1; i < retour.size(); i++) {
                            System.out.print(retour.get(i));
                            if (i < retour.size() - 1) {
                                System.out.print(" -> ");
                            }
                        }
                        System.out.println();
                    }
                }
            } else {
                System.out.println("Aucun chemin eulérien trouvé");
            }
        }
    }
    
    /**
     * Test du postier chinois (cas général).
     */
    private static void testPostierChinois() {
        Graphe graphe = GraphesTest.creerGraphe5();
        Sommet centre = graphe.getSommet("C");
        
        List<Sommet> sommetsImpairs = graphe.getSommetsImpairs();
        System.out.println("Sommets impairs : " + sommetsImpairs);
        System.out.println("Nombre de sommets impairs : " + sommetsImpairs.size());
        
        AlgorithmeEulerien algoEulerien = new AlgorithmeEulerien(graphe);
        List<Sommet> tournee = algoEulerien.postierChinois(centre);
        
        if (!tournee.isEmpty()) {
            System.out.println("Tournée du postier chinois trouvée (" + tournee.size() + " sommets) :");
            for (int i = 0; i < tournee.size(); i++) {
                System.out.print(tournee.get(i));
                if (i < tournee.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
            
            // Calculer la distance totale
            double distanceTotale = 0.0;
            for (int i = 0; i < tournee.size() - 1; i++) {
                double dist = graphe.getDistance(tournee.get(i), tournee.get(i + 1));
                if (dist > 0) {
                    distanceTotale += dist;
                }
            }
            System.out.println("Distance totale : " + String.format("%.2f", distanceTotale) + " m");
        } else {
            System.out.println("Aucune tournée trouvée");
        }
    }
}
