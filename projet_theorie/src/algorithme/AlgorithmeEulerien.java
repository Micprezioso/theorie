package algorithme;

import modele.*;

import java.util.*;

/**
 * Implémente les algorithmes eulériens (Hierholzer, postier chinois).
 */
public class AlgorithmeEulerien {
    private Graphe graphe;
    private AlgorithmePlusCourtChemin algoChemin;
    
    /**
     * Constructeur.
     * @param graphe Le graphe (doit être non orienté pour HO1)
     */
    public AlgorithmeEulerien(Graphe graphe) {
        this.graphe = graphe;
        this.algoChemin = new AlgorithmePlusCourtChemin(graphe);
    }
    
    /**
     * Calcule un circuit eulérien (tous les sommets de degré pair).
     * Utilise l'algorithme de Hierholzer.
     * @param depart Sommet de départ
     * @return Liste des sommets formant le circuit eulérien
     */
    public List<Sommet> circuitEulerien(Sommet depart) {
        if (!graphe.estEulerien()) {
            System.out.println("Le graphe n'est pas eulérien (tous les sommets doivent avoir un degré pair)");
            return new ArrayList<>();
        }
        
        // Créer une copie des arêtes pour marquer celles utilisées
        Map<Arete, Boolean> aretesUtilisees = new HashMap<>();
        for (Arete arete : graphe.getAretes()) {
            aretesUtilisees.put(arete, false);
        }
        
        // Construire le circuit initial
        List<Sommet> circuit = construireCircuit(depart, aretesUtilisees);
        
        // Algorithme de Hierholzer : fusionner les circuits supplémentaires
        int position = 0;
        while (position < circuit.size()) {
            Sommet sommetCourant = circuit.get(position);
            
            // Chercher une arête non utilisée partant de ce sommet
            Arete areteNonUtilisee = trouverAreteNonUtilisee(sommetCourant, aretesUtilisees);
            
            if (areteNonUtilisee != null) {
                // Construire un nouveau circuit partant de ce sommet
                List<Sommet> nouveauCircuit = construireCircuit(sommetCourant, aretesUtilisees);
                
                // Insérer le nouveau circuit dans le circuit principal (remplacer le sommet courant)
                circuit.remove(position);
                for (int i = 0; i < nouveauCircuit.size(); i++) {
                    circuit.add(position + i, nouveauCircuit.get(i));
                }
                position = 0; // Recommencer depuis le début
            } else {
                position++;
            }
        }
        
        // Ajouter le sommet de départ à la fin pour fermer le circuit
        if (!circuit.isEmpty() && !circuit.get(circuit.size() - 1).equals(depart)) {
            circuit.add(depart);
        }
        
        return circuit;
    }
    
    /**
     * Construit un circuit partant d'un sommet donné.
     */
    private List<Sommet> construireCircuit(Sommet depart, Map<Arete, Boolean> aretesUtilisees) {
        List<Sommet> circuit = new ArrayList<>();
        circuit.add(depart);
        Sommet sommetActuel = depart;
        
        while (true) {
            Arete arete = trouverAreteNonUtilisee(sommetActuel, aretesUtilisees);
            if (arete == null) {
                break;
            }
            
            aretesUtilisees.put(arete, true);
            Sommet suivant = arete.getSource().equals(sommetActuel) ? 
                            arete.getDestination() : arete.getSource();
            circuit.add(suivant);
            sommetActuel = suivant;
        }
        
        return circuit;
    }
    
    /**
     * Trouve une arête non utilisée partant d'un sommet.
     */
    private Arete trouverAreteNonUtilisee(Sommet sommet, Map<Arete, Boolean> aretesUtilisees) {
        for (Arete arete : graphe.getAretes()) {
            if (!aretesUtilisees.get(arete)) {
                // Vérifier que l'arête part bien de ce sommet
                if (arete.getSource().equals(sommet) || arete.getDestination().equals(sommet)) {
                    return arete;
                }
            }
        }
        return null;
    }
    
    /**
     * Calcule un chemin eulérien (exactement 2 sommets de degré impair).
     * @param depart Un des deux sommets impairs
     * @param arrivee L'autre sommet impair
     * @return Liste des sommets formant le chemin eulérien
     */
    public List<Sommet> cheminEulerien(Sommet depart, Sommet arrivee) {
        List<Sommet> sommetsImpairs = graphe.getSommetsImpairs();
        if (sommetsImpairs.size() != 2) {
            System.out.println("Le graphe doit avoir exactement 2 sommets de degré impair");
            return new ArrayList<>();
        }
        
        if (!sommetsImpairs.contains(depart) || !sommetsImpairs.contains(arrivee)) {
            System.out.println("Les sommets de départ et d'arrivée doivent être les sommets impairs");
            return new ArrayList<>();
        }
        
        // Utiliser l'algorithme de circuit eulérien en ajoutant temporairement une arête
        // entre les deux sommets impairs
        Arete areteTemporaire = new Arete(depart, arrivee, 0, "temporaire");
        graphe.ajouterArete(areteTemporaire);
        
        List<Sommet> circuit = circuitEulerien(depart);
        
        // Retirer l'arête temporaire
        graphe.getAretes().remove(areteTemporaire);
        
        // Si le circuit commence et finit par le même sommet, c'est bon
        // Sinon, ajuster pour commencer par depart et finir par arrivee
        if (!circuit.isEmpty() && circuit.get(0).equals(circuit.get(circuit.size() - 1))) {
            // C'est un circuit, on peut le transformer en chemin
            circuit.remove(circuit.size() - 1); // Retirer le dernier (identique au premier)
        }
        
        return circuit;
    }
    
