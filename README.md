# Runner Hero - Side-Scrolling Runner Game

## Project Overview

**Runner Hero** is a side-scrolling endless runner game developed in Java as part of the **GROUPE G7** academic project. The game challenges players to survive as long as possible while automatically running to the right, jumping over and sliding under various obstacles, collecting coins and hearts, and avoiding enemy fire.

### Game Genre
- **Type**: 2D Side-scrolling Endless Runner
- **Platform**: Java Desktop Application
- **Framework**: Custom Object-Oriented Game Engine
- **Graphics**: Grid-based rendering with PNG sprite support

## Game Features

### Core Gameplay Mechanics

#### 🏃‍♂️ **Automatic Movement System**
- Player continuously moves right at increasing speeds
- World scrolls from right to left creating infinite gameplay
- Background parallax scrolling for visual depth
- Speed increases with score multipliers over time

#### ⚡ **Player Controls & Actions**
- **Jump** (UP Arrow / SPACE): Leap over low obstacles like rocks
- **Slide** (DOWN Arrow): Duck under flying enemies like pigeons
- **Smooth Animations**: Fluid transitions between running, jumping, and sliding
- **Strategic Movement**: Players must time actions precisely to avoid obstacles

#### 💖 **Health & Survival System**
- **3 Health Points Maximum**: Player starts with full health
- **Damage System**: Lose 1 HP from projectiles, instant death from obstacles/enemies
- **Invincibility Frames**: 0.5-second protection after taking damage
- **Health Pickups**: Restore 1 HP (up to maximum)
- **Visual Indicators**: Heart display and invincibility effects

#### 🎯 **Scoring System**
- **Survival Points**: +1 point per second survived
- **Time Multipliers**: x2, x3, x4... every 10 seconds of survival
- **Coin Collection**: +10 points per coin collected
- **High Score Tracking**: Persistent leaderboard across game sessions
- **Strategic Collection**: Coins/health can only be collected while running or sliding (not jumping)

### 🎮 **Game States & UI**

#### **Start Screen**
- Professional title screen with "RUNNER HERO" branding
- Play button to begin the game
- Control instructions display
- Clean, modern dark theme UI

#### **Game HUD**
- **Health Display**: Visual heart indicators (♥♥♥)
- **Multiplier Indicator**: Shows current score multiplier wheg<g<n active
- **Score Counter**: Real-time score updates
- **Timer**: MM:SS format survival time
- **Invincibility Status**: Special indicator during damage immunity

#### **Game Over Screen**
- Final score display with dramatic presentation
- High scores leaderboard (top scores across all games)
- Replay button for immediate restart
- Game statistics and achievement tracking

### 🎭 **Enemies & Obstacles**

#### **Static Obstacles**
- **Rocks**: Ground-level obstacles requiring jumps to clear
- **Death Zones**: Invisible instant-kill areas (pits, walls)

#### **Dynamic Enemies**
- **Pigeons**: Flying enemies that move up and down, require sliding to avoid
- **Enemy Shooters**: Stationary turrets that fire projectiles at the player
- **Projectiles**: Bullets fired by enemy shooters, cause 1 HP damage

#### **Collectibles**
- **Coins**: Golden collectibles worth 10 points each
- **Health Pickups**: Red cross items that restore 1 HP
- **Strategic Gameplay**: Collectibles can only be gathered while running/sliding

### 🛠 **Technical Features**

#### **Advanced Environment Generation**
- **Procedural Content**: Algorithmic obstacle and collectible placement
- **Weighted Probability System**: Balanced entity distribution
- **Smart Enemy Spacing**: Prevents overwhelming consecutive enemy placement
- **Performance Optimization**: Efficient entity lifecycle management

#### **Collision Detection**
- **Distance-based Detection**: Precise circular collision areas
- **Multi-state Player Logic**: Different collision behaviors for jumping/sliding/running
- **Entity Cleanup**: Automatic removal of off-screen entities

