package engine.model;

public class EnemyShooter extends Entity {
    private static final double FIRE_INTERVAL = 1.0; // seconds between shots
    private static final double OFFSET_Y_METERS = 1.0; // vertical offset for high/low shots

    private double m_timeSinceLastShot = 0;
    private boolean m_nextHigh = true;

    public EnemyShooter(Model model, int row, int col) {
        super(model, row, col, 180); // Facing left
    }

    @Override
    public void update(double deltaTime) {
        // Enemy is static, but still handle shooting timer
        m_timeSinceLastShot += deltaTime;

        if (m_timeSinceLastShot >= FIRE_INTERVAL) {
            m_timeSinceLastShot -= FIRE_INTERVAL;
            fireProjectile();
        }
    }

    private void fireProjectile() {
        double x = getX();
        double y = getY();

        // Apply vertical offset for alternating high/low shots
        if (m_nextHigh) {
            y -= OFFSET_Y_METERS;
        } else {
            y += OFFSET_Y_METERS;
        }
        m_nextHigh = !m_nextHigh;

        // Spawn new projectile moving leftwards
        new Projectile(m_model, x, y, 180);
    }
} 