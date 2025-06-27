package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import oop.graphics.Canvas;
import java.util.List;

import game.model.ScoreManager;
import oop.graphics.Canvas;

/**
 * UIManager handles the game's UI states and interactions:
 * START, RUNNING, GAME_OVER
 */
public class UIManager {
    
    public enum UIState {
        START,      // Start screen with PLAY button
        RUNNING,    // Game is running
        GAME_OVER   // Game over screen with REPLAY button and high scores
    }
    
    private UIState m_currentState = UIState.START;
    private Canvas m_canvas;
    private Game m_game;
    private ScoreManager m_scoreManager;
    private int m_lastScore = 0; // Store last game score for display
    
    // Button rectangles for hit detection
    private Rectangle m_playButton;
    private Rectangle m_replayButton;
    
    // UI constants
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 215, 0);
    
    public UIManager(Canvas canvas) {
        m_canvas = canvas;
        m_scoreManager = ScoreManager.getInstance();
        
        // Initialize button rectangles
        updateButtonPositions();
    }
    
    /**
     * Set the game instance
     */
    public void setGame(Game game) {
        m_game = game;
    }
    
    /**
     * Get current UI state
     */
    public UIState getCurrentState() {
        return m_currentState;
    }
    
    /**
     * Set the current UI state
     */
    public void setState(UIState state) {
        System.out.println("UIManager.setState() called - changing from " + m_currentState + " to " + state);
        
        if (m_currentState == state) {
            System.out.println("State unchanged - already " + state);
            return;
        }
        
        m_currentState = state;
        updateButtonPositions(); // Update button positions when state changes
        System.out.println("UI State successfully changed to: " + state);
        
        // Force a repaint to update the UI
        if (m_canvas != null) {
            m_canvas.repaint();
            System.out.println("Canvas repaint requested");
        }
    }
    
    /**
     * Update button positions based on canvas size
     */
    private void updateButtonPositions() {
        int canvasWidth = m_canvas.getWidth();
        int canvasHeight = m_canvas.getHeight();
        
        // PLAY button (centered)
        int buttonWidth = 200;
        int buttonHeight = 60;
        int playButtonX = (canvasWidth - buttonWidth) / 2;
        int playButtonY = (canvasHeight - buttonHeight) / 2;
        m_playButton = new Rectangle(playButtonX, playButtonY, buttonWidth, buttonHeight);
        
        // REPLAY button (centered, but higher up to make room for scores)
        int replayButtonY = canvasHeight / 2 - 100;
        m_replayButton = new Rectangle(playButtonX, replayButtonY, buttonWidth, buttonHeight);
    }
    
    /**
     * Draw the UI based on current state
     */
    public void drawUI(Graphics2D g) {
        switch (m_currentState) {
            case START:
                drawStartScreen(g);
                break;
            case RUNNING:
                // Game UI is handled by Game class
                break;
            case GAME_OVER:
                drawGameOverScreen(g);
                break;
        }
    }
    
    /**
     * Draw start screen with PLAY button
     */
    private void drawStartScreen(Graphics2D g) {
        int canvasWidth = m_canvas.getWidth();
        int canvasHeight = m_canvas.getHeight();
        
        // Draw background
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // Draw title
        g.setColor(ACCENT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "RUNNER HERO";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (canvasWidth - titleWidth) / 2, canvasHeight / 2 - 100);
        
        // Draw subtitle
        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String subtitle = "Jump and slide to avoid obstacles!";
        int subtitleWidth = g.getFontMetrics().stringWidth(subtitle);
        g.drawString(subtitle, (canvasWidth - subtitleWidth) / 2, canvasHeight / 2 - 50);
        
        // Draw PLAY button
        drawButton(g, m_playButton, "PLAY", true);
        
        // Draw controls info
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        String controls = "Controls: UP/SPACE = Jump, DOWN = Slide";
        int controlsWidth = g.getFontMetrics().stringWidth(controls);
        g.drawString(controls, (canvasWidth - controlsWidth) / 2, canvasHeight - 50);
    }
    
    /**
     * Draw game over screen with REPLAY button and high scores
     */
    private void drawGameOverScreen(Graphics2D g) {
        int canvasWidth = m_canvas.getWidth();
        int canvasHeight = m_canvas.getHeight();
        
        // Draw semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // Draw GAME OVER title
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String gameOver = "GAME OVER";
        int gameOverWidth = g.getFontMetrics().stringWidth(gameOver);
        g.drawString(gameOver, (canvasWidth - gameOverWidth) / 2, 100);
        
        // Draw final score
        g.setColor(ACCENT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String scoreText = "Score: " + m_lastScore;
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (canvasWidth - scoreWidth) / 2, 150);
        
        // Draw REPLAY button
        drawButton(g, m_replayButton, "REPLAY", true);
        
        // Draw high scores table
        drawHighScoresTable(g, canvasWidth, canvasHeight);
    }
    
    /**
     * Draw high scores table
     */
    private void drawHighScoresTable(Graphics2D g, int canvasWidth, int canvasHeight) {
        List<Integer> highScores = m_scoreManager.getHighScores();
        
        if (highScores.isEmpty()) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            String noScores = "No scores yet!";
            int noScoresWidth = g.getFontMetrics().stringWidth(noScores);
            g.drawString(noScores, (canvasWidth - noScoresWidth) / 2, canvasHeight / 2 + 100);
            return;
        }
        
        // Table title
        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String tableTitle = "HIGH SCORES";
        int tableTitleWidth = g.getFontMetrics().stringWidth(tableTitle);
        g.drawString(tableTitle, (canvasWidth - tableTitleWidth) / 2, canvasHeight / 2 + 80);
        
        // Table headers
        g.setFont(new Font("Arial", Font.BOLD, 16));
        int tableStartY = canvasHeight / 2 + 110;
        int leftColumnX = canvasWidth / 2 - 100;
        int rightColumnX = canvasWidth / 2 + 20;
        
        g.setColor(ACCENT_COLOR);
        g.drawString("#", leftColumnX, tableStartY);
        g.drawString("Score", rightColumnX, tableStartY);
        
        // Draw line under headers
        g.drawLine(leftColumnX - 10, tableStartY + 5, rightColumnX + 80, tableStartY + 5);
        
        // Table rows (show top 10)
        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        int maxRows = Math.min(10, highScores.size());
        
        for (int i = 0; i < maxRows; i++) {
            int rowY = tableStartY + 25 + (i * 22);
            String rank = String.valueOf(i + 1);
            String score = String.valueOf(highScores.get(i));
            
            // Highlight current row if it's the last scored game
            if (highScores.get(i) == m_lastScore) {
                g.setColor(ACCENT_COLOR);
            } else {
                g.setColor(TEXT_COLOR);
            }
            
            g.drawString(rank, leftColumnX, rowY);
            g.drawString(score, rightColumnX, rowY);
        }
        
        // Show total games played
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        String gamesPlayed = "Games played: " + m_scoreManager.getGamesPlayed();
        int gamesPlayedWidth = g.getFontMetrics().stringWidth(gamesPlayed);
        g.drawString(gamesPlayed, (canvasWidth - gamesPlayedWidth) / 2, canvasHeight - 30);
    }
    
    /**
     * Draw a button with hover effects
     */
    private void drawButton(Graphics2D g, Rectangle button, String text, boolean enabled) {
        // Update button position in case canvas was resized
        updateButtonPositions();
        
        // Button background
        if (enabled) {
            g.setColor(BUTTON_COLOR);
        } else {
            g.setColor(new Color(100, 100, 100));
        }
        g.fillRoundRect(button.x, button.y, button.width, button.height, 15, 15);
        
        // Button border
        g.setColor(enabled ? BUTTON_TEXT_COLOR : new Color(150, 150, 150));
        g.drawRoundRect(button.x, button.y, button.width, button.height, 15, 15);
        
        // Button text
        g.setFont(new Font("Arial", Font.BOLD, 24));
        int textWidth = g.getFontMetrics().stringWidth(text);
        int textHeight = g.getFontMetrics().getHeight();
        int textX = button.x + (button.width - textWidth) / 2;
        int textY = button.y + (button.height + textHeight / 2) / 2;
        
        g.drawString(text, textX, textY);
    }
    
    /**
     * Handle game over
     */
    public void onGameOver(int finalScore) {
        System.out.println("UIManager.onGameOver called with score: " + finalScore);
        m_lastScore = finalScore;
        
        // Save score to high scores and reset for next game
        m_scoreManager.gameOver();
        
        setState(UIState.GAME_OVER);
        System.out.println("UI state changed to GAME_OVER");
    }
    
    /**
     * Start new game
     */
    public void startNewGame() {
        System.out.println("UIManager.startNewGame() called");
        System.out.println("Current state before: " + m_currentState);
        
        if (m_game != null) {
            System.out.println("Calling game.resetGame()");
            m_game.resetGame();
        } else {
            System.out.println("Warning: m_game is null!");
        }
        
        System.out.println("Setting state to RUNNING");
        setState(UIState.RUNNING);
        System.out.println("State after startNewGame: " + m_currentState);
    }
    
    /**
     * Handle mouse click at the given coordinates
     */
    public void handleMouseClick(int x, int y) {
        System.out.println("UIManager.handleMouseClick called at (" + x + ", " + y + ")");
        System.out.println("Current UI state: " + m_currentState);
        
        switch (m_currentState) {
            case START:
                System.out.println("In START state, checking PLAY button");
                updateButtonPositions(); // Ensure button positions are current
                System.out.println("PLAY button bounds: " + m_playButton);
                
                if (m_playButton != null && m_playButton.contains(x, y)) {
                    System.out.println("PLAY button clicked! Calling startNewGame()");
                    startNewGame();
                } else {
                    System.out.println("Click missed PLAY button. Button: " + m_playButton);
                }
                break;
                
            case GAME_OVER:
                System.out.println("In GAME_OVER state, checking REPLAY button");
                if (m_replayButton != null && m_replayButton.contains(x, y)) {
                    System.out.println("REPLAY button clicked! Calling startNewGame()");
                    startNewGame();
                } else {
                    System.out.println("Click missed REPLAY button");
                }
                break;
                
            case RUNNING:
                System.out.println("In RUNNING state - no UI clicks processed");
                break;
        }
    }
} 