    /**
     * Résout le problème du postier chinois (cas général).
     * Pour l'instant, version simplifiée pour HO1 (graphe non orienté).
     * @param centre Centre de traitement (point de départ/arrivée)
     * @return Liste des sommets formant la tournée optimale
     */
    public List<Sommet> postierChinois(Sommet centre) {
        if (graphe.getType() != TypeGraphe.NON_ORIENTE) {
            System.out.println("Le postier chinois n'est implémenté que pour les graphes non orientés (HO1)");
            return new ArrayList<>();
        }
        
        List<Sommet> sommetsImpairs = graphe.getSommetsImpairs();
        
        if (sommetsImpairs.isEmpty()) {
            // Le graphe est déjà eulérien
            return circuitEulerien(centre);
        }
        
        if (sommetsImpairs.size() % 2 != 0) {
            System.out.println("Le nombre de sommets impairs doit être pair");
            return new ArrayList<>();
        }
        
        if (sommetsImpairs.size() == 2) {
            // Cas simple : chemin eulérien
            Sommet autre = sommetsImpairs.get(0).equals(centre) ? 
                          sommetsImpairs.get(1) : sommetsImpairs.get(0);
            List<Sommet> chemin = cheminEulerien(centre, autre);
            
            // Ajouter le retour au centre
            List<Sommet> cheminRetour = algoChemin.calculer(autre, centre);
            if (cheminRetour != null && cheminRetour.size() > 1) {
                for (int i = 1; i < cheminRetour.size(); i++) {
                    chemin.add(cheminRetour.get(i));
                }
            }
            return chemin;
        }
        
        // Cas général : trouver le couplage parfait de poids minimal
        // Pour simplifier, on utilise une heuristique gloutonne
        return postierChinoisHeuristique(centre, sommetsImpairs);
    }
    
    /**
     * Heuristique gloutonne pour le postier chinois.
     */
    private List<Sommet> postierChinoisHeuristique(Sommet centre, List<Sommet> sommetsImpairs) {
        // Couplage glouton : apparier les sommets impairs deux par deux
        // en choisissant toujours la paire avec la distance minimale
        List<Sommet> sommetsRestants = new ArrayList<>(sommetsImpairs);
        List<Arete> aretesADupliquer = new ArrayList<>();
        
        while (sommetsRestants.size() >= 2) {
            double distanceMin = Double.MAX_VALUE;
            Sommet s1 = null;
            Sommet s2 = null;
            
            for (int i = 0; i < sommetsRestants.size(); i++) {
                for (int j = i + 1; j < sommetsRestants.size(); j++) {
                    double distance = algoChemin.calculerDistance(sommetsRestants.get(i), sommetsRestants.get(j));
                    if (distance > 0 && distance < distanceMin) {
                        distanceMin = distance;
                        s1 = sommetsRestants.get(i);
                        s2 = sommetsRestants.get(j);
                    }
                }
            }
            
            if (s1 != null && s2 != null) {
                // Dupliquer le chemin entre s1 et s2
                List<Sommet> chemin = algoChemin.calculer(s1, s2);
                if (chemin != null) {
                    for (int k = 0; k < chemin.size() - 1; k++) {
                        Sommet source = chemin.get(k);
                        Sommet dest = chemin.get(k + 1);
                        double dist = graphe.getDistance(source, dest);
                        if (dist > 0) {
                            Arete nouvelleArete = new Arete(source, dest, dist, "dupliquee");
                            graphe.ajouterArete(nouvelleArete);
                            aretesADupliquer.add(nouvelleArete);
                        }
                    }
                }
                sommetsRestants.remove(s1);
                sommetsRestants.remove(s2);
            } else {
                break;
            }
        }
        
        // Maintenant le graphe est eulérien, on peut calculer le circuit
        List<Sommet> circuit = circuitEulerien(centre);
        
        // Optionnel : retirer les arêtes dupliquées si on veut garder le graphe original
        // (pour l'instant on les garde)
        
        return circuit;
    }
}

