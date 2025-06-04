package engine.model;

import engine.view.Avatar;

public abstract class Entity {
  public Object avatar;
  public Stunt stunt;
  protected Model m_model;
  protected int m_row, m_col;
  protected double m_x, m_y; // Position in meters
  protected double m_orientation; // Orientation in degrees
  protected double m_velocity; // Velocity in meters per second
  protected double m_angularVelocity; // Rotation speed in degrees per second

  protected Entity(Model m, int r, int c, double o) {
    m_model = m;
    m_row = r;
    m_col = c;
    m_x = m.gridToMetersX(c);
    m_y = m.gridToMetersY(r);
    m_orientation = normalize(o);
    m_velocity = 0;
    m_angularVelocity = 0;
    m_model.addAt(this);
  }
  
  // Normalize an angle back to the range [0:360[
  private double normalize(double angle) {
    while (angle < 0) {
      angle += 360;
    }
    while (angle >= 360) {
      angle -= 360;
    }
    return angle;
  }

  

  /*
   * Get the entity to face the orientation 
   * match the given angle
   */
  public void face(double theta) {
    m_orientation = normalize(theta);
  }

  public double orientation() {
    return m_orientation;
  }

  public int row() {
    return m_row;
  }

  public int col() {
    return m_col;
  }
  
  public double getX() {
    return m_x;
  }
  
  public double getY() {
    return m_y;
  }
  
  public double getVelocity() {
    return m_velocity;
  }
  
  public void setVelocity(double velocity) {
    m_velocity = velocity;
  }
  
  public double getAngularVelocity() {
    return m_angularVelocity;
  }
  
  public void setAngularVelocity(double angularVelocity) {
    m_angularVelocity = angularVelocity;
  }

  
  
  
  /*
   * Set the position of this entity (package-private for Model access)
   */
  void setPosition(int row, int col) {
    m_row = row;
    m_col = col;
    m_x = m_model.gridToMetersX(col);
    m_y = m_model.gridToMetersY(row);
  }
  
  /*
   * Update the entity's continuous position
   */
  public void setMetricPosition(double x, double y) {
    m_x = x;
    m_y = y;
    // Update grid position
    int newRow = m_model.metersToGridY(y);
    int newCol = m_model.metersToGridX(x);
    if (newRow != m_row || newCol != m_col) {
      m_model.move(this, newRow - m_row, newCol - m_col);
    }
  }
  
  /*
   * Update entity state based on elapsed time
   */
  public void update(double deltaTime) {
    // Update orientation based on angular velocity
    if (m_angularVelocity != 0) {
      stunt.rotate(m_angularVelocity * deltaTime);
    }
    
    // Update position based on velocity and orientation
    if (m_velocity != 0) {
      double radians = Math.toRadians(m_orientation);
      double dx = Math.cos(radians) * m_velocity * deltaTime;
      double dy = Math.sin(radians) * m_velocity * deltaTime;
      setMetricPosition(m_x + dx, m_y + dy);
    }
  }
  
  public void setAvatar(Avatar avatar) {
    this.avatar = avatar;
  }

  public Object getAvatar() {
    return avatar;
  }
}
