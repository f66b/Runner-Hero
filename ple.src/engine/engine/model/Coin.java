package engine.model;

import game.model.ScoreManager;
import game.model.StuntPlayer;

/**
 * Coin gives score points when collected and then disappears.
 * Can only be collected when player is sliding or running (not jumping).
 */
public class Coin extends Collectable {

    public static final int VALUE = 10;

    public Coin(Model model, int row, int col) {
        super(model, row, col); // Call parent constructor
        // Set orientation and velocity for leftward movement
        face(180); // Face left
        setVelocity(model.getWorldScrollSpeed());
        System.out.println("Coin created at (" + row + ", " + col + ") with speed " + 
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
                    System.out.println("Coin not collected - player is " + getPlayerState(player));
                }
            }
        }
        
        // Remove if moved too far left (off-screen) - more aggressive
        if (getX() < -1.5) {
            m_model.removeEntity(this);
        }
    }
    
    /**
     * Determine if coin can be collected based on player state
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
        // Add VALUE to ScoreManager and remove this entity
        ScoreManager scoreManager = ScoreManager.getInstance();
        scoreManager.addCoin(); // This adds the coin value to the score
        
        // Remove this coin from the model
        m_model.removeEntity(this);
        
        System.out.println("Coin collected! Player was " + getPlayerState(player) + 
                          ". Score increased by " + VALUE);
    }
} 