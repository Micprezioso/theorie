package theme3;

import graph.*;

import java.util.*;

/**
 * Gère le rééquilibrage des quartiers en fonction de la capacité journalière.
 * Utilisé pour l'hypothèse 2 du Thème 3.
 */
public class CapacityRebalancer {
    
    /**
     * Message de déplacement d'un quartier.
     */
    public static class MoveMessage {
        private final Vertex vertex;
        private final int fromDay;
        private final int toDay;
        private final boolean isNewDay;
        
        public MoveMessage(Vertex vertex, int fromDay, int toDay, boolean isNewDay) {
            this.vertex = vertex;
            this.fromDay = fromDay;
            this.toDay = toDay;
            this.isNewDay = isNewDay;
        }
        
        public Vertex getVertex() {
            return vertex;
        }
        
        public int getFromDay() {
            return fromDay;
        }
        
        public int getToDay() {
            return toDay;
        }
        
        public boolean isNewDay() {
            return isNewDay;
        }
    }
    
    /**
     * Résultat du rééquilibrage avec le planning final.
     */
    public static class RebalancingResult {
        private final Map<Vertex, Integer> vertexToDay;
        private final Map<Integer, List<Vertex>> dayToVertices;
        private final Map<Integer, Double> dayToTotalQuantity;
        private final int totalDays;
        private final List<MoveMessage> moveMessages;
        
        public RebalancingResult(Map<Vertex, Integer> vertexToDay, 
                                Map<Integer, Double> dayToTotalQuantity,
                                int totalDays,
                                List<MoveMessage> moveMessages) {
            this.vertexToDay = vertexToDay;
            this.totalDays = totalDays;
            this.dayToVertices = new HashMap<>();
            this.dayToTotalQuantity = dayToTotalQuantity;
            this.moveMessages = moveMessages;
            
            // Construire la map inverse
            for (Map.Entry<Vertex, Integer> entry : vertexToDay.entrySet()) {
                int day = entry.getValue();
                dayToVertices.computeIfAbsent(day, k -> new ArrayList<>()).add(entry.getKey());
            }
            
            // Trier les quartiers par ordre alphabétique dans chaque jour
            for (List<Vertex> vertices : dayToVertices.values()) {
                vertices.sort(Comparator.comparing(Vertex::getId));
            }
        }
        
        public Map<Vertex, Integer> getVertexToDay() {
            return vertexToDay;
        }
        
        public Map<Integer, List<Vertex>> getDayToVertices() {
            return dayToVertices;
        }
        
        public Map<Integer, Double> getDayToTotalQuantity() {
            return dayToTotalQuantity;
        }
        
        public int getTotalDays() {
            return totalDays;
        }
        
        public List<MoveMessage> getMoveMessages() {
            return moveMessages;
        }
    }
    
