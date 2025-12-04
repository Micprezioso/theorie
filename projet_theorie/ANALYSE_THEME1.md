# Analyse détaillée du Thème 1 : Optimisation du ramassage aux pieds des habitations

## 1. Analyse détaillée du Thème 1

### 1.1. Objectifs du Thème 1

Le Thème 1 se concentre sur l'optimisation de la collecte de déchets directement aux pieds des habitations. Les objectifs principaux sont :

- **Optimiser les itinéraires de ramassage** pour minimiser les distances parcourues
- **Organiser la collecte** de différents types de déchets (encombrants, poubelles)
- **Planifier les tournées** en tenant compte des contraintes du réseau routier
- **Appliquer les concepts de théorie des graphes** à un problème concret de collecte de déchets

### 1.2. Contraintes importantes

#### Contraintes techniques
- **Un seul camion** intervient pour chaque problématique
- Le camion **part toujours du centre de traitement** et **y revient à la fin** de la collecte
- Le graphe de base est le **réseau routier** de la commune
- C'est un **multigraphe pondéré** par les distances entre intersections/carrefours
- L'orientation du graphe varie selon les hypothèses (HO1, HO2, HO3)

#### Contraintes pratiques
- Pour les encombrants : les particuliers peuvent demander l'enlèvement à domicile
- Pour les poubelles : le camion doit ramasser **toutes les poubelles** de la commune (chaque poubelle de chaque côté de chaque rue)
- Les adresses peuvent être simplifiées en indiquant uniquement la section de rue (arc ou arête) où elles se trouvent

### 1.3. Hypothèses sur le graphe

| Code | Description | Type de graphe |
|------|-------------|----------------|
| **HO1** | Toutes les rues sont à double sens. Les camions ramassent d'un côté et de l'autre de la rue lors d'un seul passage. | **Graphe non orienté** |
| **HO2** | Certaines rues peuvent être à sens unique. Les rues à double sens possèdent au moins deux voies et les camions ne ramassent que du côté de la voie sur laquelle ils passent. | **Graphe orienté** |
| **HO3** | Certaines rues peuvent être à sens unique, d'autres à double sens. Dans les rues à double sens ne possédant qu'une seule voie, les camions ramassent des deux côtés lors d'un seul passage, mais dans les rues possédant plusieurs voies, ils ne ramassent que du côté de la voie sur laquelle ils passent. | **Graphe mixte** |

### 1.4. Tableau récapitulatif des sous-problèmes

| Sous-problème | Objectif | Contraintes | Type de graphe | Type de problème |
|---------------|----------|-------------|----------------|------------------|
| **P1.1 : Collecte d'un seul encombrant** | Trouver l'itinéraire le plus court pour se rendre chez un particulier | Un seul camion, départ/retour au centre de traitement | Selon HO1/HO2/HO3 | Plus court chemin |
| **P1.2 : Tournée d'encombrants (regroupés)** | Calculer l'itinéraire le plus court pour passer chez ~10 particuliers en une tournée | Tournée limitée à une dizaine de ramassages, départ/retour au centre | Selon HO1/HO2/HO3 | TSP (Traveling Salesman Problem) |
| **P2.1 : Collecte toutes poubelles (cas eulérien)** | Parcourir toutes les rues en minimisant la distance | Tous les sommets de degrés pairs | HO1 (non orienté) | Circuit eulérien |
| **P2.2 : Collecte toutes poubelles (2 sommets impairs)** | Parcourir toutes les rues en minimisant la distance | Exactement 2 sommets de degrés impairs | HO1 (non orienté) | Chemin eulérien |
| **P2.3 : Collecte toutes poubelles (cas général)** | Parcourir toutes les rues en minimisant la distance | Aucune contrainte sur la parité des degrés | Selon HO1/HO2/HO3 | Postier chinois (Chinese Postman Problem) |

---

## 2. Lien avec la théorie des graphes

### 2.1. Problématique 1 : Collecte des encombrants

#### P1.1 : Un seul domicile à desservir

**Problème de graphes correspondant :** Plus court chemin (Shortest Path Problem)

**Explication :**
- Il s'agit de trouver le chemin de coût minimal entre le centre de traitement (sommet source) et l'adresse du particulier (sommet destination), puis le retour au centre.
- C'est un problème classique de plus court chemin dans un graphe pondéré.

**Complexité :**
- **HO1 (non orienté) :** Plus simple, peut utiliser Dijkstra ou BFS si toutes les distances sont égales
- **HO2 (orienté) :** Nécessite un algorithme adapté aux graphes orientés (Dijkstra)
- **HO3 (mixte) :** Plus complexe, nécessite de gérer à la fois arcs orientés et arêtes non orientées

#### P1.2 : Tournée de plusieurs domiciles

**Problème de graphes correspondant :** TSP (Traveling Salesman Problem) ou problème de tournée

**Explication :**
- Il faut visiter un ensemble de sommets (les adresses des particuliers) en partant du centre de traitement et en y revenant, en minimisant la distance totale.
- C'est un problème de TSP avec point de départ/d'arrivée fixe.

**Complexité :**
- **Problème NP-difficile** en général
- Pour ~10 domiciles, une solution exacte peut être envisageable (force brute ou programmation dynamique)
- Sinon, utiliser des heuristiques (plus proche voisin, insertion, etc.)

**Variations selon l'orientation :**
- **HO1 :** TSP symétrique (plus simple)
- **HO2 :** TSP asymétrique (ATSP, plus complexe)
- **HO3 :** TSP mixte (très complexe)

### 2.2. Problématique 2 : Collecte des poubelles dans toutes les rues

#### P2.1 : Cas eulérien (tous sommets de degré pair)

**Problème de graphes correspondant :** Circuit eulérien (Eulerian Circuit)

