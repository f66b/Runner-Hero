package engine.model;

public class Pigeon extends Entity {
    private static final double HORIZONTAL_SPEED = 3.0; // meters per second (slower than projectile)
    private static final double VERTICAL_SPEED = 1.5;   // meters per second vertical movement
    private static final double SWITCH_PERIOD = 1.0;    // seconds before changing vertical direction

    private double m_timeSinceSwitch = 0;
    private boolean m_goingUp = true;

    public Pigeon(Model model, int row, int col) {
        super(model, row, col, 180); // Initially facing left
    }

    @Override
    public void update(double deltaTime) {
        // Horizontal movement from right to left
        double dx = -HORIZONTAL_SPEED * deltaTime;
        // Vertical movement toggles direction periodically
        double dy = (m_goingUp ? -VERTICAL_SPEED : VERTICAL_SPEED) * deltaTime;

        // Use model helper to move in continuous space
        m_model.moveByPixels(this, dx, dy);

        // Update timer and toggle direction if needed
        m_timeSinceSwitch += deltaTime;
        if (m_timeSinceSwitch >= SWITCH_PERIOD) {
            m_timeSinceSwitch = 0;
            m_goingUp = !m_goingUp;
        }
    }
} 