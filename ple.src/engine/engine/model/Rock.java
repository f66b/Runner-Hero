package engine.model;

public class Rock extends Entity {

    public Rock(Model model, int row, int col) {
        super(model, row, col, 180); // Facing left for leftward movement
        // Set initial velocity from model's current scroll speed
        setVelocity(model.getWorldScrollSpeed());
        System.out.println("Rock created at (" + row + ", " + col + ") with speed " + 
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
            
            if (distance < 0.6) { // 0.6 meter collision radius for rocks
                onPlayerCollision(player);
            }
        }
        
        // Remove if moved too far left (off-screen) - more aggressive
        if (getX() < -1.5) {
            m_model.removeEntity(this);
        }
    }

    /**
     * Handle collision with player - instant death
     */
    public void onPlayerCollision(Player player) {
        System.out.println("Player collided with Rock!");
        player.die();
    }
} 