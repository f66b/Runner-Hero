package engine.model;

public class Player extends Entity {
  private static final double MAX_VELOCITY = 5.0; // meters per second
  private static final double ROTATION_SPEED = 180.0; // degrees per second
  private static final double MOVEMENT_STEP = 0.3; // meters per discrete step
  private double m_lastShotTime;
  private static final double SHOT_COOLDOWN = 0.5; // seconds between shots

  public Player(Model m, int x, int y, double o) {
    super(m, x, y, o);
    new game.model.StuntPlayer(m, this); // Create and link the stunt
  }

  /*
   * Move this entity up one step (fluid movement)
   */
  public void up() {
    // Option 1: Use stunt system (maintains orientation behavior)
    if (stunt != null) {
      stunt.up();
    } else {
      // Option 2: Direct fluid movement
      moveByPixels(0, -MOVEMENT_STEP);
    }
  }

  /*
   * Move this entity down one step (fluid movement)
   */
  public void down() {
    if (stunt != null) {
      stunt.down();
    } else {
      moveByPixels(0, MOVEMENT_STEP);
    }
  }

  /*
   * Move this entity left one step (fluid movement)
   */
  public void left() {
    if (stunt != null) {
      stunt.left();
    } else {
      moveByPixels(-MOVEMENT_STEP, 0);
    }
  }

  /*
   * Move this entity right one step (fluid movement)
   */
  public void right() {
    if (stunt != null) {
      stunt.right();
    } else {
      moveByPixels(MOVEMENT_STEP, 0);
    }
  }
  
  /*
   * Start moving forward based on current orientation (continuous movement)
   */
  public void startMoving() {
    setVelocity(MAX_VELOCITY);
  }
  
  /*
   * Start moving backward based on current orientation (continuous movement)
   */
  public void startMovingBackward() {
    setVelocity(-MAX_VELOCITY);
  }
  
  /*
   * Stop moving
   */
  public void stopMoving() {
    setVelocity(0);
  }
  
  /*
   * Move forward by a specific distance
   */
  public void moveForwardStep() {
    moveForward(MOVEMENT_STEP);
  }
  
  /*
   * Move backward by a specific distance
   */
  public void moveBackwardStep() {
    moveBackward(MOVEMENT_STEP);
  }
  
  /*
   * Strafe left (move left without changing orientation)
   */
  public void strafeLeft() {
    double currentOrientation = orientation();
    double strafeAngle = currentOrientation - 90; // 90 degrees left of current facing
    moveInDirection(MOVEMENT_STEP, strafeAngle);
  }
  
  /*
   * Strafe right (move right without changing orientation)
   */
  public void strafeRight() {
    double currentOrientation = orientation();
    double strafeAngle = currentOrientation + 90; // 90 degrees right of current facing
    moveInDirection(MOVEMENT_STEP, strafeAngle);
  }
  
  /*
   * Start rotating counter-clockwise
   */
  public void startRotatingLeft() {
    setAngularVelocity(-ROTATION_SPEED);
  }
  
  /*
   * Start rotating clockwise
   */
  public void startRotatingRight() {
    setAngularVelocity(ROTATION_SPEED);
  }
  
  /*
   * Stop rotating
   */
  public void stopRotating() {
    setAngularVelocity(0);
  }
  
  /*
   * Rotate by a specific angle instantly
   */
  public void rotateBy(double angleDelta) {
    face(orientation() + angleDelta);
  }
  
  /*
   * Set target orientation to rotate towards
   */
  public void setTargetOrientation(double angle) {
    // For smooth rotation, you could implement gradual turning here
    // For now, just face the angle directly
    face(angle);
  }
  
  /*
   * Rotate smoothly towards mouse position
   */
  public void rotateTowards(double targetX, double targetY) {
    double dx = targetX - getX();
    double dy = targetY - getY();
    double targetAngle = Math.toDegrees(Math.atan2(dy, dx));
    setTargetOrientation(targetAngle);
  }
  
  /*
   * Shoot a projectile in the current facing direction
   */
  public void shoot(double currentTime) {
    if (currentTime - m_lastShotTime < SHOT_COOLDOWN) {
      return; // Still in cooldown
    }
    
    m_lastShotTime = currentTime;
    
    // Calculate projectile spawn position (slightly in front of player)
    double spawnDistance = m_model.getCellSizeMeters() * 0.6;
    double radians = Math.toRadians(orientation());
    double projectileX = getX() + Math.cos(radians) * spawnDistance;
    double projectileY = getY() + Math.sin(radians) * spawnDistance;
    
    // Create projectile
    new Projectile(m_model, projectileX, projectileY, orientation());
  }
  
  /*
   * Start moving in the nearest cardinal direction based on current orientation
   */
  public void startMovingCardinal() {
    double cardinalAngle = getNearestCardinalDirection();
    face(cardinalAngle); // Set to exact cardinal direction
    setVelocity(MAX_VELOCITY);
  }
  
  /*
   * Get the nearest cardinal direction (0°=East, 90°=South, 180°=West, 270°=North)
   */
  private double getNearestCardinalDirection() {
    double currentAngle = orientation();
    
    // Normalize angle to 0-360 range
    while (currentAngle < 0) currentAngle += 360;
    while (currentAngle >= 360) currentAngle -= 360;
    
    // Cardinal directions
    double[] cardinals = {0, 90, 180, 270}; // East, South, West, North
    
    double nearestAngle = cardinals[0];
    double minDifference = Math.abs(currentAngle - cardinals[0]);
    
    // Check all cardinal directions
    for (double cardinal : cardinals) {
      double diff = Math.abs(currentAngle - cardinal);
      // Also check the wrapped-around difference (e.g., 350° vs 10°)
      double wrappedDiff = Math.min(diff, 360 - diff);
      
      if (wrappedDiff < minDifference) {
        minDifference = wrappedDiff;
        nearestAngle = cardinal;
      }
    }
    
    return nearestAngle;
  }
  
  /*
   * Set the movement step size for discrete movements
   */
  public void setMovementStep(double stepSize) {
    // This would require making MOVEMENT_STEP non-final
    // For now, this is just a placeholder for future enhancement
  }
  
  @Override
  public void update(double deltaTime) {
    // Call parent update for basic physics
    super.update(deltaTime);
  }
}