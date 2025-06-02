package engine.model;

public class Player extends Entity {
  private static final double MAX_VELOCITY = 5.0; // meters per second
  private static final double ROTATION_SPEED = 180.0; // degrees per second
  private double m_targetOrientation;
  private boolean m_rotatingToTarget;
  private double m_lastShotTime;
  private static final double SHOT_COOLDOWN = 0.5; // seconds between shots

  public Player(Model m, int x, int y, double o) {
    super(m, x, y, o);
    m_targetOrientation = o;
    m_rotatingToTarget = false;
    m_lastShotTime = 0;
  }

  /*
   * Move this entity up one row.
   */
  public void up() {
    move(-1, 0);
  }

  /*
   * Move this entity down one row.
   */
  public void down() {
    move(1, 0);
  }

  /*
   * Move this entity left one column.
   */
  public void left() {
    move(0, -1);
  }

  /*
   * Move this entity right one column.
   */
  public void right() {
    move(0, 1);
  }
  
  /*
   * Start moving forward based on current orientation
   */
  public void startMoving() {
    setVelocity(MAX_VELOCITY);
  }
  
  /*
   * Start moving backward based on current orientation
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
   * Start rotating counter-clockwise
   */
  public void startRotatingLeft() {
    setAngularVelocity(-ROTATION_SPEED);
    m_rotatingToTarget = false;
  }
  
  /*
   * Start rotating clockwise
   */
  public void startRotatingRight() {
    setAngularVelocity(ROTATION_SPEED);
    m_rotatingToTarget = false;
  }
  
  /*
   * Stop rotating
   */
  public void stopRotating() {
    setAngularVelocity(0);
    m_rotatingToTarget = false;
  }
  
  /*
   * Set target orientation to rotate towards
   */
  public void setTargetOrientation(double angle) {
    m_targetOrientation = angle;
    m_rotatingToTarget = true;
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
  
  @Override
  public void update(double deltaTime) {
    // Handle smooth rotation to target
    if (m_rotatingToTarget && Math.abs(getAngularVelocity()) < 0.01) {
      double angleDiff = m_targetOrientation - orientation();
      
      // Normalize angle difference to [-180, 180]
      while (angleDiff > 180) angleDiff -= 360;
      while (angleDiff < -180) angleDiff += 360;
      
      if (Math.abs(angleDiff) > 1) {
        // Rotate towards target
        double rotationSpeed = Math.min(ROTATION_SPEED, Math.abs(angleDiff) / deltaTime);
        setAngularVelocity(angleDiff > 0 ? rotationSpeed : -rotationSpeed);
      } else {
        // Close enough, stop rotating
        face(m_targetOrientation);
        setAngularVelocity(0);
        m_rotatingToTarget = false;
      }
    }
    
    // Call parent update
    super.update(deltaTime);
  }

}
