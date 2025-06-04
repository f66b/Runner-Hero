package engine.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import engine.IModel;
import engine.view.View;
import engine.IView;

public class Model implements IModel {
  private int m_ncols, m_nrows;
  private Entity[][] m_grid;
  private Player m_player;
  private List<Entity> m_entities;
  private View m_view;
  private Config m_conf;
  private double m_cellSizeMeters = 1.0; // Default 1 meter per cell
  private List<IView> m_views;

  public Model(int nr, int nc) {
    m_ncols = nc;
    m_nrows = nr;
    m_grid = new Entity[nr][nc];
    m_entities = new LinkedList<Entity>();
    m_views = new ArrayList<>();
    m_conf = new Config();
  }
  
  public Model(int nr, int nc, double cellSizeMeters) {
    this(nr, nc);
    m_cellSizeMeters = cellSizeMeters;
  }

  /* 
   * A callback from the constructor of an entity
   * to notify this model of a new entity to add
   * to this model.
   */
  void addAt(Entity e) {
    int r = e.row();
    int c = e.col();
    
    // Normalize coordinates
    r = normalize(r, m_nrows);
    c = normalize(c, m_ncols);
    
    System.out.println("Adding entity at grid position: " + r + "," + c);
    
    // Update entity position
    e.setPosition(r, c);
    
    // Add to grid and entities list
    m_grid[r][c] = e;
    m_entities.add(e);
    
    // If it's a player, set it as the player
    if (e instanceof Player) {
      m_player = (Player) e;
      System.out.println("Added player at metric position: " + e.getX() + "," + e.getY());
    }
    
    // Notify all views of new entity
    System.out.println("Notifying " + m_views.size() + " views of new entity");
    for (IView view : m_views) {
      view.birth(e);
    }
  }

  /*
   * Move the given entity from its current location
   * by adding the given number of rows and columns
   * to its current location.
   */
  public void move(Entity e, int nrows, int ncols) {
    int oldRow = e.row();
    int oldCol = e.col();
    
    // Calculate new position
    int newRow = normalize(oldRow + nrows, m_nrows);
    int newCol = normalize(oldCol + ncols, m_ncols);
    
    // Update grid
    m_grid[oldRow][oldCol] = null;
    m_grid[newRow][newCol] = e;
    
    // Update entity position
    e.setPosition(newRow, newCol);
  }
  
  /*
   * Remove an entity from the model
   */
  public void removeEntity(Entity e) {
    int row = e.row();
    int col = e.col();
    
    // Remove from grid
    if (m_grid[row][col] == e) {
      m_grid[row][col] = null;
    }
    
    // Remove from entities list
    m_entities.remove(e);
    
    // Notify all views of entity removal
    for (IView view : m_views) {
      view.death(e);
    }
  }

  /* 
   * Normalize a number back to the range [0,length[
   */
  private int normalize(int n, int length) {
    while (n < 0) {
      n += length;
    }
    return n % length;
  }

  @Override
  public Entity entity(int r, int c) {
    r = normalize(r, m_nrows);
    c = normalize(c, m_ncols);
    return m_grid[r][c];
  }

  @Override
  public Iterator<Entity> entities() {
    return m_entities.iterator();
  }

  @Override
  public Config config() {
    return m_conf;
  }
  
  @Override
  public void config(Config c) {
    m_conf = c;
  }

  @Override
  public Player player() {
    return m_player;
  }

  @Override
  public int ncols() {
    return m_ncols;
  }

  @Override
  public int nrows() {
    return m_nrows;
  }
  
  @Override
  public double getCellSizeMeters() {
    return m_cellSizeMeters;
  }
  
  @Override
  public double getWorldWidthMeters() {
    return m_ncols * m_cellSizeMeters;
  }
  
  @Override
  public double getWorldHeightMeters() {
    return m_nrows * m_cellSizeMeters;
  }
  
  @Override
  public double gridToMetersX(int col) {
    return col * m_cellSizeMeters + m_cellSizeMeters / 2.0;
  }
  
  @Override
  public double gridToMetersY(int row) {
    return row * m_cellSizeMeters + m_cellSizeMeters / 2.0;
  }
  
  @Override
  public int metersToGridX(double x) {
    return (int)(x / m_cellSizeMeters);
  }
  
  @Override
  public int metersToGridY(double y) {
    return (int)(y / m_cellSizeMeters);
  }
  
  @Override
  public void update(double deltaTime) {
    // Update all entities
    List<Entity> toRemove = new LinkedList<>();
    
    for (Entity entity : m_entities) {
      entity.update(deltaTime);
      
      // Check for expired projectiles
      if (entity instanceof Projectile) {
        Projectile proj = (Projectile) entity;
        if (proj.isExpired()) {
          toRemove.add(proj);
        }
      }
    }
    
    // Remove expired entities
    for (Entity entity : toRemove) {
      removeEntity(entity);
    }
  }

  public void register(IView view) {
    m_views.add(view);
  }
  
  public void unregister(IView view) {
    m_views.remove(view);
  }

}
