package engine.model;

import game.model.StuntPlayer;

public class Pigeon extends Entity {
    private static final int PLAYER_ROW = 10; // Player's altitude - pigeon stays at this level for collision

    public Pigeon(Model model, int row, int col) {
        super(model, row, col, 180); // Initially facing left for leftward movement
        // Set velocity to current world scroll speed
        setVelocity(model.getWorldScrollSpeed());
        
        System.out.println("Pigeon created at (" + row + ", " + col + ") at player altitude for collision, " +
                          "speed " + String.format("%.1f", model.getWorldScrollSpeed()) + " m/s");
    }

    @Override
    public void update(double deltaTime) {
        // Update speed to match current world scroll speed
        setVelocity(m_model.getWorldScrollSpeed());
        
        // Call parent update for automatic leftward movement
        super.update(deltaTime);
        
        // Check collision with player using strategic detection with multiple collision zones
        Player player = m_model.player();
        if (player != null) {
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            // Ground-level collision (slide to avoid)
            boolean groundCollision = distance < 0.5; // 0.5 meter collision radius at player level
            
            // Jump collision zone - check if player is jumping and in the air above pigeon
            boolean jumpCollision = false;
            if (player.stunt instanceof StuntPlayer) {
                StuntPlayer stunt = (StuntPlayer) player.stunt;
                if (stunt.isJumping()) {
                    // Calculate jump collision zone (above the pigeon's ground position)
                    double jumpZoneY = getY() - (2.0 * m_model.getCellSizeMeters()); // 2 cells above pigeon
                    double jumpDy = player.getY() - jumpZoneY;
                    double jumpDistance = Math.sqrt(dx * dx + jumpDy * jumpDy);
                    jumpCollision = jumpDistance < 0.8; // Larger collision radius for jump zone
                }
            }
            
            if (groundCollision || jumpCollision) {
                // Strategic collision detection based on player state
                if (shouldHitPlayer(player)) {
                    if (jumpCollision) {
                        System.out.println("Player jumped into pigeon! Jump collision detected.");
                    }
                    onPlayerCollision(player);
                } else {
                    System.out.println("Pigeon avoided! Player " + getPlayerState(player) + " (sliding)");
                }
            }
        }
        
        // Remove if moved too far left (off-screen) - more aggressive
        if (getX() < -1.5) {
            m_model.removeEntity(this);
        }
    }
    
    /**
     * Determine if pigeon should hit player based on strategic rules:
     * - Pigeon always hurts player when running or jumping
     * - Pigeon only avoids player when sliding
     */
    private boolean shouldHitPlayer(Player player) {
        if (player.stunt instanceof StuntPlayer) {
            StuntPlayer stunt = (StuntPlayer) player.stunt;
            
            // Pigeon hurts player unless sliding
            return !stunt.isSliding();
        }
        
        // If no stunt or wrong type, default behavior (always hit)
        return true;
    }
    
    /**
     * Get player state for debugging
     */
    private String getPlayerState(Player player) {
        if (player.stunt instanceof StuntPlayer) {
            StuntPlayer stunt = (StuntPlayer) player.stunt;
            if (stunt.isJumping()) return "JUMPING";
            if (stunt.isSliding()) return "SLIDING";
            return "RUNNING";
        }
        return "UNKNOWN";
    }

    /**
     * Handle collision with player
     */
    public void onPlayerCollision(Player player) {
        // Pigeon causes instant death on collision (like an obstacle)
        System.out.println("Player hit by pigeon! Player was " + getPlayerState(player) + 
                          ", pigeon was at player altitude");
        player.die();
    }
} 