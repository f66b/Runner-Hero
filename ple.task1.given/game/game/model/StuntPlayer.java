package game.model;

import engine.model.*;

public class StuntPlayer extends Stunt {
    public double north, south, east, west;
    
    // Movement settings for fluid movement
    private static final double MOVEMENT_SPEED = 2.0; // meters per second
    private static final double STEP_SIZE = 0.5; // meters per discrete step
    
    public StuntPlayer(Model m, Entity e) {
        super(m, e);
    }
    
    @Override
    public void rotate(double angle) {
        angle = cardinalOf(angle);
        super.rotate(angle);
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
    
    // Rotation methods for Shift+Left/Right
    public void rotateLeft() {
        // Rotate 90 degrees counter-clockwise
        rotate(-90);
    }

    public void rotateRight() {
        // Rotate 90 degrees clockwise
        rotate(90);
    }
    
    // Fluid movement methods - these now move by pixels instead of grid cells
    public void left() {
        orient(180);
        e.moveByPixels(-STEP_SIZE, 0); // Move left by step size
    }
    
    public void right() {
        orient(0);
        e.moveByPixels(STEP_SIZE, 0); // Move right by step size
    }
    
    public void up() {
        orient(270);
        e.moveByPixels(0, -STEP_SIZE); // Move up by step size
    }
    
    public void down() {
        orient(90);
        e.moveByPixels(0, STEP_SIZE); // Move down by step size
    }
    
    
    
    // Alternative continuous movement methods
    public void startMovingLeft() {
        orient(180);
        e.setVelocity(MOVEMENT_SPEED);
    }
    
    public void startMovingRight() {
        orient(0);
        e.setVelocity(MOVEMENT_SPEED);
    }
    
    public void startMovingUp() {
        orient(270);
        e.setVelocity(MOVEMENT_SPEED);
    }
    
    public void startMovingDown() {
        orient(90);
        e.setVelocity(MOVEMENT_SPEED);
    }
    
    
    public void stopMoving() {
        e.setVelocity(0);
    }
    
    // Method to move in current facing direction
    public void moveForward(double distance) {
        e.moveForward(distance);
    }
    
    public void moveBackward(double distance) {
        e.moveBackward(distance);
    }
    
    // Set movement step size for discrete movements
    public void setStepSize(double stepSize) {
        // This would require making STEP_SIZE non-final, or adding an instance variable
    }
    public void strafeLeft() {
        double currentOrientation = e.orientation();
        double speed = (currentOrientation == 180) ? MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        e.moveByPixels(-STEP_SIZE * (speed / MOVEMENT_SPEED), 0);
    }

    /**
     * Move right without changing orientation.
     * Uses full speed if facing right (0°), half speed otherwise.
     */
    public void strafeRight() {
        double currentOrientation = e.orientation();
        double speed = (currentOrientation == 0) ? MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        e.moveByPixels(STEP_SIZE * (speed / MOVEMENT_SPEED), 0);
    }

    /**
     * Move up without changing orientation.
     * Uses full speed if facing up (270°), half speed otherwise.
     */
    public void strafeUp() {
        double currentOrientation = e.orientation();
        double speed = (currentOrientation == 270) ? MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        e.moveByPixels(0, -STEP_SIZE * (speed / MOVEMENT_SPEED));
    }

    /**
     * Move down without changing orientation.
     * Uses full speed if facing down (90°), half speed otherwise.
     */
    public void strafeDown() {
        double currentOrientation = e.orientation();
        double speed = (currentOrientation == 90) ? MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        e.moveByPixels(0, STEP_SIZE * (speed / MOVEMENT_SPEED));
    }

    /**
     * Start continuous movement left without changing orientation.
     * Uses full speed if facing left (180°), half speed otherwise.
     */
    public void startStrafingLeft() {
        double currentOrientation = e.orientation();
        double speed = (currentOrientation == 180) ? MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        
        // Set up continuous leftward movement
        e.setVelocity(0); // Stop current velocity-based movement
        // Use a custom movement approach or store the strafe direction
        // This would require additional state management in Entity class
    }

    /**
     * Start continuous movement right without changing orientation.
     * Uses full speed if facing right (0°), half speed otherwise.
     */
    public void startStrafingRight() {
        double currentOrientation = e.orientation();
        double speed = (currentOrientation == 0) ? MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        
        e.setVelocity(0);
        // Similar approach as above
    }

    /**
     * Start continuous movement up without changing orientation.
     * Uses full speed if facing up (270°), half speed otherwise.
     */
    public void startStrafingUp() {
        double currentOrientation = e.orientation();
        double speed = (currentOrientation == 270) ? MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        
        e.setVelocity(0);
        // Similar approach as above
    }

    /**
     * Start continuous movement down without changing orientation.
     * Uses full speed if facing down (90°), half speed otherwise.
     */
    public void startStrafingDown() {
        double currentOrientation = e.orientation();
        double speed = (currentOrientation == 90) ? MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        
        e.setVelocity(0);
        // Similar approach as above
    }

    /**
     * More flexible method: move in any direction without changing orientation
     * @param direction 0=right, 90=down, 180=left, 270=up
     */
    public void strafeInDirection(double direction) {
        double currentOrientation = e.orientation();
        
        // Normalize angles to handle floating point precision
        direction = normalizeAngle(direction);
        currentOrientation = normalizeAngle(currentOrientation);
        
        // Use full speed if moving in facing direction, half speed otherwise
        double speed = (Math.abs(direction - currentOrientation) < 0.1) ? 
                       MOVEMENT_SPEED : MOVEMENT_SPEED / 2.0;
        
        // Calculate movement components
        double radians = Math.toRadians(direction);
        double deltaX = Math.cos(radians) * STEP_SIZE * (speed / MOVEMENT_SPEED);
        double deltaY = Math.sin(radians) * STEP_SIZE * (speed / MOVEMENT_SPEED);
        
        e.moveByPixels(deltaX, deltaY);
    }
    private double normalizeAngle(double angle) {
        while (angle < 0) angle += 360;
        while (angle >= 360) angle -= 360;
        return angle;
    }
}