#### **Animation & Visual Effects**
- **State-based Player Animation**: Running, jumping, sliding sprites
- **Parallax Background Scrolling**: Multiple scroll speeds for depth
- **Visual Feedback**: Invincibility effects, damage indicators
- **PNG Sprite Support**: High-quality image rendering with fallback shapes

## Team Members & Responsibilities

### **👥 Team Composition - GROUPE G7**

The project was developed collaboratively with each team member taking ownership of specific game systems while contributing to the overall architecture:

| Team Member | Primary Responsibility | Key Contributions |
|-------------|----------------------|-------------------|
| **Faycal Brahmi** | **Game Movement & Obstacles** | Player jump/slide mechanics, Rock/Pigeon/EnemyShooter implementation, EnvironmentGenerator random spawning system, scrolling physics |
| **Driss** | **Health System & Game Rules** | HealthPickup implementation, invincibility system, damage mechanics, special game rules (sliding behavior, entity cleanup) |
| **Rime** | **Collectibles & Scoring Integration** | Coin collection system, ScoreManager integration, collectible pickup mechanics, strategic collection rules |
| **Aya** | **Defeat Conditions** | DeathZone implementation, game over triggers, failure state management, collision death logic |
| **Imane** | **Model Adaptability** | DynamicModel resize/configuration system, grid-based architecture, world size management, model flexibility |
| **Amine** | **Score Management & HUD** | ScoreManager singleton, time-based scoring, multiplier system, HUD integration, UI display coordination |

### **🤝 Collaborative Development Approach**

#### **Equal Contribution Philosophy**
- **Shared Ownership**: While each member had primary responsibilities, all contributed to design decisions and implementation
- **Cross-functional Support**: Team members assisted each other across different system boundaries
- **Code Integration**: All team members participated in integrating their components with the overall architecture
- **Quality Assurance**: Collective code review and testing by all team members

#### **Development Environment**
- **Primary Development**: Work was primarily conducted on **Amine's development machine** for consistency
- **Collaborative Sessions**: All team members participated in pair programming and group problem-solving sessions
- **Knowledge Sharing**: Regular team meetings ensured all members understood the complete system
- **Version Control**: All team members contributed to code and documentation

#### **Workload Distribution Strategy**
- **Component-based Division**: Each core game system was assigned to leverage individual strengths
- **Interface-driven Development**: Clear APIs between components allowed parallel development
- **Integration Phases**: Structured merge points where all components were integrated together
- **Testing & Debugging**: Collective responsibility for finding and fixing bugs across all systems

#### **Team Collaboration Evidence**
- **Design Decisions**: All major architectural choices were made collectively
- **Bug Resolution**: Cross-component issues were solved through team collaboration
- **Feature Enhancement**: Improvements and optimizations involved input from all team members
- **Documentation**: This README and all project documentation reflects the entire team's understanding

## Technical Architecture

### **🏗 System Architecture**

#### **Model-View-Controller Pattern**
```
Game (Main Controller)
├── Model (Game State)
│   ├── Player (Health, Position, Actions)
│   ├── Entities (Obstacles, Enemies, Collectibles)
│   └── DynamicModel (World Management)
├── View (Rendering)
│   ├── View0 (Main Game Renderer)
│   ├── Avatar System (Entity Visualization)
│   └── ImageLoader (Asset Management)
└── Controller (Input Handling)
    ├── Controller0 (Game Controls)
    └── UIManager (Menu Systems)
```

#### **Key Subsystems**
- **ScoreManager**: Singleton pattern for centralized scoring
- **EnvironmentGenerator**: Procedural content generation
- **StuntPlayer**: Player action state machine
- **UIManager**: Game state and menu management
- **Ticker**: 60 FPS game loop with delta time