    /**
     * Rééquilibre le planning en respectant la capacité maximale journalière.
     * 
     * @param graph Le graphe des quartiers
     * @param initialColoring Le résultat de la coloration initiale (H1)
     * @param quantities Les quantités de déchets par quartier
     * @param maxCapacity La capacité maximale journalière (Qmax)
     * @return Un RebalancingResult avec le planning rééquilibré
     */
    public static RebalancingResult rebalance(UndirectedGraph graph,
                                               WelshPowellColoring.ColoringResult initialColoring,
                                               Map<Vertex, Double> quantities,
                                               double maxCapacity) {
        // Copier la coloration initiale
        Map<Vertex, Integer> vertexToDay = new HashMap<>(initialColoring.getVertexToColor());
        List<MoveMessage> moveMessages = new ArrayList<>();
        
        // Calculer les charges initiales par jour
        Map<Integer, Double> dayToTotalQuantity = new HashMap<>();
        for (Map.Entry<Vertex, Integer> entry : vertexToDay.entrySet()) {
            int day = entry.getValue();
            double quantity = quantities.getOrDefault(entry.getKey(), 0.0);
            dayToTotalQuantity.put(day, dayToTotalQuantity.getOrDefault(day, 0.0) + quantity);
        }
        
        int maxDay = Collections.max(vertexToDay.values());
        
        // Répéter jusqu'à ce qu'il n'y ait plus de jours surchargés
        boolean changed = true;
        while (changed) {
            changed = false;
            
            // Trouver tous les jours surchargés
            List<Integer> overloadedDays = new ArrayList<>();
            for (Map.Entry<Integer, Double> entry : dayToTotalQuantity.entrySet()) {
                if (entry.getValue() > maxCapacity) {
                    overloadedDays.add(entry.getKey());
                }
            }
            
            if (overloadedDays.isEmpty()) {
                break; // Plus de jours surchargés
            }
            
            // Traiter chaque jour surchargé
            for (Integer overloadedDay : overloadedDays) {
                // Continuer à traiter ce jour jusqu'à ce qu'il ne soit plus surchargé
                boolean dayStillOverloaded = true;
                while (dayStillOverloaded) {
                    // Récupérer les quartiers de ce jour (recalculer à chaque itération car ils peuvent changer)
                    List<Vertex> verticesInDay = new ArrayList<>();
                    for (Map.Entry<Vertex, Integer> entry : vertexToDay.entrySet()) {
                        if (entry.getValue().equals(overloadedDay)) {
                            verticesInDay.add(entry.getKey());
                        }
                    }
                    
                    // Vérifier si le jour est encore surchargé
                    double currentDayLoad = dayToTotalQuantity.get(overloadedDay);
                    if (currentDayLoad <= maxCapacity) {
                        dayStillOverloaded = false;
                        break;
                    }
                    
                    // Trier par quantité décroissante pour traiter les plus gros d'abord
                    verticesInDay.sort((v1, v2) -> {
                        double q1 = quantities.getOrDefault(v1, 0.0);
                        double q2 = quantities.getOrDefault(v2, 0.0);
                        return Double.compare(q2, q1);
                    });
                    
                    // Traiter chaque quartier UN PAR UN dans l'ordre décroissant
                    boolean anyMoved = false;
                    
                    for (Vertex vertex : verticesInDay) {
                        // Vérifier si le quartier est toujours dans ce jour (il peut avoir été déplacé)
                        Integer currentDay = vertexToDay.get(vertex);
                        if (currentDay == null || !currentDay.equals(overloadedDay)) {
                            continue; // Ce quartier a déjà été déplacé, passer au suivant
                        }
                        
                        double vertexQuantity = quantities.getOrDefault(vertex, 0.0);
                        
                        // Recalculer la charge actuelle du jour (elle peut avoir changé après les déplacements précédents)
                        currentDayLoad = dayToTotalQuantity.get(overloadedDay);
                        
                        // Si le jour n'est plus surchargé, arrêter
                        if (currentDayLoad <= maxCapacity) {
                            dayStillOverloaded = false;
                            break;
                        }
                        
                        // Chercher un jour compatible parmi TOUS les jours existants (sauf le jour surchargé)
                        boolean moved = false;
                        for (int candidateDay = 1; candidateDay <= maxDay; candidateDay++) {
                            if (candidateDay == overloadedDay) {
                                continue; // Ignorer le jour surchargé
                            }
                            
                            double candidateDayLoad = dayToTotalQuantity.getOrDefault(candidateDay, 0.0);
                            
                            // Condition 1 : Vérifier la capacité (l'ajout ne doit pas faire dépasser Qmax)
                            if (candidateDayLoad + vertexQuantity > maxCapacity) {
                                continue;
                            }
                            
                            // Condition 2 : Aucun quartier voisin ne doit être déjà planifié ce jour-là
                            boolean hasNeighbor = false;
                            for (Vertex neighbor : graph.getNeighbors(vertex)) {
                                Integer neighborDay = vertexToDay.get(neighbor);
                                if (neighborDay != null && neighborDay == candidateDay) {
                                    hasNeighbor = true;
                                    break;
                                }
                            }
                            
                            if (!hasNeighbor) {
                                // Les deux conditions sont satisfaites : déplacer le quartier
                                vertexToDay.put(vertex, candidateDay);
                                dayToTotalQuantity.put(overloadedDay, currentDayLoad - vertexQuantity);
                                dayToTotalQuantity.put(candidateDay, candidateDayLoad + vertexQuantity);
                                moveMessages.add(new MoveMessage(vertex, overloadedDay, candidateDay, false));
                                moved = true;
                                anyMoved = true;
                                changed = true;
                                break; // Quartier déplacé, passer au quartier suivant
                            }
                        }
                        
                        // Si aucun jour existant ne permet d'accueillir ce quartier, créer un nouveau jour
                        if (!moved) {
                            // Vérifier à nouveau que le quartier est toujours dans ce jour et que le jour est encore surchargé
                            currentDay = vertexToDay.get(vertex);
                            currentDayLoad = dayToTotalQuantity.get(overloadedDay);
                            if (currentDay != null && currentDay.equals(overloadedDay) && currentDayLoad > maxCapacity) {
                                maxDay++;
                                vertexToDay.put(vertex, maxDay);
                                dayToTotalQuantity.put(overloadedDay, currentDayLoad - vertexQuantity);
                                dayToTotalQuantity.put(maxDay, vertexQuantity);
                                moveMessages.add(new MoveMessage(vertex, overloadedDay, maxDay, true));
                                anyMoved = true;
                                changed = true;
                                // Continuer avec le quartier suivant
                            }
                        }
                    }
                    
                    // Si aucun quartier n'a pu être déplacé ou créé, arrêter pour ce jour
                    if (!anyMoved) {
                        dayStillOverloaded = false;
                    }
                }
            }
        }
        
        return new RebalancingResult(vertexToDay, dayToTotalQuantity, maxDay, moveMessages);
    }
}

