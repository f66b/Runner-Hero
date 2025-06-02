package engine;

import java.awt.Graphics2D;
import oop.graphics.Canvas;

/**
 * IView interface defines the contract for game rendering.
 * Views handle conversion from metric coordinates to pixel coordinates.
 */
public interface IView {
    // Core rendering
    void paint(Canvas canvas, Graphics2D g);
    
    // Mouse position handling
    void setMousePosition(int x, int y);
    void focus(int px, int py);
    
    // Viewport control
    void setViewportTranslation(double dx, double dy);
    void setZoomLevel(double zoom);
    double getZoomLevel();
    double getViewportX();
    double getViewportY();
    
    // Coordinate conversion
    double pixelToMetersX(int pixelX);
    double pixelToMetersY(int pixelY);
    int metersToPixelX(double metersX);
    int metersToPixelY(double metersY);
    
    // Get mouse position in meters
    double getMouseMetersX();
    double getMouseMetersY();
}
