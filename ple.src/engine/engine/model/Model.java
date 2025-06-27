package engine.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import engine.IModel;
import engine.view.View;
import engine.IView;

public class Model implements IModel {
  public int m_ncols, m_nrows;
  public Entity[][] m_grid;
  private Player m_player;
  public List<Entity> m_entities;
  private View m_view;
  private Config m_conf;
  public double m_cellSizeMeters = 1.0; // Default 1 meter per cell
  public List<IView> m_views;
  private double m_worldScrollSpeed = 4.0; // Current world scroll speed (updated by game)

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
   * 
   * DEPRECATED: Use moveByPixels for fluid movement
   */
  public void move(Entity e, int nrows, int ncols) {
    // Convert grid movement to pixel movement
    double pixelDx = ncols * m_cellSizeMeters;
    double pixelDy = nrows * m_cellSizeMeters;
    moveByPixels(e, pixelDx, pixelDy);
  }
  
  /*
   * Move the given entity by the specified pixel amounts (in meters)
   * This provides fluid movement instead of discrete grid movement
   */
  public void moveByPixels(Entity e, double deltaX, double deltaY) {
    // Get current position
    double currentX = e.getX();
    double currentY = e.getY();
    
    // Calculate new position
    double newX = currentX + deltaX;
    double newY = currentY + deltaY;
    
    // Handle world wrapping if configured
    if (m_conf != null && m_conf.tore) {
      // Wrap around world boundaries
      while (newX < 0) newX += getWorldWidthMeters();
      while (newX >= getWorldWidthMeters()) newX -= getWorldWidthMeters();
      while (newY < 0) newY += getWorldHeightMeters();
      while (newY >= getWorldHeightMeters()) newY -= getWorldHeightMeters();
    } else {
      // For runner-style games, allow entities to move beyond boundaries
      // Only clamp Y coordinates to keep entities within vertical bounds
      // Let X coordinates go negative or beyond world width for proper off-screen removal
      newY = Math.max(0, Math.min(getWorldHeightMeters() - 0.01, newY));
      // Don't clamp X - allow entities to move off-screen for removal
    }
    
    // Update entity's metric position (this will automatically update grid position)
    e.setMetricPosition(newX, newY);
  }
  
  /*
   * Move entity by a specific velocity and direction over time
   */
  public void moveByVelocity(Entity e, double velocity, double angleDegrees, double deltaTime) {
    double angleRadians = Math.toRadians(angleDegrees);
    double deltaX = Math.cos(angleRadians) * velocity * deltaTime;
    double deltaY = Math.sin(angleRadians) * velocity * deltaTime;
    moveByPixels(e, deltaX, deltaY);
  }
  
  /*
   * Internal method to update grid when entity changes position
   * Called by Entity.setMetricPosition()
   */
  void updateEntityGridPosition(Entity e, int oldRow, int oldCol, int newRow, int newCol) {
    // Remove from old grid position (if it was within bounds)
    if (oldRow >= 0 && oldRow < m_nrows && oldCol >= 0 && oldCol < m_ncols) {
      if (m_grid[oldRow][oldCol] == e) {
        m_grid[oldRow][oldCol] = null;
      }
    }
    
    // Add to new grid position (only if within bounds)
    // Entities can exist outside the grid for off-screen movement
    if (newRow >= 0 && newRow < m_nrows && newCol >= 0 && newCol < m_ncols) {
      // Note: This might overwrite another entity - you may want collision detection here
      m_grid[newRow][newCol] = e;
    }
    // If entity is outside grid bounds, it's not placed in the grid but still exists in entities list
  }
  
  /*
   * Remove an entity from the model
   */
  public void removeEntity(Entity e) {
    if (e == null) {
      System.err.println("WARNING: Attempted to remove null entity");
      return;
    }
    
    int row = e.row();
    int col = e.col();
    String entityType = e.getClass().getSimpleName();
    double x = e.getX();
    
    // Remove from grid (if it's within bounds)
    if (row >= 0 && row < m_nrows && col >= 0 && col < m_ncols) {
      if (m_grid[row][col] == e) {
        m_grid[row][col] = null;
      }
    }
    
    // Remove from entities list
    boolean removed = m_entities.remove(e);
    
    if (removed) {
      System.out.println("REMOVED: " + entityType + " at X=" + String.format("%.2f", x) + 
                        " (grid: " + row + "," + col + "). Entities remaining: " + m_entities.size());
    } else {
      System.err.println("WARNING: Failed to remove " + entityType + " from entities list");
    }
    
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
    // Update all entities using a snapshot to avoid concurrent modification
    List<Entity> toRemove = new LinkedList<>();
    List<Entity> entitiesSnapshot = new ArrayList<>(m_entities);
    for (Entity entity : entitiesSnapshot) {
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
  
  /**
   * Get current world scroll speed
   */
  public double getWorldScrollSpeed() {
    return m_worldScrollSpeed;
  }
  
  /**
   * Set current world scroll speed (called by game)
   */
  public void setWorldScrollSpeed(double speed) {
    m_worldScrollSpeed = speed;
  }
}