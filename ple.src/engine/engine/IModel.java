package engine;

import java.util.Iterator;
import engine.model.Entity;
import engine.model.Player;
import engine.model.Config;

/**
 * IModel interface defines the contract for game world models.
 * The model uses metric-based coordinates (meters) internally.
 */
public interface IModel {
    // Grid dimensions
    int ncols();
    int nrows();
    
    // Metric-based dimensions
    double getCellSizeMeters();
    double getWorldWidthMeters();
    double getWorldHeightMeters();
    
    // Entity management
    Entity entity(int r, int c);
    Iterator<Entity> entities();
    Player player();
    
    // Configuration
    Config config();
    void config(Config c);
    
    // Coordinate conversion
    double gridToMetersX(int col);
    double gridToMetersY(int row);
    int metersToGridX(double x);
    int metersToGridY(double y);
    
    // Update mechanism
    void update(double deltaTime);
}
