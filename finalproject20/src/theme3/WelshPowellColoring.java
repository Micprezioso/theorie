package theme3;

import graph.*;

import java.util.*;

/**
 * Implémente l'algorithme de coloration de graphe de Welsh & Powell.
 * Utilisé pour l'hypothèse 1 du Thème 3 : planification des jours de collecte
 * en respectant uniquement la contrainte de voisinage.
 */
public class WelshPowellColoring {
    
    /**
     * Résultat de la coloration : mapping sommet -> couleur (jour).
     */
    public static class ColoringResult {
        private final Map<Vertex, Integer> vertexToColor;
        private final Map<Integer, List<Vertex>> colorToVertices;
        private final int chromaticNumber;
        
        public ColoringResult(Map<Vertex, Integer> vertexToColor, int chromaticNumber) {
            this.vertexToColor = vertexToColor;
            this.chromaticNumber = chromaticNumber;
            this.colorToVertices = new HashMap<>();
            
            // Construire la map inverse
            for (Map.Entry<Vertex, Integer> entry : vertexToColor.entrySet()) {
                int color = entry.getValue();
                colorToVertices.computeIfAbsent(color, k -> new ArrayList<>()).add(entry.getKey());
            }
            
            // Trier les quartiers par ordre alphabétique dans chaque couleur
            for (List<Vertex> vertices : colorToVertices.values()) {
                vertices.sort(Comparator.comparing(Vertex::getId));
            }
        }
        
        public Map<Vertex, Integer> getVertexToColor() {
            return vertexToColor;
        }
        
        public Map<Integer, List<Vertex>> getColorToVertices() {
            return colorToVertices;
        }
        
        public int getChromaticNumber() {
            return chromaticNumber;
        }
        
        public int getColor(Vertex vertex) {
            return vertexToColor.getOrDefault(vertex, -1);
        }
    }
    
    /**
     * Applique l'algorithme de Welsh & Powell pour colorier le graphe.
     * 
     * @param graph Le graphe non orienté à colorier
     * @return Un ColoringResult contenant la coloration et le nombre chromatique
     */
    public static ColoringResult colorGraph(UndirectedGraph graph) {
        if (graph.isEmpty()) {
            throw new IllegalArgumentException("Le graphe est vide");
        }
        
        Map<Vertex, Integer> vertexToColor = new HashMap<>();
        
        // Étape 1 : Calculer le degré de chaque sommet
        List<Vertex> vertices = new ArrayList<>(graph.getVertices());
        Map<Vertex, Integer> degrees = new HashMap<>();
        for (Vertex v : vertices) {
            degrees.put(v, graph.getDegree(v));
        }
        
        // Étape 2 : Trier les sommets par degré décroissant
        vertices.sort((v1, v2) -> {
            int deg1 = degrees.get(v1);
            int deg2 = degrees.get(v2);
            if (deg1 != deg2) {
                return Integer.compare(deg2, deg1); // Décroissant
            }
            // En cas d'égalité, trier par ID pour avoir un ordre déterministe
            return v1.getId().compareTo(v2.getId());
        });
        
        // Étape 3 : Colorier les sommets
        int maxColor = -1;
        Map<Integer, Integer> colorToFirstIndex = new HashMap<>(); // Pour suivre l'ordre d'apparition
        
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            
            // Trouver la plus petite couleur disponible (qui n'est pas utilisée par un voisin)
            Set<Integer> usedColors = new HashSet<>();
            for (Vertex neighbor : graph.getNeighbors(vertex)) {
                Integer neighborColor = vertexToColor.get(neighbor);
                if (neighborColor != null) {
                    usedColors.add(neighborColor);
                }
            }
            
            // Trouver la première couleur disponible (commence à 1)
            int color = 1;
            while (usedColors.contains(color)) {
                color++;
            }
            
            vertexToColor.put(vertex, color);
            maxColor = Math.max(maxColor, color);
            
            // Enregistrer l'index de la première apparition de cette couleur
            if (!colorToFirstIndex.containsKey(color)) {
                colorToFirstIndex.put(color, i);
            }
        }
        
        // Remapper les couleurs selon l'ordre d'apparition (première utilisation)
        List<Integer> colorsInOrder = new ArrayList<>(colorToFirstIndex.keySet());
        colorsInOrder.sort(Comparator.comparing(colorToFirstIndex::get));
        
        Map<Integer, Integer> colorRemapping = new HashMap<>();
        int newColor = 1;
        for (Integer oldColor : colorsInOrder) {
            colorRemapping.put(oldColor, newColor++);
        }
        
        // Appliquer le remapping
        Map<Vertex, Integer> remappedVertexToColor = new HashMap<>();
        for (Map.Entry<Vertex, Integer> entry : vertexToColor.entrySet()) {
            int newColorValue = colorRemapping.get(entry.getValue());
            remappedVertexToColor.put(entry.getKey(), newColorValue);
        }
        
        return new ColoringResult(remappedVertexToColor, colorRemapping.size());
    }
}

