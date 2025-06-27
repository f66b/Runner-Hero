package engine.model;

/**
 * Player class for the runner-hero game.
 * The player runs automatically to the right and can jump/slide to avoid obstacles.
 * Has a health system with invincibility frames.
 */
public class Player extends Entity {
  private static final double MAX_VELOCITY = 5.0; // meters per second
  private static final double ROTATION_SPEED = 180.0; // degrees per second
  private static final double MOVEMENT_STEP = 0.3; // meters per discrete step
  private double m_lastShotTime;
  private static final double SHOT_COOLDOWN = 0.5; // seconds between shots

  // Health system
  private int m_health = 3; // Max 3 health points
  private static final int MAX_HEALTH = 3;
  private boolean m_invincible = false;
  private double m_invincibilityTimer = 0;
  private static final double INVINCIBILITY_DURATION = 0.5; // 0.5 seconds
  
  // Runner-style movement
  private static final double RUNNER_SPEED = 3.0; // Auto-movement speed
  private boolean m_isAlive = true;

  public Player(Model m, int x, int y, double o) {
    super(m, x, y, o);
    new game.model.StuntPlayer(m, this); // Create and link the stunt
    // Always face right for runner style
    face(0);
  }

  /**
   * Get current health points
   */
  public int getHealth() {
    return m_health;
  }
  
  /**
   * Get maximum health points
   */
  public int getMaxHealth() {
    return MAX_HEALTH;
  }
  
  /**
   * Heal the player by the specified amount
   */
  public void heal(int amount) {
    m_health = Math.min(MAX_HEALTH, m_health + amount);
    System.out.println("Player healed. Health: " + m_health + "/" + MAX_HEALTH);
  }
  
  /**
   * Damage the player by the specified amount
   */
  public void takeDamage(int damage) {
    if (m_invincible) {
      return; // No damage during invincibility
    }
    
    m_health -= damage;
    System.out.println("Player took " + damage + " damage. Health: " + m_health + "/" + MAX_HEALTH);
    
    if (m_health <= 0) {
      m_health = 0;
      die();
    } else {
      // Start invincibility frames
      m_invincible = true;
      m_invincibilityTimer = INVINCIBILITY_DURATION;
    }
  }
  
  /**
   * Kill the player instantly (collision with obstacle/bot)
   */
  public void die() {
    m_isAlive = false;
    m_health = 0;
    System.out.println("Player died!");
    // TODO: Trigger game over
  }
  
  /**
   * Check if player is alive
   */
  public boolean isAlive() {
    return m_isAlive;
  }
  
  /**
   * Check if player is invincible
   */
  public boolean isInvincible() {
    return m_invincible;
  }

  /*
   * Move this entity up one step (jump in runner style)
   */
  public void up() {
    if (stunt != null) {
      stunt.jump();
    }
  }

  /*
   * Move this entity down one step (slide in runner style)
   */
  public void down() {
    if (stunt != null) {
      stunt.slide();
    }
  }

  /*
   * Move this entity left one step (not used in runner style)
   */
  public void left() {
    // No left movement in runner style
  }

  /*
   * Move this entity right one step (not used in runner style - auto movement)
   */
  public void right() {
    // No manual right movement in runner style - handled by automatic movement
  }
  
  /**
   * Jump (same as up)
   */
  public void jump() {
    up();
  }
  
  /**
   * Slide (same as down)
   */
  public void slide() {
    down();
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
    // For runner style, always face right
    face(0);
  }
  
  /*
   * Rotate smoothly towards mouse position (not used in runner style)
   */
  public void rotateTowards(double targetX, double targetY) {
    // Not used in runner style
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
    double[] cardinals = {0, 90, 180, 270};
    double closest = cardinals[0];
    double minDiff = Math.abs(currentAngle - closest);
    
    for (double cardinal : cardinals) {
      double diff = Math.abs(currentAngle - cardinal);
      if (diff > 180) diff = 360 - diff; // Handle wrap-around
      if (diff < minDiff) {
        minDiff = diff;
        closest = cardinal;
      }
    }
    
    return closest;
  }
  
  public void setMovementStep(double stepSize) {
    // Movement step customization
  }

  @Override
  public void update(double deltaTime) {
    super.update(deltaTime);
    
    // Update invincibility timer
    if (m_invincible) {
      m_invincibilityTimer -= deltaTime;
      if (m_invincibilityTimer <= 0) {
        m_invincible = false;
        m_invincibilityTimer = 0;
      }
    }
    
    // Check collisions with other entities
    checkCollisions();
  }
  
  /**
   * Check collisions with other entities in the same grid cell
   */
  private void checkCollisions() {
    if (!m_isAlive) return;
    
    Entity other = m_model.entity(row(), col());
    if (other != null && other != this) {
      handleCollision(other);
    }
  }
  
  /**
   * Handle collision with another entity
   */
  private void handleCollision(Entity other) {
    if (other instanceof Collectable) {
      // Handle collectible pickup
      Collectable collectable = (Collectable) other;
      collectable.onPickUp(this);
    } else if (other instanceof Rock || other instanceof Pigeon) {
      // Instant death from obstacle collision
      die();
    } else if (other instanceof Projectile) {
      // Projectile collision is handled by the Projectile class itself
      // with strategic hit detection - don't handle here to avoid conflicts
      System.out.println("Player grid collision with projectile detected (handled by Projectile class)");
    } else if (other instanceof DeathZone) {
      // Instant death from death zone
      die();
    }
  }
}