### **🔧 Engine Features**
- **Grid-based Coordinates**: Discrete cell positioning with metric conversion
- **Smooth Movement**: Continuous position updates with velocity-based motion
- **Entity Lifecycle**: Automatic creation, updating, and cleanup
- **Event-driven Architecture**: Asynchronous input handling and game events


### **Game Assets**
- **Image Resources**: Place PNG files in `ple.src/resources/images/`
- **Required Images**: playerposition1.png, playerposition2.png, playerjump.png, playerglisse.png, rock.png, pigeon.png, enemyshooter.png, projectile.png, coin.png, health.png, backround.png
- **Fallback System**: Game runs with geometric shapes if images are missing

## Development Process

### **🚀 Project Evolution**

#### **Phase 1: Core Architecture**
- Established MVC pattern with custom game engine
- Implemented basic entity system and grid-based world
- Created player movement and basic collision detection

#### **Phase 2: Game Mechanics**
- Added health system and invincibility frames
- Implemented jump and slide mechanics with smooth animations
- Created obstacle and enemy behavior systems

#### **Phase 3: Scoring & Progression**
- Developed comprehensive scoring system with multipliers
- Added coin collection and health pickup mechanics
- Implemented high score persistence and leaderboards

#### **Phase 4: UI & Polish**
- Created professional menu system with multiple game states
- Added HUD with health, score, and time display
- Implemented game over screen with replay functionality

#### **Phase 5: Optimization & Debugging**
- Performance optimization for entity management
- Comprehensive debugging system with console output
- Bug fixes for animation states and collision detection

### **🔍 Quality Assurance**
- **Comprehensive Testing**: All team members tested each component thoroughly
- **Edge Case Handling**: Robust error handling and fallback systems
- **Performance Monitoring**: Debug output for frame rates and entity counts
- **User Experience**: Smooth gameplay with responsive controls

## Contract Implementation Status

### **✅ Completed Features**
- ✅ Automatic rightward movement with scrolling world
- ✅ Jump (UP/SPACE) and slide (DOWN) mechanics with smooth animations
- ✅ Health system (3 HP max, projectile damage, instant death obstacles)
- ✅ Enemies: Rocks (jump), Pigeons (slide), Enemy Shooters (projectiles)
- ✅ Collectibles: Coins (+10 points), Health Pickups (+1 HP)
- ✅ Time-based scoring with multipliers after survival milestones
- ✅ Game states: START screen, RUNNING game, GAME_OVER with high scores
- ✅ Strategic collection (only when running/sliding, not jumping)
- ✅ Invincibility frames (0.5s) after projectile damage
- ✅ Slide duration mechanics (slide while key pressed, max 2 seconds)
- ✅ Entity cleanup and efficient memory management
- ✅ Professional UI with buttons and game state management

### **🎮 Gameplay Experience**
The final game delivers a polished, challenging, and engaging endless runner experience that meets all contract specifications while providing smooth gameplay, professional presentation, and robust technical implementation.


### **contribution percentages**
 - **Faycal Brahmi**  17%
 - **Mohamed Amine Kacimi** 17%
 - **Driss Benabdeslam** 16.5%
 - **Gaidi Imane** 16.5%
 - **Elouakyly Rime** 16.5%
 - **Aya Elguermouh** 16.5%
 

---

## Conclusion

**Runner Hero** represents a successful collaborative software development project that demonstrates:

- **Technical Excellence**: A well-architected, extensible game engine
- **Team Collaboration**: Effective division of labor with equal contribution from all members
- **Quality Implementation**: Robust, tested, and polished final product
- **Professional Presentation**: Complete documentation and user-friendly interface

The project showcases the team's ability to work together effectively, with each member's expertise contributing to a cohesive and high-quality final product. While the development was primarily conducted on Amine's machine for technical consistency, the intellectual contribution, design decisions, problem-solving, and implementation work was truly collaborative, with all team members playing equally important roles in the project's success.

---

**GROUPE G7 - Runner Hero Development Team**  
*Collaborative Java Game Development Project* 