**Explication :**
- Si tous les sommets ont un degré pair, le graphe admet un circuit eulérien (théorème d'Euler).
- Un circuit eulérien permet de parcourir chaque arête exactement une fois et de revenir au point de départ.
- C'est la solution optimale (distance minimale = somme des poids de toutes les arêtes).

**Complexité :**
- **O(m)** où m est le nombre d'arêtes (algorithme de Fleury ou algorithme de Hierholzer)
- Solution exacte et optimale

#### P2.2 : Cas avec 2 sommets de degré impair

**Problème de graphes correspondant :** Chemin eulérien (Eulerian Path)

**Explication :**
- Si exactement 2 sommets ont un degré impair, le graphe admet un chemin eulérien (mais pas de circuit).
- Le chemin eulérien commence à un sommet impair et se termine à l'autre.
- Pour revenir au centre de traitement, il faut ajouter le plus court chemin entre le sommet d'arrivée et le centre.

**Complexité :**
- **O(m)** pour trouver le chemin eulérien
- **O(n²)** pour trouver le plus court chemin de retour (Dijkstra)
- Solution exacte et optimale

#### P2.3 : Cas général

**Problème de graphes correspondant :** Problème du postier chinois (Chinese Postman Problem)

**Explication :**
- Dans le cas général, il faut parcourir toutes les arêtes au moins une fois en minimisant la distance totale.
- Il faut d'abord rendre le graphe eulérien en dupliquant certaines arêtes (ou arcs), puis trouver un circuit eulérien.
- L'objectif est de minimiser le poids total des arêtes à dupliquer.

**Complexité :**
- **Graphe non orienté (HO1) :** Résolu en O(n³) par un algorithme de couplage parfait de poids minimal sur les sommets impairs
- **Graphe orienté (HO2) :** Plus complexe, nécessite de rendre le graphe eulérien orienté (équilibrage des degrés entrants/sortants)
- **Graphe mixte (HO3) :** Très complexe, problème NP-difficile en général

### 2.3. Tableau synthétique des problèmes de graphes

| Sous-problème | Problème de graphes | Type de solution | Complexité |
|---------------|---------------------|------------------|------------|
| P1.1 | Plus court chemin | Exacte (Dijkstra) | O(n²) ou O(m log n) |
| P1.2 | TSP | Exacte (petit n) ou Heuristique | NP-difficile / O(n²) heuristique |
| P2.1 | Circuit eulérien | Exacte (Hierholzer) | O(m) |
| P2.2 | Chemin eulérien + plus court chemin | Exacte | O(m + n²) |
| P2.3 | Postier chinois | Exacte (HO1) ou Heuristique (HO2/HO3) | O(n³) à NP-difficile |

---

## 3. Méthodes de résolution et algorithmes possibles

### 3.1. P1.1 : Plus court chemin pour un seul encombrant

#### Méthode : Algorithme de Dijkstra

**Fonctionnement :**
1. Initialiser la distance du centre de traitement à 0, toutes les autres à +∞
2. Marquer tous les sommets comme non visités
3. Tant qu'il reste des sommets non visités :
   - Choisir le sommet non visité avec la distance minimale
   - Le marquer comme visité
   - Pour chaque voisin non visité :
     - Calculer la distance via ce sommet
     - Si cette distance est inférieure à la distance actuelle, la mettre à jour
4. La distance minimale jusqu'à l'adresse du particulier est trouvée
5. Reconstruire le chemin en remontant depuis le sommet destination
6. Calculer le chemin de retour de la même manière

**Pseudo-code :**
```
FONCTION plusCourtChemin(graphe, source, destination):
    distances = tableau de +∞ pour tous les sommets
    distances[source] = 0
    predecesseurs = tableau de null pour tous les sommets
    nonVisites = ensemble de tous les sommets
    
    TANT QUE nonVisites n'est pas vide:
        u = sommet de nonVisites avec distance minimale
        RETIRER u de nonVisites
        
        SI u == destination:
            RETOURNER reconstruireChemin(predecesseurs, source, destination)
        
        POUR CHAQUE voisin v de u:
            SI v dans nonVisites:
                nouvelleDistance = distances[u] + poids(u, v)
                SI nouvelleDistance < distances[v]:
                    distances[v] = nouvelleDistance
                    predecesseurs[v] = u
    
    RETOURNER null (pas de chemin)
```

**Complexité :** O(n²) avec matrice d'adjacence, O(m log n) avec liste d'adjacence et tas de priorité

**Type :** Solution exacte

### 3.2. P1.2 : Tournée de plusieurs encombrants

#### Méthode 1 : Heuristique du plus proche voisin

**Fonctionnement :**
1. Partir du centre de traitement
2. Tant qu'il reste des domiciles à visiter :
   - Trouver le domicile non visité le plus proche du domicile actuel
   - L'ajouter à la tournée
   - Se déplacer vers ce domicile
3. Revenir au centre de traitement

**Pseudo-code :**
```
FONCTION tourneePlusProcheVoisin(centre, domiciles):
    tournee = [centre]
    domicilesRestants = copie de domiciles
    positionActuelle = centre
    
    TANT QUE domicilesRestants n'est pas vide:
        plusProche = null
        distanceMin = +∞
        
        POUR CHAQUE domicile d dans domicilesRestants:
            distance = plusCourtChemin(positionActuelle, d)
            SI distance < distanceMin:
                distanceMin = distance
                plusProche = d
        
        AJOUTER plusProche à tournee
        RETIRER plusProche de domicilesRestants
        positionActuelle = plusProche
    
    AJOUTER centre à tournee (retour)
    RETOURNER tournee
```

**Complexité :** O(n² × complexité_Dijkstra) = O(n² × m log n) ou O(n⁴)

**Type :** Heuristique (solution approchée)

#### Méthode 2 : Solution exacte par force brute (petit nombre de domiciles)

**Fonctionnement :**
1. Générer toutes les permutations possibles des domiciles
2. Pour chaque permutation, calculer la distance totale (centre → domicile1 → ... → domicileN → centre)
3. Retourner la permutation avec la distance minimale

**Pseudo-code :**
```
FONCTION tourneeForceBrute(centre, domiciles):
    meilleureTournee = null
    distanceMinimale = +∞
    
    POUR CHAQUE permutation p de domiciles:
        distance = distance(centre, p[0])
        POUR i de 0 à taille(p)-2:
            distance += distance(p[i], p[i+1])
        distance += distance(p[dernier], centre)
        
        SI distance < distanceMinimale:
            distanceMinimale = distance
            meilleureTournee = p
    
    RETOURNER [centre] + meilleureTournee + [centre]
```

**Complexité :** O(n! × n) où n est le nombre de domiciles (10 domiciles = 10! = 3 628 800 permutations)

**Type :** Solution exacte (mais seulement pour petit n)

### 3.3. P2.1 : Circuit eulérien (cas idéal)

#### Méthode : Algorithme de Hierholzer

**Fonctionnement :**
1. Vérifier que tous les sommets ont un degré pair
2. Choisir un sommet de départ (le centre de traitement)
3. Construire un circuit en suivant des arêtes non utilisées jusqu'à revenir au départ
4. S'il reste des arêtes non parcourues :
   - Trouver un sommet du circuit actuel qui a encore des arêtes non utilisées
   - Construire un nouveau circuit partant de ce sommet
   - Fusionner ce nouveau circuit dans le circuit principal
5. Répéter jusqu'à ce que toutes les arêtes soient parcourues

**Pseudo-code :**
```
FONCTION circuitEulerien(graphe, depart):
    circuit = []
    arêtesUtilisées = ensemble vide
    
    FONCTION construireCircuit(sommet):
        circuitLocal = [sommet]
        sommetActuel = sommet
        
        TANT QUE il existe une arête (sommetActuel, voisin) non utilisée:
            AJOUTER (sommetActuel, voisin) à arêtesUtilisées
            sommetActuel = voisin
            AJOUTER sommetActuel à circuitLocal
        
        RETOURNER circuitLocal
    
    circuit = construireCircuit(depart)
    
    TANT QUE taille(arêtesUtilisées) < nombreArêtes(graphe):
        POUR i de 0 à taille(circuit)-1:
            sommet = circuit[i]
            SI sommet a des arêtes non utilisées:
                nouveauCircuit = construireCircuit(sommet)
                INSERER nouveauCircuit dans circuit à la position i
                SORTIR de la boucle
    
    RETOURNER circuit
```

**Complexité :** O(m) où m est le nombre d'arêtes

**Type :** Solution exacte et optimale

### 3.4. P2.2 : Chemin eulérien (2 sommets impairs)

#### Méthode : Algorithme de Hierholzer modifié

**Fonctionnement :**
1. Identifier les deux sommets de degré impair
2. Si le centre de traitement est l'un d'eux, commencer par lui
3. Sinon, commencer par l'un des deux sommets impairs
4. Appliquer l'algorithme de Hierholzer pour obtenir un chemin eulérien
5. Si le chemin ne se termine pas au centre, ajouter le plus court chemin de retour

**Pseudo-code :**
```
FONCTION cheminEulerien(graphe, centre):
    sommetsImpairs = trouverSommetsImpairs(graphe)
    
    SI taille(sommetsImpairs) != 2:
        ERREUR "Le graphe n'a pas exactement 2 sommets impairs"
    
    depart = SI centre dans sommetsImpairs ALORS centre SINON sommetsImpairs[0]
    arrivee = l'autre sommet impair
    
    chemin = algorithmeHierholzer(graphe, depart)
    
    SI arrivee != centre:
        cheminRetour = plusCourtChemin(arrivee, centre)
        AJOUTER cheminRetour à chemin (sans le premier sommet)
    
    RETOURNER chemin
```

**Complexité :** O(m + n²) (O(m) pour le chemin eulérien + O(n²) pour le plus court chemin de retour)

**Type :** Solution exacte et optimale

### 3.5. P2.3 : Postier chinois (cas général)

#### Méthode pour graphe non orienté (HO1) : Couplage parfait de poids minimal

**Fonctionnement :**
1. Identifier tous les sommets de degré impair
2. Construire un graphe complet sur ces sommets, où le poids d'une arête est la distance la plus courte entre les deux sommets
3. Trouver un couplage parfait de poids minimal dans ce graphe complet
4. Dupliquer les arêtes correspondant aux chemins du couplage dans le graphe original
5. Le graphe devient eulérien, appliquer l'algorithme de Hierholzer

**Pseudo-code :**
```
FONCTION postierChinoisNonOriente(graphe, centre):
    sommetsImpairs = trouverSommetsImpairs(graphe)
    
    SI taille(sommetsImpairs) == 0:
        RETOURNER circuitEulerien(graphe, centre)
    
    SI taille(sommetsImpairs) % 2 != 0:
        ERREUR "Nombre impair de sommets impairs impossible"
    
    // Construire graphe complet des sommets impairs
    grapheComplet = nouveau graphe
    POUR CHAQUE paire (s1, s2) dans sommetsImpairs:
        distance = plusCourtChemin(graphe, s1, s2)
        AJOUTER arête (s1, s2) avec poids distance à grapheComplet
    
    // Trouver couplage parfait de poids minimal
    couplage = couplageParfaitMinimal(grapheComplet)
    
    // Dupliquer les arêtes dans le graphe original
    POUR CHAQUE paire (s1, s2) dans couplage:
        chemin = reconstruireChemin(s1, s2)
        POUR CHAQUE arête e dans chemin:
            dupliquerArête(graphe, e)
    
    // Le graphe est maintenant eulérien
    RETOURNER circuitEulerien(graphe, centre)
```

**Complexité :** O(n³) pour le couplage parfait (algorithme de Blossom) + O(m) pour le circuit eulérien

**Type :** Solution exacte pour HO1

#### Méthode pour graphe orienté (HO2) : Équilibrage des degrés

**Fonctionnement :**
1. Pour chaque sommet, calculer la différence entre degré entrant et degré sortant
2. Si un sommet a plus d'entrées que de sorties, il faut ajouter des arcs sortants
3. Si un sommet a plus de sorties que d'entrées, il faut ajouter des arcs entrants
4. Résoudre un problème de flot minimum pour équilibrer les degrés
5. Dupliquer les arcs nécessaires
6. Appliquer l'algorithme de Hierholzer pour graphe orienté

**Complexité :** O(n³) à O(n⁴) selon la méthode

**Type :** Solution exacte mais complexe

#### Méthode heuristique pour HO2 et HO3

**Fonctionnement :**
1. Appliquer une heuristique gloutonne : dupliquer les arêtes/arcs les moins coûteux pour rendre le graphe eulérien
2. Appliquer l'algorithme de Hierholzer

**Complexité :** O(m log m + m)

**Type :** Heuristique (solution approchée)

---

## 4. Modélisation des graphes et des données

### 4.1. Type de graphe

- **Structure :** Multigraphe pondéré (plusieurs arêtes/arcs possibles entre deux sommets)
- **Pondération :** Distances entre intersections/carrefours (en mètres ou kilomètres)
- **Orientation :** Variable selon les hypothèses :
  - **HO1 :** Graphe non orienté (arêtes bidirectionnelles)
  - **HO2 :** Graphe orienté (arcs unidirectionnels)
  - **HO3 :** Graphe mixte (mélange d'arêtes et d'arcs)

### 4.2. Représentation en mémoire

#### Option 1 : Liste d'adjacence (recommandée)

**Avantages :**
- Efficace en mémoire pour graphes peu denses
- Permet de parcourir facilement les voisins d'un sommet
- Adaptée aux algorithmes de parcours (Dijkstra, Hierholzer)

**Structure :**
```
Map<Sommet, List<Arete>> ou Map<Sommet, List<Arc>>
```

#### Option 2 : Matrice d'adjacence

**Avantages :**
- Accès direct O(1) pour vérifier l'existence d'une arête
- Simple à implémenter

**Inconvénients :**
- Consomme O(n²) mémoire même pour graphes peu denses
- Moins adaptée pour multigraphes (nécessite liste de poids)

**Recommandation :** Utiliser la liste d'adjacence pour ce projet.

### 4.3. Informations à stocker

#### Pour les sommets (intersections/carrefours)
- **Identifiant unique** (String ou int)
- **Coordonnées** (optionnel, pour visualisation future)
- **Type** (intersection normale, centre de traitement, etc.)
- **Degré** (pour vérifier la parité dans les problèmes eulériens)

#### Pour les arêtes/arcs (rues)
- **Sommet source** (départ)
- **Sommet destination** (arrivée)
- **Distance** (poids de l'arête/arc)
- **Sens** (double sens, sens unique) - selon HO1/HO2/HO3
- **Nom de la rue** (optionnel, pour affichage)

#### Pour les demandes de collecte
- **Type de collecte** (encombrant, poubelle)
- **Adresse** (identifiant du sommet ou de l'arête où se trouve le domicile)
- **Date de demande** (optionnel)
- **Statut** (en attente, programmée, collectée)

#### Pour le centre de traitement
- **Identifiant du sommet** correspondant
- **Capacité** (optionnel, pour extensions futures)
- **Horaires** (optionnel)

---

## 5. Première conception objet Java pour le Thème 1

### 5.1. Cas d'utilisation (Use Cases)

1. **UC1 : Calculer le plus court chemin pour un encombrant**
   - Acteur : Entreprise de collecte
   - Description : Calculer l'itinéraire optimal pour collecter un encombrant à une adresse donnée

2. **UC2 : Planifier une tournée d'encombrants**
   - Acteur : Entreprise de collecte
   - Description : Calculer l'itinéraire optimal pour collecter plusieurs encombrants (jusqu'à ~10) en une tournée

3. **UC3 : Calculer le circuit eulérien pour la collecte des poubelles (cas idéal)**
   - Acteur : Entreprise de collecte
   - Description : Calculer le circuit optimal pour collecter toutes les poubelles quand tous les sommets sont de degré pair

4. **UC4 : Calculer le chemin eulérien pour la collecte des poubelles (2 sommets impairs)**
   - Acteur : Entreprise de collecte
   - Description : Calculer le chemin optimal pour collecter toutes les poubelles quand exactement 2 sommets sont de degré impair

5. **UC5 : Résoudre le problème du postier chinois (cas général)**
   - Acteur : Entreprise de collecte
   - Description : Calculer l'itinéraire optimal pour collecter toutes les poubelles dans le cas général

6. **UC6 : Charger un graphe de test**
   - Acteur : Système
   - Description : Initialiser le graphe à partir de données (fichier ou création manuelle)

### 5.2. Liste des classes Java

| Classe | Responsabilité principale |
|--------|--------------------------|
| `Graphe` | Représenter le réseau routier (multigraphe pondéré) |
| `Sommet` | Représenter une intersection/carrefour |
| `Arete` | Représenter une rue (arête non orientée pour HO1) |
| `Arc` | Représenter une rue orientée (pour HO2/HO3) |
| `CentreTraitement` | Représenter le centre de traitement des déchets |
| `DemandeCollecte` | Représenter une demande de collecte d'encombrant |
| `Tournee` | Représenter une tournée de collecte (séquence de sommets) |
| `AlgorithmePlusCourtChemin` | Implémenter l'algorithme de Dijkstra |
| `AlgorithmeTournee` | Implémenter les algorithmes de tournée (TSP) |
| `AlgorithmeEulerien` | Implémenter les algorithmes eulériens (Hierholzer, postier chinois) |
| `Main` | Point d'entrée du programme, tests |

### 5.3. Diagramme de classes textuel

```
┌─────────────────────┐
│      Sommet         │
├─────────────────────┤
│ - id: String        │
│ - coordonnees: ?    │
│ - type: TypeSommet │
├─────────────────────┤
│ + getId()           │
│ + getVoisins()      │
│ + getDegre()        │
└─────────────────────┘
           ▲
           │
           │
┌─────────────────────┐
│       Arete         │
├─────────────────────┤
│ - source: Sommet    │
│ - destination: Sommet│
│ - distance: double  │
│ - nomRue: String    │
├─────────────────────┤
│ + getSource()       │
│ + getDestination()  │
│ + getDistance()     │
└─────────────────────┘

┌─────────────────────┐
│        Arc          │
├─────────────────────┤
│ - source: Sommet    │
│ - destination: Sommet│
│ - distance: double  │
│ - nomRue: String    │
├─────────────────────┤
│ + getSource()       │
│ + getDestination()  │
│ + getDistance()     │
└─────────────────────┘

┌─────────────────────┐
│       Graphe        │
├─────────────────────┤
│ - sommets: Map<...> │
│ - aretes: List<Arete>│
│ - arcs: List<Arc>   │
│ - type: TypeGraphe │
├─────────────────────┤
│ + ajouterSommet()   │
│ + ajouterArete()    │
│ + ajouterArc()      │
│ + getVoisins()      │
│ + getDistance()     │
│ + estEulerien()     │
└─────────────────────┘

┌─────────────────────┐
│  CentreTraitement   │
├─────────────────────┤
│ - sommet: Sommet    │
├─────────────────────┤
│ + getSommet()       │
└─────────────────────┘

┌─────────────────────┐
│  DemandeCollecte    │
├─────────────────────┤
│ - id: String        │
│ - adresse: Sommet   │
│ - type: TypeCollecte│
├─────────────────────┤
│ + getAdresse()      │
│ + getType()         │
└─────────────────────┘

┌─────────────────────┐
│      Tournee        │
├─────────────────────┤
│ - sommets: List<Sommet>│
│ - distanceTotale: double│
├─────────────────────┤
│ + ajouterSommet()   │
│ + getDistanceTotale()│
│ + afficher()        │
└─────────────────────┘

┌─────────────────────┐
│ AlgorithmePlusCourt │
│       Chemin        │
├─────────────────────┤
│ - graphe: Graphe    │
├─────────────────────┤
│ + calculer(source,  │
│   destination)      │
└─────────────────────┘

┌─────────────────────┐
│  AlgorithmeTournee  │
├─────────────────────┤
│ - graphe: Graphe    │
│ - centre: Sommet    │
├─────────────────────┤
│ + tourneePlusProche │
│   Voisin(domiciles) │
│ + tourneeForceBrute │
│   (domiciles)       │
└─────────────────────┘

┌─────────────────────┐
│ AlgorithmeEulerien  │
├─────────────────────┤
│ - graphe: Graphe    │
├─────────────────────┤
│ + circuitEulerien() │
│ + cheminEulerien()  │
│ + postierChinois()  │
└─────────────────────┘
```

### 5.4. Squelettes de classes Java

#### Classe Sommet

```java
package modele;

/**
 * Représente un sommet du graphe (intersection ou carrefour).
 */
public class Sommet {
    private String id;
    private TypeSommet type;
    
    /**
     * Constructeur d'un sommet.
     * @param id Identifiant unique du sommet
     * @param type Type du sommet (INTERSECTION, CENTRE_TRAITEMENT, etc.)
     */
    public Sommet(String id, TypeSommet type) {
        this.id = id;
        this.type = type;
    }
    
    public String getId() {
        return id;
    }
    
    public TypeSommet getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sommet sommet = (Sommet) obj;
        return id.equals(sommet.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return id;
    }
}
```

#### Enum TypeSommet

```java
package modele;

/**
 * Types de sommets possibles dans le graphe.
 */
public enum TypeSommet {
    INTERSECTION,           // Intersection normale
    CENTRE_TRAITEMENT      // Centre de traitement des déchets
}
```

#### Classe Arete

```java
package modele;

/**
 * Représente une arête non orientée (rue à double sens pour HO1).
 */
public class Arete {
    private Sommet source;
    private Sommet destination;
    private double distance;
    private String nomRue;
    
    /**
     * Constructeur d'une arête.
     * @param source Sommet source
     * @param destination Sommet destination
     * @param distance Distance en mètres
     * @param nomRue Nom de la rue (optionnel)
     */
    public Arete(Sommet source, Sommet destination, double distance, String nomRue) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.nomRue = nomRue;
    }
    
    public Sommet getSource() {
        return source;
    }
    
    public Sommet getDestination() {
        return destination;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public String getNomRue() {
        return nomRue;
    }
    
    /**
     * Vérifie si cette arête connecte deux sommets donnés (dans un sens ou l'autre).
     */
    public boolean connecte(Sommet s1, Sommet s2) {
        return (source.equals(s1) && destination.equals(s2)) ||
               (source.equals(s2) && destination.equals(s1));
    }
}
```

#### Classe Arc

```java
package modele;

/**
 * Représente un arc orienté (rue à sens unique pour HO2/HO3).
 */
public class Arc {
    private Sommet source;
    private Sommet destination;
    private double distance;
    private String nomRue;
    
    /**
     * Constructeur d'un arc.
     * @param source Sommet source
     * @param destination Sommet destination
     * @param distance Distance en mètres
     * @param nomRue Nom de la rue (optionnel)
     */
    public Arc(Sommet source, Sommet destination, double distance, String nomRue) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.nomRue = nomRue;
    }
    
    public Sommet getSource() {
        return source;
    }
    
    public Sommet getDestination() {
        return destination;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public String getNomRue() {
        return nomRue;
    }
}
```

#### Enum TypeGraphe

```java
package modele;

/**
 * Types de graphes selon les hypothèses HO1, HO2, HO3.
 */
public enum TypeGraphe {
    NON_ORIENTE,    // HO1 : toutes les rues à double sens
    ORIENTE,        // HO2 : certaines rues à sens unique
    MIXTE           // HO3 : mélange de rues à double sens et sens unique
}
```

#### Classe Graphe

```java
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
}
```

#### Classe CentreTraitement

```java
package modele;

/**
 * Représente le centre de traitement des déchets.
 */
public class CentreTraitement {
    private Sommet sommet;
    
    /**
     * Constructeur du centre de traitement.
     * @param sommet Le sommet du graphe correspondant au centre
     */
    public CentreTraitement(Sommet sommet) {
        this.sommet = sommet;
    }
    
    public Sommet getSommet() {
        return sommet;
    }
}
```

#### Classe DemandeCollecte

```java
package modele;

/**
 * Représente une demande de collecte d'encombrant.
 */
public class DemandeCollecte {
    private String id;
    private Sommet adresse;
    private TypeCollecte type;
    
    /**
     * Constructeur d'une demande de collecte.
     * @param id Identifiant unique de la demande
     * @param adresse Adresse (sommet) où se trouve le déchet
     * @param type Type de collecte (ENCOMBRANT, POUBELLE)
     */
    public DemandeCollecte(String id, Sommet adresse, TypeCollecte type) {
        this.id = id;
        this.adresse = adresse;
        this.type = type;
    }
    
    public String getId() {
        return id;
    }
    
    public Sommet getAdresse() {
        return adresse;
    }
    
    public TypeCollecte getType() {
        return type;
    }
}
```

#### Enum TypeCollecte

```java
package modele;

/**
 * Types de collecte possibles.
 */
public enum TypeCollecte {
    ENCOMBRANT,  // Collecte d'encombrants
    POUBELLE     // Collecte de poubelles
}
```

#### Classe Tournee

```java
package modele;

import java.util.*;

/**
 * Représente une tournée de collecte (séquence de sommets à visiter).
 */
public class Tournee {
    private List<Sommet> sommets;
    private double distanceTotale;
    private Graphe graphe;
    
    /**
     * Constructeur d'une tournée vide.
     */
    public Tournee(Graphe graphe) {
        this.graphe = graphe;
        this.sommets = new ArrayList<>();
        this.distanceTotale = 0.0;
    }
    
    /**
     * Ajoute un sommet à la tournée et met à jour la distance totale.
     */
    public void ajouterSommet(Sommet sommet) {
        if (!sommets.isEmpty()) {
            Sommet dernier = sommets.get(sommets.size() - 1);
            double distance = graphe.getDistance(dernier, sommet);
            if (distance > 0) {
                distanceTotale += distance;
            }
        }
        sommets.add(sommet);
    }
    
    /**
     * Retourne la distance totale de la tournée.
     */
    public double getDistanceTotale() {
        return distanceTotale;
    }
    
    /**
     * Retourne la liste des sommets de la tournée.
     */
    public List<Sommet> getSommets() {
        return new ArrayList<>(sommets);
    }
    
    /**
     * Affiche la tournée.
     */
    public void afficher() {
        System.out.println("Tournée (" + distanceTotale + " m) :");
        for (Sommet sommet : sommets) {
            System.out.print(sommet.getId() + " -> ");
        }
        System.out.println();
    }
}
```

#### Classe AlgorithmePlusCourtChemin

```java
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
     * Trouve le sommet non visité avec la distance minimale.
     */
    private Sommet trouverSommetMinimal(Set<Sommet> nonVisites, Map<Sommet, Double> distances) {
        Sommet minimal = null;
        double distanceMin = Double.MAX_VALUE;
        for (Sommet sommet : nonVisites) {
            if (distances.get(sommet) < distanceMin) {
                distanceMin = distances.get(sommet);
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
        return chemin;
    }
}
```

#### Classe AlgorithmeTournee

```java
package algorithme;

import modele.*;

import java.util.*;

/**
 * Implémente les algorithmes de tournée (TSP).
 */
public class AlgorithmeTournee {
    private Graphe graphe;
    private Sommet centre;
    private AlgorithmePlusCourtChemin algoChemin;
    
    /**
     * Constructeur.
     * @param graphe Le graphe
     * @param centre Le centre de traitement (point de départ/arrivée)
     */
    public AlgorithmeTournee(Graphe graphe, Sommet centre) {
        this.graphe = graphe;
        this.centre = centre;
        this.algoChemin = new AlgorithmePlusCourtChemin(graphe);
    }
    
    /**
     * Calcule une tournée en utilisant l'heuristique du plus proche voisin.
     * @param domiciles Liste des domiciles à visiter
     * @return La tournée optimisée
     */
    public Tournee tourneePlusProcheVoisin(List<Sommet> domiciles) {
        Tournee tournee = new Tournee(graphe);
        tournee.ajouterSommet(centre);
        
        List<Sommet> domicilesRestants = new ArrayList<>(domiciles);
        Sommet positionActuelle = centre;
        
        while (!domicilesRestants.isEmpty()) {
            Sommet plusProche = null;
            double distanceMin = Double.MAX_VALUE;
            
            for (Sommet domicile : domicilesRestants) {
                List<Sommet> chemin = algoChemin.calculer(positionActuelle, domicile);
                if (chemin != null) {
                    double distance = calculerDistanceChemin(chemin);
                    if (distance < distanceMin) {
                        distanceMin = distance;
                        plusProche = domicile;
                    }
                }
            }
            
            if (plusProche != null) {
                tournee.ajouterSommet(plusProche);
                domicilesRestants.remove(plusProche);
                positionActuelle = plusProche;
            } else {
                break; // Impossible de continuer
            }
        }
        
        // Retour au centre
        tournee.ajouterSommet(centre);
        return tournee;
    }
    
    /**
     * Calcule la distance totale d'un chemin.
     */
    private double calculerDistanceChemin(List<Sommet> chemin) {
        double distance = 0.0;
        for (int i = 0; i < chemin.size() - 1; i++) {
            distance += graphe.getDistance(chemin.get(i), chemin.get(i + 1));
        }
        return distance;
    }
}
```

#### Classe AlgorithmeEulerien

```java
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
     * @param depart Sommet de départ
     * @return Liste des sommets formant le circuit eulérien
     */
    public List<Sommet> circuitEulerien(Sommet depart) {
        // TODO : Implémenter l'algorithme de Hierholzer
        // Pour l'instant, retourne une liste vide
        return new ArrayList<>();
    }
    
    /**
     * Calcule un chemin eulérien (exactement 2 sommets de degré impair).
     * @param depart Un des deux sommets impairs
     * @param arrivee L'autre sommet impair
     * @return Liste des sommets formant le chemin eulérien
     */
    public List<Sommet> cheminEulerien(Sommet depart, Sommet arrivee) {
        // TODO : Implémenter l'algorithme de Hierholzer modifié
        return new ArrayList<>();
    }
    
    /**
     * Résout le problème du postier chinois (cas général).
     * @param centre Centre de traitement (point de départ/arrivée)
     * @return Liste des sommets formant la tournée optimale
     */
    public List<Sommet> postierChinois(Sommet centre) {
        // TODO : Implémenter l'algorithme du postier chinois
        // Pour HO1 (non orienté) : couplage parfait de poids minimal
        return new ArrayList<>();
    }
}
```

#### Classe Main (exemple d'utilisation)

```java
import modele.*;
import algorithme.*;

import java.util.*;

/**
 * Point d'entrée du programme pour tester les fonctionnalités du Thème 1.
 */
public class Main {
    public static void main(String[] args) {
        // Création d'un graphe de test simple
        Graphe graphe = creerGrapheTest();
        
        // Test : Plus court chemin pour un encombrant
        Sommet centre = graphe.getSommet("C");
        Sommet domicile = graphe.getSommet("A");
        
        AlgorithmePlusCourtChemin algo = new AlgorithmePlusCourtChemin(graphe);
        List<Sommet> chemin = algo.calculer(centre, domicile);
        
        if (chemin != null) {
            System.out.println("Chemin trouvé : " + chemin);
        }
        
        // Test : Tournée d'encombrants
        List<Sommet> domiciles = Arrays.asList(
            graphe.getSommet("A"),
            graphe.getSommet("B")
        );
        
        AlgorithmeTournee algoTournee = new AlgorithmeTournee(graphe, centre);
        Tournee tournee = algoTournee.tourneePlusProcheVoisin(domiciles);
        tournee.afficher();
    }
    
    /**
     * Crée un graphe de test simple.
     */
    private static Graphe creerGrapheTest() {
        Graphe graphe = new Graphe(TypeGraphe.NON_ORIENTE);
        
        // Créer les sommets
        Sommet a = new Sommet("A", TypeSommet.INTERSECTION);
        Sommet b = new Sommet("B", TypeSommet.INTERSECTION);
        Sommet c = new Sommet("C", TypeSommet.CENTRE_TRAITEMENT);
        
        graphe.ajouterSommet(a);
        graphe.ajouterSommet(b);
        graphe.ajouterSommet(c);
        
        // Créer les arêtes
        graphe.ajouterArete(new Arete(a, b, 100, "Rue 1"));
        graphe.ajouterArete(new Arete(b, c, 150, "Rue 2"));
        graphe.ajouterArete(new Arete(a, c, 200, "Rue 3"));
        
        return graphe;
    }
}
```

---

## 6. Graphes de tests adaptés au Thème 1

### 6.1. Graphe 1 : Plus court chemin simple (P1.1)

**Objectif :** Tester le calcul du plus court chemin pour un seul encombrant.

**Sommets :**
- C : Centre de traitement
- A, B, D, E : Intersections

**Arêtes (HO1 - non orienté) :**
- C-A : 100 m
- C-B : 150 m
- A-B : 80 m
- A-D : 120 m
- B-E : 90 m
- D-E : 110 m

**Test :** Calculer le chemin optimal de C vers D (doit être C-A-D = 220 m, ou C-B-E-D = 350 m, donc C-A-D est optimal).

**Représentation textuelle :**
```
     A ----80---- B
     |            |
   100|           |150
     |            |
     C            E
     |            |
   120|           |90
     |            |
     D ----110---- E
```

### 6.2. Graphe 2 : Tournée de 3 encombrants (P1.2)

**Objectif :** Tester une petite tournée TSP avec 3 domiciles.

**Sommets :**
- C : Centre de traitement
- A, B, D : Domiciles à visiter

**Arêtes (HO1) :**
- C-A : 50 m
- C-B : 60 m
- C-D : 70 m
- A-B : 30 m
- A-D : 40 m
- B-D : 35 m

**Test :** Trouver la tournée optimale C → ? → ? → ? → C
- Solution optimale : C-A-B-D-C = 50+30+35+70 = 185 m
- Ou : C-A-D-B-C = 50+40+35+60 = 185 m

**Permet de vérifier :** L'algorithme du plus proche voisin et la comparaison avec la solution exacte (force brute pour 3 domiciles = 3! = 6 permutations).

### 6.3. Graphe 3 : Circuit eulérien (P2.1 - cas idéal)

**Objectif :** Tester le circuit eulérien quand tous les sommets sont de degré pair.

**Sommets :**
- C : Centre de traitement (degré 2)
- A, B, D, E : Intersections (tous de degré 2)

**Arêtes (HO1) :**
- C-A : 100 m
- A-B : 80 m
- B-C : 120 m
- C-D : 90 m
- D-E : 70 m
- E-C : 110 m

**Vérification :** Tous les sommets ont un degré pair (2), donc le graphe est eulérien.

**Test :** Trouver un circuit eulérien partant de C.
- Solution : C-A-B-C-D-E-C (ou toute autre permutation valide)
- Distance totale = somme de toutes les arêtes = 100+80+120+90+70+110 = 570 m

**Représentation :**
```
     A ----80---- B
     |            |
   100|           |120
     |            |
     C ----90---- D
     |            |
   110|           |70
     |            |
     E            E
```

### 6.4. Graphe 4 : Chemin eulérien (P2.2 - 2 sommets impairs)

**Objectif :** Tester le chemin eulérien avec exactement 2 sommets de degré impair.

**Sommets :**
- C : Centre de traitement (degré 3 - impair)
- A : Intersection (degré 1 - impair)
- B, D, E : Intersections (degrés pairs)

**Arêtes (HO1) :**
- C-A : 100 m
- C-B : 80 m
- C-D : 90 m
- B-E : 70 m
- E-D : 60 m

**Vérification :** 
- C : degré 3 (impair)
- A : degré 1 (impair)
- B, D, E : degré 2 (pair)

**Test :** Trouver un chemin eulérien.
- Si on commence par C : C-B-E-D-C-A (ou C-D-E-B-C-A)
- Puis ajouter le plus court chemin de retour de A vers C = 100 m
- Distance totale = somme de toutes les arêtes + distance de retour

### 6.5. Graphe 5 : Postier chinois (P2.3 - cas général)

**Objectif :** Tester le problème du postier chinois avec plusieurs sommets impairs.

**Sommets :**
- C : Centre de traitement (degré 3 - impair)
- A, B, D, E : Intersections
  - A : degré 3 (impair)
  - B : degré 1 (impair)
  - D : degré 3 (impair)
  - E : degré 1 (impair)

**Arêtes (HO1) :**
- C-A : 50 m
- C-B : 60 m
- A-D : 40 m
- A-E : 30 m
- D-E : 20 m

**Vérification :** 4 sommets impairs (C, A, B, D) - nombre pair, donc résolvable.

**Test :** Résoudre le postier chinois.
- Identifier les 4 sommets impairs
- Trouver le couplage parfait de poids minimal
- Dupliquer les arêtes correspondantes
- Appliquer l'algorithme de circuit eulérien

### 6.6. Graphe 6 : Graphe orienté simple (HO2)

**Objectif :** Tester les algorithmes sur un graphe orienté (HO2).

**Sommets :**
- C : Centre de traitement
- A, B, D : Intersections

**Arcs (HO2 - orienté) :**
- C → A : 100 m
- A → B : 80 m
- B → C : 120 m
- C → D : 90 m
- D → A : 70 m

**Test :** 
- Plus court chemin de C vers A (direct : 100 m)
- Plus court chemin de A vers C (A-B-C : 200 m, ou A-D-C si D → C existe)

**Note :** Pour les problèmes eulériens avec HO2, il faut vérifier l'équilibre des degrés entrants/sortants.

### 6.7. Tableau récapitulatif des graphes de test

| Graphe | Nombre sommets | Nombre arêtes/arcs | Problème testé | Hypothèse |
|--------|----------------|-------------------|----------------|-----------|
| G1 | 5 | 6 | Plus court chemin | HO1 |
| G2 | 4 | 6 | Tournée TSP | HO1 |
| G3 | 5 | 6 | Circuit eulérien | HO1 |
| G4 | 5 | 5 | Chemin eulérien | HO1 |
| G5 | 5 | 5 | Postier chinois | HO1 |
| G6 | 4 | 5 | Plus court chemin orienté | HO2 |

---

## 7. Idées d'extensions (facultatif, uniquement en lien avec le Thème 1)

### 7.1. Extensions pour la collecte d'encombrants

- **Contraintes de temps :** Ajouter des fenêtres horaires pour chaque demande (le camion doit passer entre 8h et 12h par exemple)
- **Priorités :** Certaines demandes peuvent être prioritaires et doivent être traitées en premier
- **Capacité du camion :** Limiter le nombre d'encombrants collectés par tournée selon la capacité du véhicule
- **Optimisation multi-objectifs :** Minimiser à la fois la distance ET le temps de parcours

### 7.2. Extensions pour la collecte des poubelles

- **Fréquence de collecte :** Certaines rues nécessitent une collecte plus fréquente (quotidienne vs hebdomadaire)
- **Types de déchets :** Différencier les poubelles selon leur type (ordures ménagères, tri sélectif) et optimiser des tournées séparées
- **Contraintes de circulation :** Certaines rues sont interdites à certaines heures (zones scolaires, marchés)
- **Optimisation avec plusieurs camions :** Étendre au cas où plusieurs camions peuvent intervenir (problème de partitionnement des tournées)

### 7.3. Extensions techniques

- **Visualisation graphique :** Afficher le graphe et les tournées calculées sur une interface graphique simple
- **Import/Export de données :** Charger des graphes réels depuis des fichiers (formats CSV, JSON, etc.)
- **Comparaison d'algorithmes :** Implémenter plusieurs heuristiques pour les tournées et comparer leurs performances
- **Métriques de performance :** Calculer et afficher des statistiques (distance moyenne, temps de calcul, etc.)

### 7.4. Extensions pour la modélisation

- **Gestion des sens uniques complexes :** Modéliser plus finement les rues à plusieurs voies avec des règles de collecte spécifiques
- **Points de collecte multiples :** Gérer le cas où une même rue a plusieurs points de collecte (conteneurs)
- **Graphes dynamiques :** Prendre en compte les modifications temporaires du réseau (travaux, fermetures)

---

## Conclusion

Cette analyse du Thème 1 fournit une base solide pour le développement du projet. Les éléments clés à retenir sont :

1. **Trois problématiques principales :** collecte d'encombrants (individuelle et en tournée), collecte des poubelles (cas eulériens et postier chinois)
2. **Trois hypothèses sur les graphes :** HO1 (non orienté), HO2 (orienté), HO3 (mixte)
3. **Algorithmes à implémenter :** Dijkstra, TSP (heuristiques), Hierholzer, postier chinois
4. **Architecture objet claire :** séparation entre modèle (graphe, sommets, arêtes) et algorithmes

Le travail peut commencer par l'implémentation des structures de données de base (Graphe, Sommet, Arete, Arc), puis progresser vers les algorithmes en commençant par les plus simples (plus court chemin) avant d'aborder les problèmes eulériens.


