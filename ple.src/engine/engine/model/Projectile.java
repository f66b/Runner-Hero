package engine.model;

import game.model.StuntPlayer;

/**
 * Projectile represents a moving entity that travels in a straight line
 * at a constant velocity until it hits something or leaves the world.
 * Projectiles have strategic collision detection based on player state.
 */
public class Projectile extends Entity {
    private static final double SPEED_MULTIPLIER = 2.0; // Projectiles move twice as fast as world scroll
    private double m_lifetime;
    private static final double MAX_LIFETIME = 5.0; // seconds
    private boolean m_isHighShot; // true if shot above player level, false if below
    
    public Projectile(Model model, double x, double y, double orientation, boolean isHighShot) {
        // Place projectile at grid position based on x,y meters
        super(model, model.metersToGridY(y), model.metersToGridX(x), orientation);
        
        // Set continuous position
        setMetricPosition(x, y);
        
        // Set velocity based on world scroll speed (projectiles are faster)
        setVelocity(model.getWorldScrollSpeed() * SPEED_MULTIPLIER);
        
        m_lifetime = 0;
        m_isHighShot = isHighShot;
        
        System.out.println("Projectile created at (" + x + "," + y + ") facing " + orientation + " degrees, " + 
                          (isHighShot ? "HIGH shot" : "LOW shot") + ", speed " + 
                          String.format("%.1f", model.getWorldScrollSpeed() * SPEED_MULTIPLIER) + " m/s");
    }
    
    // Legacy constructor for compatibility
    public Projectile(Model model, double x, double y, double orientation) {
        this(model, x, y, orientation, false); // Default to low shot
    }
    
    @Override
    public void update(double deltaTime) {
        // Update speed to match current world scroll speed (projectiles are faster)
        setVelocity(m_model.getWorldScrollSpeed() * SPEED_MULTIPLIER);
        
        // Call parent update for automatic movement based on velocity and orientation
        super.update(deltaTime);
        
        // Check collision with player using strategic detection
        Player player = m_model.player();
        if (player != null) {
            // Check distance-based collision (more accurate than grid-based)
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            
         // Calculate dynamic collision radius based on cell size (allows ±1 cell vertical offset)
            double collisionRadius = m_model.getCellSizeMeters() * 1.2; // ~1.2m for 1m cells

            if (distance < collisionRadius) {
                // Strategic collision detection based on player state
                if (shouldHitPlayer(player)) {
                    onPlayerCollision(player);
                    return; // Exit early if collision occurred
                } else {
                    System.out.println("Projectile avoided! Player " + getPlayerState(player) + 
                                     ", projectile is " + (m_isHighShot ? "HIGH" : "LOW"));
                }
            }
        }
        
        // Update lifetime
        m_lifetime += deltaTime;
        
        // Remove projectile if it exceeds max lifetime or goes off-screen - more aggressive
        if (m_lifetime > MAX_LIFETIME || getX() < -1.5 || getX() > m_model.getWorldWidthMeters() + 2.0) {
            m_model.removeEntity(this);
        }
    }
    
    /**
     * Determine if projectile should hit player based on strategic rules:
     * - When player is RUNNING: vulnerable to BOTH high and low projectiles
     * - When player is JUMPING: vulnerable to high projectiles, avoids low projectiles
     * - When player is SLIDING: vulnerable to low projectiles, avoids high projectiles
     */
    private boolean shouldHitPlayer(Player player) {
        if (player.stunt instanceof StuntPlayer) {
            StuntPlayer stunt = (StuntPlayer) player.stunt;
            
            boolean isJumping = stunt.isJumping();
            boolean isSliding = stunt.isSliding();
            boolean isRunning = !isJumping && !isSliding; // Running = neither jumping nor sliding
            
            // Debug output to trace the collision logic
            System.out.println("PROJECTILE COLLISION CHECK:");
            System.out.println("  Player stunt type: " + player.stunt.getClass().getSimpleName());
            System.out.println("  isJumping: " + isJumping);
            System.out.println("  isSliding: " + isSliding);
            System.out.println("  isRunning: " + isRunning);
            System.out.println("  Projectile type: " + (m_isHighShot ? "HIGH" : "LOW"));
            
            if (isRunning) {
                // Running is most dangerous - hit by both high and low projectiles
                System.out.println("  RESULT: HIT (running = vulnerable to both)");
                return true;
            } else if (m_isHighShot) {
                // High projectile: hurts when jumping, avoided when sliding
                boolean willHit = isJumping;
                System.out.println("  RESULT: " + (willHit ? "HIT" : "AVOID") + " (high projectile vs " + (isJumping ? "jumping" : "sliding") + ")");
                return willHit;
            } else {
                // Low projectile: hurts when sliding, avoided when jumping
                boolean willHit = isSliding;
                System.out.println("  RESULT: " + (willHit ? "HIT" : "AVOID") + " (low projectile vs " + (isSliding ? "sliding" : "jumping") + ")");
                return willHit;
            }
        }
        
        // If no stunt or wrong type, default behavior (always hit)
        System.out.println("PROJECTILE COLLISION CHECK: No StuntPlayer found - default hit");
        System.out.println("  Player stunt: " + (player.stunt != null ? player.stunt.getClass().getSimpleName() : "null"));
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
     * Handle collision with player - deal damage
     */
    public void onPlayerCollision(Player player) {
        String playerState = getPlayerState(player);
        String hitReason = "";
        
        if (playerState.equals("RUNNING")) {
            hitReason = " (running = vulnerable to both projectiles)";
        } else if (m_isHighShot && playerState.equals("JUMPING")) {
            hitReason = " (jumping into high projectile)";
        } else if (!m_isHighShot && playerState.equals("SLIDING")) {
            hitReason = " (sliding into low projectile)";
        }
        
        System.out.println("Projectile hit player! Player was " + playerState + 
                          ", projectile was " + (m_isHighShot ? "HIGH" : "LOW") + hitReason);
        player.takeDamage(1); // Deal 1 damage
        m_model.removeEntity(this); // Remove projectile after hit
    }
    
    public boolean isExpired() {
        return m_lifetime > MAX_LIFETIME;
    }
    
    public boolean isHighShot() {
        return m_isHighShot;
    }
} 