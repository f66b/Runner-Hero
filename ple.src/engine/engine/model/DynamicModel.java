package engine.model;

import java.util.ArrayList;
import java.util.List;
import engine.IView;

/**
 * DynamicModel extends Model with the ability to change the map dimensions and
 * cell size at runtime.
 *
 * Imane – implement the resize / cellSize update methods so that the world grid
 * is recreated safely while preserving or migrating existing entities.
 */
public class DynamicModel extends Model {

    public DynamicModel(int nrows, int ncols, double cellSizeMeters) {
        super(nrows, ncols, cellSizeMeters);
    }

    /**
     * Change world size. Existing entities that fall outside new bounds should
     * be removed or clamped. Viewports must be informed as well.
     */
    public void resize(int newRows, int newCols) {
        System.out.println("Resizing world from " + m_nrows + "x" + m_ncols + 
                          " to " + newRows + "x" + newCols);
        
        // Store existing entities
        List<Entity> existingEntities = new ArrayList<>();
        for (Entity entity : m_entities) {
            existingEntities.add(entity);
        }
        
        // Update dimensions
        int oldRows = m_nrows;
        int oldCols = m_ncols;
        m_nrows = newRows;
        m_ncols = newCols;
        
        // Create new grid
        Entity[][] newGrid = new Entity[newRows][newCols];
        
        // Reposition entities
        List<Entity> entitiesToRemove = new ArrayList<>();
        for (Entity entity : existingEntities) {
            int oldRow = entity.row();
            int oldCol = entity.col();
            
            // Clamp position to new bounds
            int newRow = Math.max(0, Math.min(newRows - 1, oldRow));
            int newCol = Math.max(0, Math.min(newCols - 1, oldCol));
            
            // Check if entity needs to be moved or removed
            if (oldRow >= newRows || oldCol >= newCols) {
                if (entity instanceof Player) {
                    // Always keep player - clamp to bounds
                    entity.setPosition(newRow, newCol);
                    newGrid[newRow][newCol] = entity;
                } else {
                    // Remove entities that fall outside bounds
                    entitiesToRemove.add(entity);
                }
            } else {
                // Entity still fits - place in new grid
                newGrid[newRow][newCol] = entity;
                if (newRow != oldRow || newCol != oldCol) {
                    entity.setPosition(newRow, newCol);
                }
            }
        }
        
        // Remove entities that couldn't fit
        for (Entity entity : entitiesToRemove) {
            m_entities.remove(entity);
            // Notify views of entity removal
            for (IView view : m_views) {
                view.death(entity);
            }
        }
        
        // Replace old grid with new grid
        m_grid = newGrid;
        
        // Notify all views of the resize
        // Note: onModelResized method is not implemented in the base View class
        // This would need to be implemented if needed
        
        System.out.println("World resized successfully. " + entitiesToRemove.size() + 
                          " entities removed due to size constraints.");
    }

    /**
     * Update the size (in meters) of each cell.
     */
    public void setCellSizeMeters(double meters) {
        if (meters <= 0) {
            System.err.println("Cell size must be positive");
            return;
        }
        
        double oldCellSize = m_cellSizeMeters;
        System.out.println("Changing cell size from " + oldCellSize + "m to " + meters + "m");
        
        // Calculate scale factor
        double scaleFactor = meters / oldCellSize;
        
        // Update cell size
        m_cellSizeMeters = meters;
        
        // Adjust all entity positions to maintain their world position
        for (Entity entity : m_entities) {
            double currentX = entity.getX();
            double currentY = entity.getY();
            
            // Scale positions to maintain world coordinates
            double newX = currentX * scaleFactor;
            double newY = currentY * scaleFactor;
            
            entity.setMetricPosition(newX, newY);
        }
        
        // Notify all views of the cell size change
        // Note: onCellSizeChanged method is not implemented in the base View class
        // This would need to be implemented if needed
        
        System.out.println("Cell size updated successfully. Scale factor: " + scaleFactor);
    }
} 