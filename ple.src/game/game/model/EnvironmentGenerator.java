package game.model;

import engine.model.Model;
import engine.model.Rock;
import engine.model.Pigeon;
import engine.model.EnemyShooter;
import engine.model.Coin;
import engine.model.HealthPickup;

import java.util.Random;

public class EnvironmentGenerator {
    private final Model m_model;
    private int m_columnsGenerated = 0;
    private final Random m_random = new Random();
    
    // Player altitude - all entities spawn at this level
    private static final int PLAYER_ROW = 10;
    // Note: Pigeons spawn at player level for collision, but render above visually
    
    // Generation probabilities - single entity per column (reduced frequency)
    private static final double GENERATION_PROBABILITY = 0.15; // Reduced from 0.3 to 0.15 (15% chance)
    
    // Entity type weights (higher = more likely)
    private static final int ROCK_WEIGHT = 25;
    private static final int ENEMY_SHOOTER_WEIGHT = 20;
    private static final int PIGEON_WEIGHT = 15;
    private static final int COIN_WEIGHT = 30;
    private static final int HEALTH_PICKUP_WEIGHT = 10;
    private static final int TOTAL_WEIGHT = ROCK_WEIGHT + ENEMY_SHOOTER_WEIGHT + PIGEON_WEIGHT + COIN_WEIGHT + HEALTH_PICKUP_WEIGHT;
    
    // Generation spacing - increased for even less frequent generation
    private static final int MIN_COLUMNS_BETWEEN_GENERATION = 8; // Increased from 6 to 8 columns
    private int m_columnsSinceLastGeneration = 0;
    
    // Track last generated entity type to prevent successive EnemyShooters
    private String m_lastGeneratedEntityType = null;
    
    // Track if the previous column had any generation (for EnemyShooter spacing)
    private boolean m_previousColumnHadGeneration = false;

    public EnvironmentGenerator(Model model) {
        this.m_model = model;
    }

