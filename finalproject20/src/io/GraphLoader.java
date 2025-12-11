package io;

import graph.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Charge un graphe depuis un fichier texte.
 * 
 * Format attendu :
 * #Sommets
 * ID1;Nom1
 * ID2;Nom2
 * ...
 * #Aretes (pour graphe non orienté) ou #Arcs (pour graphe orienté)
 * ID1;ID2;Poids
 * ...
 */
public class GraphLoader {
    
    /**
     * Charge un graphe non orienté depuis un fichier.
     * 
     * @param filePath Chemin vers le fichier
     * @return Un graphe non orienté
     * @throws IOException Si le fichier ne peut pas être lu
     * @throws IllegalArgumentException Si le format du fichier est incorrect
     */
    public static UndirectedGraph loadUndirectedGraph(String filePath) throws IOException {
        UndirectedGraph graph = new UndirectedGraph();
        Map<String, Vertex> vertexMap = new HashMap<>();
        
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
                    // Format : ID;Nom
                    String[] parts = line.split(";");
                    if (parts.length < 2) {
                        throw new IllegalArgumentException("Format de sommet invalide : " + line);
                    }
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    Vertex vertex = new Vertex(id, name);
                    vertexMap.put(id, vertex);
                    graph.addVertex(vertex);
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
        
        return graph;
    }
    
    /**
     * Charge un graphe orienté depuis un fichier.
     * 
     * @param filePath Chemin vers le fichier
     * @return Un graphe orienté
     * @throws IOException Si le fichier ne peut pas être lu
     * @throws IllegalArgumentException Si le format du fichier est incorrect
     */
    public static DirectedGraph loadDirectedGraph(String filePath) throws IOException {
        DirectedGraph graph = new DirectedGraph();
        Map<String, Vertex> vertexMap = new HashMap<>();
        
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
                    } else if (line.equals("#Arcs")) {
                        inVerticesSection = false;
                        inEdgesSection = true;
                    }
                    continue;
                }
                
                if (inVerticesSection) {
                    // Format : ID;Nom
                    String[] parts = line.split(";");
                    if (parts.length < 2) {
                        throw new IllegalArgumentException("Format de sommet invalide : " + line);
                    }
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    Vertex vertex = new Vertex(id, name);
                    vertexMap.put(id, vertex);
                    graph.addVertex(vertex);
                } else if (inEdgesSection) {
                    // Format : ID1;ID2;Poids
                    String[] parts = line.split(";");
                    if (parts.length < 3) {
                        throw new IllegalArgumentException("Format d'arc invalide : " + line);
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
        
        return graph;
    }
}

