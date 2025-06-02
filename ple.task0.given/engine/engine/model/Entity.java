package engine.model;

public abstract class Entity {
  protected Model m_model;
  protected int m_row, m_col;
  protected int m_orientation;

  protected Entity(Model m, int r, int c, int o) {
    m_model = m;
    m_row = r;
    m_col = c;
    m_orientation = normalize(o);
    m_model.addAt(this);
  }
  
  // Normalize an angle back to the range [0:360[
  private int normalize(int angle) {
    while(angle<0) {
    	angle=angle+360;
    }
    while(angle>=360) {
    	angle=angle-360;
    }
    return angle;
  }

  /*
   * Rotate the entity by the given angle
   */
  public void rotate(int theta) {
    m_orientation=normalize(m_orientation + theta);
  }

  /*
   * Get the entity to face the orientation 
   * match the given angle
   */
  public void face(int theta) {
	  m_orientation=normalize(theta);
  }

  public int orientation() {
    return m_orientation;
  }

  public int row() {
    return m_row;
  }

  public int col() {
    return m_col;
  }

  /*
   * Move this entity in the model by the given
   * count of rows and columns.
   */
  public void move(int nrows, int ncols) {
    m_model.move(this, nrows, ncols);
  }
  
}
