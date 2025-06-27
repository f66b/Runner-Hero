package game.model;

import engine.model.*;

public class StuntPlayer extends Stunt {
    public double north, south, east, west;
    
    // Movement settings for fluid movement
    private static final double MOVEMENT_SPEED = 2.0; // meters per second
    private static final double STEP_SIZE = 0.5; // meters per discrete step
    
    // Runner-style movement states
    private boolean m_isJumping = false;
    private boolean m_isSliding = false;
    private boolean m_slideKeyPressed = false; // Track slide key state
    private double m_normalY;

    // Animation parameters
    private static final double JUMP_HEIGHT = 3.5; // meters - increased from 2.0 for higher jumps
    private static final double JUMP_DURATION = 0.6; // seconds
    private static final double SLIDE_DEPTH = 0.5; // meters
    private static final double SLIDE_MAX_DURATION = 2.0; // Maximum slide duration in seconds

    private double m_actionTime = 0;
    private long m_jumpStartTime = 0; // Fallback timer for jump
    private long m_slideStartTime = 0; // Fallback timer for slide
    
    public StuntPlayer(Model m, Entity e) {
        super(m, e);
        m_normalY = e.getY();
    }
    
    @Override
    public void tick(int elapsedMs) {
        super.tick(elapsedMs);
        double deltaTime = elapsedMs / 1000.0;
        
        // Debug output every 30 frames to track state
        if (elapsedMs > 0 && (System.currentTimeMillis() % 500) < 50) { // Roughly every half second
            System.out.println("StuntPlayer.tick() - Jumping: " + m_isJumping + 
                             ", Sliding: " + m_isSliding + 
                             ", SlideKeyPressed: " + m_slideKeyPressed + 
                             ", ActionTime: " + String.format("%.2f", m_actionTime) + 
                             ", DeltaTime: " + String.format("%.3f", deltaTime));
        }
        
        if (m_isJumping) {
            m_actionTime += deltaTime;
            if (m_actionTime >= JUMP_DURATION) {
                // Jump duration completed - return to running
                endJump();
                System.out.println("Jump completed, returning to running");
            } else {
                // Parabolic jump trajectory: y(t) = h * (1 - (2t/d - 1)^2)
                double t = m_actionTime;
                double d = JUMP_DURATION;
                double h = JUMP_HEIGHT;
                double time_factor = (2 * t / d) - 1;
                double yOffset = h * (1 - time_factor * time_factor);
                double targetY = m_normalY - yOffset;
                e.setMetricPosition(e.getX(), targetY);
            }
        } else if (m_isSliding) {
            m_actionTime += deltaTime;
            
            // Immediate check: if key is not pressed, end slide immediately
            if (!m_slideKeyPressed) {
                endSlide();
                System.out.println("Slide ended immediately - key not pressed");
            } else if (m_actionTime >= SLIDE_MAX_DURATION) {
                // Max duration reached - force end slide
                endSlide();
                System.out.println("Slide timeout reached (" + String.format("%.2f", m_actionTime) + "s), returning to running");
            } else {
                // Continue sliding - maintain slide position
                double targetY = m_normalY + SLIDE_DEPTH;
                e.setMetricPosition(e.getX(), targetY);
            }
        }
        
        // Ensure we're always in a valid state - if neither jumping nor sliding, we should be running
        if (!m_isJumping && !m_isSliding) {
            // Make sure player is at normal Y position for running
            if (Math.abs(e.getY() - m_normalY) > 0.01) {
                e.setMetricPosition(e.getX(), m_normalY);
            }
        }
    }
    
    @Override
    public boolean rotate(double angle) {
        angle = cardinalOf(angle);
        return super.rotate(angle);
    }
    
    private double cardinalOf(double angle) {
        if (angle == north) {
            angle = 270;
        } else if (angle == east) {
            angle = 0;
        } else if (angle == south) {
            angle = 90;
        } else if (angle == west) {
            angle = 180;
        }
        return angle;
    }
    
    // Runner-style movement methods
    /**
     * Check fallback timeouts in case tick() is not being called
     */
    public void checkFallbackTimeouts() {
        long currentTime = System.currentTimeMillis();
        
        // Check jump timeout (600ms)
        if (m_isJumping && m_jumpStartTime > 0) {
            long jumpElapsed = currentTime - m_jumpStartTime;
            if (jumpElapsed >= 600) { // 600ms = 0.6 seconds
                System.out.println("FALLBACK: Jump timeout reached (" + jumpElapsed + "ms), ending jump");
                endJump();
            }
        }
        
        // Check slide timeout (2000ms)
        if (m_isSliding && m_slideStartTime > 0) {
            long slideElapsed = currentTime - m_slideStartTime;
            if (slideElapsed >= 2000 || !m_slideKeyPressed) { // 2000ms = 2 seconds OR key not pressed
                System.out.println("FALLBACK: Slide timeout reached (" + slideElapsed + "ms) or key released, ending slide");
                endSlide();
            }
        }
    }
    
    /**
     * Make the player jump up
     */
    public void jump() {
        // Check for fallback timeout first
        checkFallbackTimeouts();
        
        if (!m_isJumping && !m_isSliding) {
            m_isJumping = true;
            m_actionTime = 0; // Reset action timer
            m_jumpStartTime = System.currentTimeMillis(); // Set fallback timer
            System.out.println("Player started jumping! (Fallback timeout in 600ms)");
        } else {
            System.out.println("Cannot jump - already jumping: " + m_isJumping + ", sliding: " + m_isSliding);
        }
    }
    