    /**
     * Generate the next column at the rightmost edge of the world.
     * Creates only ONE entity per column (obstacle OR collectable, never both).
     * Uses increased spacing for less frequent generation.
     */
    public void generateNextColumn() {
        int lastCol = m_model.ncols() - 1;
        int rows = m_model.nrows();

        // Check if we should skip this generation for spacing
        if (m_columnsSinceLastGeneration < MIN_COLUMNS_BETWEEN_GENERATION) {
            m_columnsSinceLastGeneration++;
            System.out.println("Skipping column " + m_columnsGenerated + " for spacing (need " + 
                             MIN_COLUMNS_BETWEEN_GENERATION + " columns between generations)");
            m_columnsGenerated++;
            return;
        }
        
        System.out.println("=== GENERATING COLUMN " + m_columnsGenerated + " ===");
        System.out.println("Target column: " + lastCol + ", World size: " + m_model.ncols() + "x" + rows);
        System.out.println("Player altitude: " + PLAYER_ROW + ", Pigeon spawn altitude: " + PLAYER_ROW);
        System.out.println("Previous column had generation: " + m_previousColumnHadGeneration + 
                          " (affects EnemyShooter spawning)");

        boolean generatedSomething = false;

        // Single roll to determine if we generate anything at all
        double generationRoll = m_random.nextDouble();
        System.out.println("Generation roll: " + String.format("%.3f", generationRoll) + " (need < " + GENERATION_PROBABILITY + ")");
        
        if (generationRoll < GENERATION_PROBABILITY) {
            // Determine what type of entity to generate using weighted random selection
            // Regenerate if we get successive EnemyShooters
            String entityType = "";
            int attempts = 0;
            int maxAttempts = 10; // Prevent infinite loops
            
            do {
                int randomWeight = m_random.nextInt(TOTAL_WEIGHT);
                attempts++;
                
                if (randomWeight < ROCK_WEIGHT) {
                    entityType = "Rock";
                } else if (randomWeight < ROCK_WEIGHT + ENEMY_SHOOTER_WEIGHT) {
                    entityType = "EnemyShooter";
                } else if (randomWeight < ROCK_WEIGHT + ENEMY_SHOOTER_WEIGHT + PIGEON_WEIGHT) {
                    entityType = "Pigeon";
                } else if (randomWeight < ROCK_WEIGHT + ENEMY_SHOOTER_WEIGHT + PIGEON_WEIGHT + COIN_WEIGHT) {
                    entityType = "Coin";
                } else {
                    entityType = "HealthPickup";
                }
                
                // Check EnemyShooter generation rules:
                // 1. No successive EnemyShooters
                // 2. EnemyShooters only after empty columns (to give player time)
                if (entityType.equals("EnemyShooter")) {
                    if ("EnemyShooter".equals(m_lastGeneratedEntityType)) {
                        System.out.println("Prevented successive EnemyShooter generation (attempt " + attempts + ")");
                        if (attempts >= maxAttempts) {
                            entityType = "Rock"; // Force Rock as fallback
                            System.out.println("Max attempts reached, forcing Rock generation instead");
                            break;
                        }
                        // Continue loop to regenerate
                    } else if (m_previousColumnHadGeneration) {
                        System.out.println("Prevented EnemyShooter after non-empty column - need gap for player (attempt " + attempts + ")");
                        if (attempts >= maxAttempts) {
                            entityType = "Rock"; // Force Rock as fallback
                            System.out.println("Max attempts reached, forcing Rock generation instead");
                            break;
                        }
                        // Continue loop to regenerate
                    } else {
                        // Valid EnemyShooter generation (after empty column)
                        System.out.println("EnemyShooter allowed after empty column - player has time to react");
                        break;
                    }
                } else {
                    // Non-EnemyShooter entity type, exit loop
                    break;
                }
            } while (attempts < maxAttempts);
            
            // Generate the selected entity
            if (entityType.equals("Rock")) {
                new Rock(m_model, PLAYER_ROW, lastCol);
                generatedSomething = true;
            } else if (entityType.equals("EnemyShooter")) {
                new EnemyShooter(m_model, PLAYER_ROW, lastCol);
                generatedSomething = true;
            } else if (entityType.equals("Pigeon")) {
                new Pigeon(m_model, PLAYER_ROW, lastCol);
                generatedSomething = true;
            } else if (entityType.equals("Coin")) {
                new Coin(m_model, PLAYER_ROW, lastCol);
                generatedSomething = true;
            } else if (entityType.equals("HealthPickup")) {
                new HealthPickup(m_model, PLAYER_ROW, lastCol);
                generatedSomething = true;
            }
            
            // Update last generated entity type
            if (generatedSomething) {
                m_lastGeneratedEntityType = entityType;
            }
            
            int spawnRow = PLAYER_ROW;
            if (entityType.equals("Pigeon")) {
                spawnRow = PLAYER_ROW;
            } else if (entityType.equals("Coin") || entityType.equals("HealthPickup")) {
                spawnRow = PLAYER_ROW;
            }
            
            System.out.println("Generated " + entityType + " at altitude (" + spawnRow + ", " + lastCol + ")");
            System.out.println("Previous entity: " + (m_lastGeneratedEntityType != null ? m_lastGeneratedEntityType : "None") + 
                             " -> Current entity: " + entityType);
            if (attempts > 1) {
                System.out.println("Required " + attempts + " attempts to avoid successive EnemyShooters");
            }
        } else {
            System.out.println("No entity generated this column");
            // Don't reset m_lastGeneratedEntityType when nothing is generated
            // This maintains the prevention of successive EnemyShooters across empty columns
        }

        // Reset spacing counter if we generated something, otherwise increment it
        if (generatedSomething) {
            m_columnsSinceLastGeneration = 0;
        } else {
            m_columnsSinceLastGeneration++;
        }
        
        // Update tracking for next column's EnemyShooter generation rules
        m_previousColumnHadGeneration = generatedSomething;

        m_columnsGenerated++;
        System.out.println("Column " + (m_columnsGenerated - 1) + " complete. Generated something: " + generatedSomething);
        System.out.println("Columns since last generation: " + m_columnsSinceLastGeneration);
        System.out.println("Total columns generated: " + m_columnsGenerated);
        System.out.println("=== END COLUMN GENERATION ===");
    }
} 