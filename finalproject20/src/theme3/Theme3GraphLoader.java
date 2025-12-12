package theme3;

import graph.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Charge un graphe pour le Thème 3 avec les quantités de déchets associées à chaque quartier.
 * 
 * Format attendu :
 * #Sommets
 * ID;Quantité
 * ...
 * #Aretes
 * ID1;ID2;Poids
 * ...
 */
public class Theme3GraphLoader {
    
    /**
     * Résultat du chargement : graphe + quantités de déchets par quartier.
     */
    public static class LoadResult {
        private final UndirectedGraph graph;
        private final Map<Vertex, Double> quantities;
        
        public LoadResult(UndirectedGraph graph, Map<Vertex, Double> quantities) {
            this.graph = graph;
            this.quantities = quantities;
        }
        
        public UndirectedGraph getGraph() {
            return graph;
        }
        
        public Map<Vertex, Double> getQuantities() {
            return quantities;
        }
    }
    
    /**
     * Charge un graphe avec les quantités de déchets depuis un fichier.
     * 
     * @param filePath Chemin vers le fichier
     * @return Un LoadResult contenant le graphe et les quantités
     * @throws IOException Si le fichier ne peut pas être lu
     * @throws IllegalArgumentException Si le format du fichier est incorrect
     */
    public static LoadResult loadGraphWithQuantities(String filePath) throws IOException {
        UndirectedGraph graph = new UndirectedGraph();
        Map<String, Vertex> vertexMap = new HashMap<>();
        Map<Vertex, Double> quantities = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inVerticesSection = false;
            boolean inEdgesSection = false;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Ignorer les lignes vides et les commentaires
                if (line.isEmpty() || line.startsWith("#")) {
                    if (line.equals("#Sommets")) {
                        inVerticesSection = true;
                        inEdgesSection = false;
                    } else if (line.equals("#Aretes")) {
                        inVerticesSection = false;
                        inEdgesSection = true;
                    }
                    continue;
                }
                
                if (inVerticesSection) {
                    // Format : ID;Quantité
                    String[] parts = line.split(";");
                    if (parts.length < 2) {
                        throw new IllegalArgumentException("Format de sommet invalide : " + line);
                    }
                    String id = parts[0].trim();
                    double quantity;
                    try {
                        quantity = Double.parseDouble(parts[1].trim());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Quantité invalide : " + parts[1]);
                    }
                    
                    // Utiliser l'ID comme nom aussi
                    Vertex vertex = new Vertex(id, id);
                    vertexMap.put(id, vertex);
                    graph.addVertex(vertex);
                    quantities.put(vertex, quantity);
                } else if (inEdgesSection) {
                    // Format : ID1;ID2;Poids
                    String[] parts = line.split(";");
                    if (parts.length < 3) {
                        throw new IllegalArgumentException("Format d'arête invalide : " + line);
                    }
                    String id1 = parts[0].trim();
                    String id2 = parts[1].trim();
                    double weight;
                    try {
                        weight = Double.parseDouble(parts[2].trim());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Poids invalide : " + parts[2]);
                    }
                    
                    Vertex v1 = vertexMap.get(id1);
                    Vertex v2 = vertexMap.get(id2);
                    if (v1 == null || v2 == null) {
                        throw new IllegalArgumentException("Sommet non trouvé : " + id1 + " ou " + id2);
                    }
                    
                    graph.addEdge(v1, v2, weight);
                }
            }
        }
        
        if (graph.isEmpty()) {
            throw new IllegalArgumentException("Le graphe chargé est vide");
        }
        
        return new LoadResult(graph, quantities);
    }
}

