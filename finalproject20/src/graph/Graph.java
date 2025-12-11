package graph;

import java.util.List;
import java.util.Set;

/**
 * Interface générique pour représenter un graphe.
 * Définit les opérations de base communes aux graphes orientés et non orientés.
 */
public interface Graph {
    /**
     * Ajoute un sommet au graphe.
     */
    void addVertex(Vertex vertex);

    /**
     * Retourne tous les sommets du graphe.
     */
    Set<Vertex> getVertices();

    /**
     * Retourne le sommet avec l'identifiant donné, ou null s'il n'existe pas.
     */
    Vertex getVertex(String id);

    /**
     * Retourne le nombre de sommets.
     */
    int getVertexCount();

    /**
     * Retourne le nombre d'arêtes/arcs.
     */
    int getEdgeCount();

    /**
     * Retourne le degré d'un sommet (nombre d'arêtes/arcs incidents).
     * Pour un graphe orienté, on peut considérer le degré sortant, entrant, ou total.
     */
    int getDegree(Vertex vertex);

    /**
     * Vérifie si le graphe est vide.
     */
    boolean isEmpty();

    /**
     * Retourne tous les voisins d'un sommet (sommets directement connectés).
     * Pour un graphe orienté, cela peut être les successeurs.
     */
    List<Vertex> getNeighbors(Vertex vertex);
}

