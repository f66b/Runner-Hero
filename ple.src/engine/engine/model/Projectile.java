package engine.model;

/**
 * Projectile represents a moving entity that travels in a straight line
 * at a constant velocity until it hits something or leaves the world.
 */
public class Projectile extends Entity {
    private static final double PROJECTILE_SPEED = 10.0; // meters per second
    private double m_lifetime;
    private static final double MAX_LIFETIME = 5.0; // seconds
    
    public Projectile(Model model, double x, double y, double orientation) {
        // Place projectile at grid position based on x,y meters
        super(model, model.metersToGridY(y), model.metersToGridX(x), orientation);
        
        // Set continuous position
        setMetricPosition(x, y);
        
        // Set velocity based on orientation
        setVelocity(PROJECTILE_SPEED);
        
        m_lifetime = 0;
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        
        // Update lifetime
        m_lifetime += deltaTime;
        
        // Remove projectile if it exceeds max lifetime
        if (m_lifetime > MAX_LIFETIME) {
            // Mark for removal (would need to implement in Model)
            setVelocity(0);
        }
    }
    
    public boolean isExpired() {
        return m_lifetime > MAX_LIFETIME;
    }
} 