package game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Centralises score computation (survival time, coins, multipliers…).
 *
 * Amine – implement methods to add points, apply multipliers and expose the
 * current total. Integrate with HUD to display score each frame.
 */
public class ScoreManager {

    private static ScoreManager instance;
    
    private int m_score = 0;
    private double m_elapsedTime = 0;
    private double m_lastScoredSecond = 0; // Track when we last awarded survival points
    private int m_coinCount = 0;
    private double m_multiplier = 1.0;
    private static final double MULTIPLIER_TIME_THRESHOLD = 60.0; // 60 seconds for next multiplier level
    private static final int COIN_VALUE = 10;
    private static final int SURVIVAL_POINTS_PER_SECOND = 1; // 1 point per second
    
    // High score system
    private static final List<Integer> scores = new ArrayList<>();
    
    public ScoreManager() {
        instance = this;
    }
    
    /**
     * Get singleton instance
     */
    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }
    
    /** 
     * Adds raw points (e.g., from coins). 
     */
    public void addPoints(int pts) {
        m_score += pts;
        System.out.println("Points added: " + pts + ". Total score: " + m_score);
    }
    
    /**
     * Add points from coin collection
     */
    public void addCoin() {
        m_coinCount++;
        addPoints(COIN_VALUE);
        System.out.println("Coin collected! Total coins: " + m_coinCount);
    }
    
    /** 
     * Call once per tick to accumulate survival points. 
     */
    public void update(double deltaTime) {
        m_elapsedTime += deltaTime;
        
        // Calculate current multiplier level based on elapsed time
        // Every 10 seconds increases multiplier by 1 (x1, x2, x3, etc.)
        double newMultiplier = 1.0 + Math.floor(m_elapsedTime / MULTIPLIER_TIME_THRESHOLD);
        
        // Check if multiplier increased
        if (newMultiplier > m_multiplier) {
            m_multiplier = newMultiplier;
            System.out.println("Multiplier increased to x" + String.format("%.0f", m_multiplier) + 
                             " after " + getFormattedTime() + "!");
        }
        
        // Award survival points - 1 point per second (with multiplier)
        int currentSecond = (int)m_elapsedTime;
        int lastSecond = (int)m_lastScoredSecond;
        
        if (currentSecond > lastSecond) {
            // Award points for each full second that has passed
            int secondsPassed = currentSecond - lastSecond;
            int pointsToAward = (int)(secondsPassed * SURVIVAL_POINTS_PER_SECOND * m_multiplier);
            
            if (pointsToAward > 0) {
                m_score += pointsToAward;
                m_lastScoredSecond = currentSecond;
                
                if (secondsPassed > 0) {
                    System.out.println("Survival points awarded: " + pointsToAward + 
                                     " (x" + String.format("%.0f", m_multiplier) + " multiplier). " +
                                     "Total score: " + m_score + ", Time: " + getFormattedTime());
                }
            }
        }
    }
    
    /**
     * Get current total score
     */
    public int getScore() {
        return m_score;
    }
    
    /**
     * Get elapsed time in seconds
     */
    public double getElapsedTime() {
        return m_elapsedTime;
    }
    
    /**
     * Get number of coins collected
     */
    public int getCoinCount() {
        return m_coinCount;
    }
    
    /**
     * Get current multiplier
     */
    public double getMultiplier() {
        return m_multiplier;
    }
    
    /**
     * Get formatted time string (MM:SS)
     */
    public String getFormattedTime() {
        int minutes = (int)(m_elapsedTime / 60);
        int seconds = (int)(m_elapsedTime % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    /**
     * Reset score manager for new game
     */
    public void reset() {
        m_score = 0;
        m_elapsedTime = 0;
        m_lastScoredSecond = 0;
        m_coinCount = 0;
        m_multiplier = 1.0;
        System.out.println("Score manager reset for new game");
    }
    
    /**
     * Get score breakdown for display
     */
    public String getScoreBreakdown() {
        int survivalScore = (int)(m_elapsedTime * m_multiplier);
        int coinScore = m_coinCount * COIN_VALUE;
        return String.format("Survival: %d | Coins: %d | Total: %d", 
                           survivalScore, coinScore, m_score);
    }
    
    /**
     * Save current score to high scores and reset game
     * Called when game ends
     */
    public void gameOver() {
        if (m_score > 0) {
            scores.add(m_score);
            System.out.println("Game over! Score " + m_score + " added to high scores.");
        }
        reset();
    }
    
    /**
     * Get high scores list (sorted descending)
     */
    public List<Integer> getHighScores() {
        List<Integer> sortedScores = new ArrayList<>(scores);
        Collections.sort(sortedScores, Collections.reverseOrder());
        return sortedScores;
    }
    
    /**
     * Get all scores in insertion order
     */
    public List<Integer> getAllScores() {
        return new ArrayList<>(scores);
    }
    
    /**
     * Get number of games played
     */
    public int getGamesPlayed() {
        return scores.size();
    }
    
    /**
     * Get best score
     */
    public int getBestScore() {
        if (scores.isEmpty()) return 0;
        return Collections.max(scores);
    }
    
    /**
     * Clear all high scores (for testing)
     */
    public void clearHighScores() {
        scores.clear();
        System.out.println("High scores cleared");
    }
}