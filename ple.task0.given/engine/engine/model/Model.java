package engine.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import engine.view.View;

public class Model {
  private int m_ncols, m_nrows;
  private Entity[][] m_grid;
  private Player m_player;
  private List<Entity> m_entities;
  private View m_view;
  private Config m_conf;

  public Model(int nr, int nc) {
    m_ncols = nc;
    m_nrows = nr;
    m_grid = new Entity[nr][nc];
    m_entities = new LinkedList<Entity>();
  }

  /* 
   * A callback from the constructor of an entity
   * to notify this model of a new entity to add
   * to this model.
   */
 
    
    
    
    
    
  void addAt(Entity e) {
      int row = normalize(e.m_row, m_nrows);
      int col = normalize(e.m_col, m_ncols);
      
      // Update entity position to normalized values
      e.m_row = row;
      e.m_col = col;
      
      m_grid[row][col] = e;
      if (!m_entities.contains(e)) {
          m_entities.add(e);
      }
  }
  
  void removeAt(Entity e) {
      if (e.m_row >= 0 && e.m_row < m_nrows && e.m_col >= 0 && e.m_col < m_ncols) {
          m_grid[e.m_row][e.m_col] = null;
      }
  }
  
 

  /*
   * Move the given entity from its current location
   * by adding the given number of rows and columns
   * to its current location.*/
 void move(Entity e, int nrows, int ncols) {
        int new_col = normalize(e.m_col + ncols, m_ncols);
        int new_row = normalize(e.m_row + nrows, m_nrows);
        
        if (m_grid[new_row][new_col] == null) {
            removeAt(e);    
            e.m_col = new_col;
            e.m_row = new_row;
            m_grid[new_row][new_col] = e;
        }
    }
    
    
    /* 
     * Normalize a number back to the range [0,length[
     */
    private int normalize(int n, int length) {
        while(n < 0) {
            n = n + length;
        }
        while(n >= length) { // Fixed: was checking against 360 instead of length
            n = n - length;
        }
        return n;
    }

  public Entity entity(int r, int c) {
    r=normalize(r,m_nrows);
    c=normalize(c,m_ncols);
    return m_grid[r][c];
  }

  public Iterator<Entity> entities() {
    return m_entities.iterator();
  }

  public Config config() {
    return m_conf;
  }
  
  public void config(Config c) {
    m_conf = c;
  }

  public Player player() {
    return m_player;
  }
  public void setPlayer(Player player) {
      m_player = player;
  }
  
  public int ncols() {
    return m_ncols;
  }

  public int nrows() {
    return m_nrows;
  }

}
