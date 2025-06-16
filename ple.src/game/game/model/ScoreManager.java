package game.model;

/**
 * Centralises score computation (survival time, coins, multipliers…).
 *
 * Amine – implement methods to add points, apply multipliers and expose the
 * current total. Integrate with HUD to display score each frame.
 */
public class ScoreManager {

    private int m_score = 0;
    private double m_elapsedTime = 0;
    private int m_coinCount = 0;
    private double m_multiplier = 1.0;

    
    /** Adds raw points (e.g., from coins). */
    public void addPoints(int pts) {
        // TODO (Amine): implement and update HUD
    }

    /** Call once per tick to accumulate survival points. */
    public void update(double deltaTime) {
        // TODO (Amine): add time-based score and manage multiplier changes
    }

    public int getScore() {
        return m_score;
    }
} 