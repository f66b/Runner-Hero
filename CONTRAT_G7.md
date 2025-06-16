# GROUPGE G7 : Runner Hero

__Faycal Brahmi__

# GAME

## Logique Générale
Le joueur contrôle un personnage qui court automatiquement vers la droite dans un environnement de type "playground". Le personnage doit éviter des obstacles variés et survivre aux attaques ennemies tout en collectant des ressources.

## Génération aléatoire du Model

* [!] Génération des nouvelles entités qui apparaissent
  Dans l'objet EnvironnementGenerator pour chaque x colonne générer, on cree une entité aleatoirement à travers la methode create entity
## Scrolling avec décalage

* [!] décalage dans le tableau d'une case et génération de la dernière colonne
   décaler toutes les cases à gauche et appeler un objet appelé EnvironnementGenerator qui genère la dérnière colonne
(__explication technique sur le principe de décalage__)

## Grille ou (x,y) metric ? : Imane
   Grille.
* Joueur
  * [x] (x,y) en float et dans une case (col,row)   
  * [x] déplacement fluide
  * [x] collision si deux entités dans la même case

## Pas de FSM, pas de GAL

## Pas de gravité, pas de trous

## Mécaniques de Base

### 1. Mouvement du Joueur : Faycal
- [x] **Course Automatique** : Déplacement horizontal continu vers la droite

- [x] **Saut** : 
  - [x] Activation par touche (flèche haut)
  - Permet de franchir les obstacles bas (rochers)
  * [x] déplacement fluide
  * [] animation (sprite)

- [x] **Glissade** : 
  - [x] Activation par touche (flèche bas)
  - Permet de passer sous les obstacles aériens (branches basses)
  * [x] changement d'aspect
  * [] animation (sprite)

### 2. Système de Santé : Driss
- **Points de Vie (PV)** : 
  - [x] 3 PV maximum
  - [x] Perd 1 PV par projectile ennemi touché
  - [x] Mort instantanée en collision 
     * avec obstacle
     * avec un bot 
  
- **Réparation** :
  - [x] Les points de santé restaurent 1 PV
  - [x] Maximum plafonné à 3 PV

### 3. Ennemis et Dangers Faycal
- **Obstacles Statiques** :
  - [x] Rochers : 1 case (nécessitent un saut)
  - [x] oiseaux : 1 case (nécessitent une glissade)
  - [x] les oiseaux se déplacent  - [x] les oiseaux montent et descendent

- **Bots Tireurs** :
  - [x] Apparaissent à intervalles aléatoires
  - [x] Tirent des projectiles en ligne droite
    * comportement en Java dans le `stunt`
  - [2] Projectiles évitables par saut/glissade
  - [x] 3 impacts = mort du joueur

### 4. Collectables : Rim

- [?] **Pièces (Coins)** :
  - +10 points par pièce collectée
  - Apparaissent aléatoirement dans le niveau

- [x] **Points de Santé** : 
  - [x] Restaurent 1 PV (33% de la santé totale)
  - [x] Affichés comme cœurs

### 5. Système de Score : Amine

- [x] **Score de Survie** : 
  - +1 point par seconde de jeu
  - Multiplicateur x2 après 1 minute

- [?] **Bonus de Collection** :
  - Valeur fixe des pièces (10 pts)

- [?] **Score Total** = (Temps écoulé * multiplicateur) + Pièces collectées

## Conditions de Défaite : Aya
1. [x] Collision avec un obstacle physique ou un bot ou un oiseau
2. [x] Impacts de 3 projectiles ennemis
3. [] Chute dans un précipice


## Règles Spéciales : Driss

- [x] Les entités disparaissent après avoir franchi l'écran
- [x] Glisse tant qu'on appuie sur la flêche du bas et s'arrête instantanément quand on relâche la touche.
- [x] Invincibilité temporaire (0.5s) après prise de dégâts par un projectile ou un oiseau : gestion du temps


# ENGINE

## Model

#### Geometry
  * [] Tore 
  * [x] not Tore
  * [ ] can change de map size (nbcols, nbrows)
  * [ ] can change cell size (en unité métrique)

#### Collisions 
  * [1] entité(s) par cellule  
  * [ ] bounding box

#### Show 
  * [x] birth 
  * [x] health
  * [x] death

#### Adaptability = Dynamic change 
  * [ ] of stunt 
  * [ ] of avatar (changement d'image) 
  * [ ] of bot/FSM

#### Show that actions take time

  * [ ] bots do not think while the stunt acts
  * [x] déplacement fluide : coordination entre l'avatar (la view) et l'entité dans le modèle 

#### Single / multiple actions?

  * [] one action at a time 
    * [ ] compatibility table per entity
  * [!] action abort : 
     * move interrupted by the death of entity 
     * conflict of moving in the same cell
      
## View
  * [ ] view port 
    * [ ] zoom
    * [ ] translate (scolling)
  * [ ] drawing happens in model coordinates
  
#### Two modes
  * [ ] full map fits the canvas
    * [ ] en hauteur
  * [x] cell-size is given pixels

#### Performance (mode debug)

  * [x] show elapsed tick (min, max, moyenne)
  * [x] show elapsed time of a paint (min, max, moyenne)
  * [x] number of FPS (min, max, moyenne)
  * [ ] gestion de la charge
      
#### Bot

 * [x] Bot en Java
 * [ ] Bot en FSM Java
 * [ ] Bot en GAL + parser

### Répartition précise des tâches OO

| Membre | Responsabilité principale | Classe(s) assignée(s) | Détails à développer |
|--------|---------------------------|----------------------|----------------------|
| **Faycal** | Mouvements du joueur + Ennemis/Obstacles | `Player` (mouvements), `Rock`, `Pigeon`, `EnemyShooter`, `EnvironmentGenerator` | Finaliser les animations de saut / glissade, ajuster les vitesses, maintenir la logique de scrolling et de génération aléatoire ; équilibrer la cadence de tir et trajectoires ennemies. |
| **Driss** | Gestion de la santé et dégâts | `HealthPickup` | Implémenter `onPickUp` pour : +1 PV (max 3) ; gérer invincibilité 0,5 s et feedback visuel. |
| **Rim** | Système de collectables / scoring | `Coin` & (coop. avec Amine) | Implémenter `onPickUp` pour ajouter 10 pts via `ScoreManager` puis supprimer la pièce. |
| **Aya** | Conditions de défaite | `DeathZone` | Implémenter `onPlayerCollision` : mise à 0 PV, déclenchement Game-Over. |
| **Imane** | Adaptabilité du modèle | `DynamicModel` | Coder `resize` et `setCellSizeMeters` tout en conservant les entités existantes et informant les vues. |
| **Amine** | Gestion du score global & HUD | `ScoreManager` | Calcul du score temps, multiplicateurs, intégration HUD, prise en compte des `Coin`. |
