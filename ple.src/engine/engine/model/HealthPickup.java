package engine.model;

import game.model.StuntPlayer;

/**
 * HealthPickup restores one life point (up to the player maximum) when collected.
 * Can only be collected when player is sliding or running (not jumping).
 */
public class HealthPickup extends Collectable {

    private static final int HEAL_AMOUNT = 1;

    public HealthPickup(Model model, int row, int col) {
        super(model, row, col); // Call parent constructor
        // Set orientation and velocity for leftward movement
        face(180); // Face left
        setVelocity(model.getWorldScrollSpeed());
        System.out.println("HealthPickup created at (" + row + ", " + col + ") with speed " + 
                          String.format("%.1f", model.getWorldScrollSpeed()) + " m/s");
    }

    @Override
    public void update(double deltaTime) {
        // Update speed to match current world scroll speed
        setVelocity(m_model.getWorldScrollSpeed());
        
        // Call parent update for automatic leftward movement
        super.update(deltaTime);
        
        // Check strategic collection with player
        Player player = m_model.player();
        if (player != null) {
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance < 0.6) { // 0.6 meter collection radius
                // Strategic collection: only when sliding or running, not jumping
                if (canBeCollected(player)) {
                    onPickUp(player);
                } else {
                    System.out.println("HealthPickup not collected - player is " + getPlayerState(player));
                }
            }
        }
        
        // Remove if moved too far left (off-screen) - more aggressive
        if (getX() < -1.5) {
            m_model.removeEntity(this);
        }
    }
    
    /**
     * Determine if health pickup can be collected based on player state
     * Collectables can be collected when sliding or running, not when jumping
     */
    private boolean canBeCollected(Player player) {
        if (player.stunt instanceof StuntPlayer) {
            StuntPlayer stunt = (StuntPlayer) player.stunt;
            // Can collect when sliding or running, but not when jumping
            return !stunt.isJumping();
        }
        
        // If no stunt or wrong type, default behavior (always collect)
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

    @Override
    public void onPickUp(Player player) {
        // Heal the player by 1 HP (up to maximum 3 HP)
        int oldHealth = player.getHealth();
        player.heal(HEAL_AMOUNT);
        int newHealth = player.getHealth();
        
        // Only remove if healing was successful (health actually increased)
        if (newHealth > oldHealth) {
            m_model.removeEntity(this);
            System.out.println("HealthPickup collected! Player was " + getPlayerState(player) + 
                              ". Health: " + newHealth + "/" + player.getMaxHealth());
        } else {
            System.out.println("HealthPickup ignored - player already at maximum health");
        }
    }
} 