    /**
     * End the jump and return to normal position
     */
    public void endJump() {
        if (m_isJumping) {
            m_isJumping = false;
            m_actionTime = 0; // Reset action timer
            e.setMetricPosition(e.getX(), m_normalY);
            System.out.println("Player landed and returned to running!");
        }
    }
    
    /**
     * Start sliding - called when slide key is pressed
     */
    public void slide() {
        startSliding();
    }
    
    /**
     * Start sliding down - called when key is pressed
     */
    public void startSliding() {
        if (!m_isJumping && !m_isSliding) {
            m_isSliding = true;
            m_slideKeyPressed = true;
            m_actionTime = 0; // Reset action timer
            m_slideStartTime = System.currentTimeMillis(); // Set slide start time
            // Move to slide position immediately
            double targetY = m_normalY + SLIDE_DEPTH;
            e.setMetricPosition(e.getX(), targetY);
            System.out.println("Player started sliding!");
        } else if (m_isSliding) {
            // Already sliding, just mark key as pressed to extend slide
            m_slideKeyPressed = true;
            System.out.println("Slide key pressed while already sliding - extending slide");
        } else {
            System.out.println("Cannot slide - already jumping: " + m_isJumping + ", sliding: " + m_isSliding);
        }
    }
    
    /**
     * Stop sliding - called when slide key is released
     */
    public void stopSliding() {
        m_slideKeyPressed = false;
        System.out.println("Slide key released - will stop sliding soon");
        
        // IMMEDIATE FALLBACK: If we're sliding and key is released, end slide immediately
        // This prevents getting stuck if tick() is not being called
        if (m_isSliding) {
            System.out.println("FALLBACK: Ending slide immediately since key was released");
            endSlide();
        }
    }
    
    /**
     * End the slide and return to normal position
     */
    public void endSlide() {
        if (m_isSliding) {
            m_isSliding = false;
            m_slideKeyPressed = false;
            m_actionTime = 0; // Reset action timer
            e.setMetricPosition(e.getX(), m_normalY);
            System.out.println("Player stopped sliding and returned to running!");
        }
    }
    
    /**
     * Check if player is currently jumping
     */
    public boolean isJumping() {
        return m_isJumping;
    }
    
    /**
     * Check if player is currently sliding
     */
    public boolean isSliding() {
        return m_isSliding;
    }
    
    /**
     * Update normal Y position (for when player position changes)
     */
    public void updateNormalY() {
        if (!m_isJumping && !m_isSliding) {
            m_normalY = e.getY();
        }
    }
    
    // Rotation methods for Shift+Left/Right (not used in runner style)
    @Override
    public void rotateLeft() {
        // No rotation in runner style
    }

    @Override
    public void rotateRight() {
        // No rotation in runner style
    }
    
    // Grid movement methods - disabled for runner style
    @Override
    public void left() {
        // No left movement in runner style
    }
    
    @Override
    public void right() {
        // No manual right movement in runner style - auto-scrolling handles this
    }
    
    @Override
    public void up() {
        jump(); // Up key triggers jump
    }
    
    @Override
    public void down() {
        startSliding(); // Down key starts sliding
    }
    
    // Alternative continuous movement methods - mostly disabled for runner style
    @Override
    public void startMovingLeft() {
        // No left movement in runner style
    }
    
    @Override
    public void startMovingRight() {
        // No manual right movement in runner style
    }
    
    @Override
    public void startMovingUp() {
        jump(); // Up movement triggers jump
    }
    
    @Override
    public void startMovingDown() {
        startSliding(); // Down movement starts sliding
    }
    
    @Override
    public void stopMoving() {
        // In runner style, only stop sliding
        stopSliding();
    }
    
    // Method to move in current facing direction (not used in runner style)
    @Override
    public void moveForward(double distance) {
        // No manual forward movement in runner style
    }
    
    @Override
    public void moveBackward(double distance) {
        // No manual backward movement in runner style
    }
    
    // Rest of the movement methods (strafing) - not used in runner style
    @Override
    public void setStepSize(double stepSize) {
        // Not used in runner style
    }
    
    @Override
    public void strafeLeft() {
        // No strafing in runner style
    }
    
    @Override
    public void strafeRight() {
        // No strafing in runner style
    }
    
    @Override
    public void strafeUp() {
        // No strafing in runner style
    }
    
    @Override
    public void strafeDown() {
        // No strafing in runner style
    }
    
    @Override
    public void startStrafingLeft() {
        // No strafing in runner style
    }
    
    @Override
    public void startStrafingRight() {
        // No strafing in runner style
    }
    
    @Override
    public void startStrafingUp() {
        // No strafing in runner style
    }
    
    @Override
    public void startStrafingDown() {
        // No strafing in runner style
    }
    
    @Override
    public void strafeInDirection(double direction) {
        // No strafing in runner style
    }
    
    private double normalizeAngle(double angle) {
        while (angle < 0) angle += 360;
        while (angle >= 360) angle -= 360;
        return angle;
    }
}