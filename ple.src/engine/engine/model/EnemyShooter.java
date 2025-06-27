package engine.model;

public class EnemyShooter extends Entity {
    private static final double FIRE_INTERVAL = 1.0; // seconds between shots
    private static final double OFFSET_Y_METERS = 1.0; // vertical offset for high/low shots

    private double m_timeSinceLastShot = 0;
    private boolean m_nextHigh = true;

    public EnemyShooter(Model model, int row, int col) {
        super(model, row, col, 180); // Facing left for leftward movement
        // Set velocity to current world scroll speed
        setVelocity(model.getWorldScrollSpeed());
        System.out.println("EnemyShooter created at (" + row + ", " + col + ") with speed " + 
                          String.format("%.1f", model.getWorldScrollSpeed()) + " m/s");
    }

    @Override
    public void update(double deltaTime) {
        // Update speed to match current world scroll speed
        setVelocity(m_model.getWorldScrollSpeed());
        
        // Call parent update for automatic leftward movement
        super.update(deltaTime);
        
        // Check collision with player using distance-based detection
        Player player = m_model.player();
        if (player != null) {
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance < 0.6) { // 0.6 meter collision radius for enemy shooters
                onPlayerCollision(player);
            }
        }
        
        // Handle shooting timer
        m_timeSinceLastShot += deltaTime;

        if (m_timeSinceLastShot >= FIRE_INTERVAL) {
            m_timeSinceLastShot -= FIRE_INTERVAL;
            fireProjectile();
        }
        
        // Remove if moved too far left (off-screen) - more aggressive
        if (getX() < -1.5) {
            m_model.removeEntity(this);
        }
    }

    private void fireProjectile() {
        double x = getX();
        double y = getY();
        boolean isHighShot;

        // Apply vertical offset for alternating high/low shots
        if (m_nextHigh) {
            y -= OFFSET_Y_METERS; // Shot above player level
            isHighShot = true;
        } else {
            y += OFFSET_Y_METERS; // Shot below player level
            isHighShot = false;
        }
        m_nextHigh = !m_nextHigh;

        // Spawn new projectile moving leftwards (towards player) with shot type
        new Projectile(m_model, x, y, 180, isHighShot);
        System.out.println("EnemyShooter fired " + (isHighShot ? "HIGH" : "LOW") + " projectile at " + x + "," + y);
    }
    
    /**
     * Handle collision with player - instant death
     */
    public void onPlayerCollision(Player player) {
        System.out.println("Player collided with EnemyShooter!");
        player.die();
    }
} 