package engine.view;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.Color;

import engine.IView;
import engine.model.Model;
import engine.model.Entity;
import oop.graphics.Canvas;
import java.util.Iterator;

public abstract class View implements IView {

  protected Canvas m_canvas;
  protected Model m_model;
  private int m_mouseX = 0;
  private int m_mouseY = 0;
  private double m_viewportX = 0; // In meters
  private double m_viewportY = 0; // In meters
  private double m_zoomLevel = 1.0;
  private double m_pixelsPerMeter = 50.0; // Default scale
  private boolean m_debugMode = false;

  public View(Canvas canvas, Model model) {
    m_canvas = canvas;
    m_model = model;
    // Calculate initial pixels per meter based on canvas size and world size
    m_pixelsPerMeter = Math.min(
      canvas.getWidth() / m_model.getWorldWidthMeters(),
      canvas.getHeight() / m_model.getWorldHeightMeters()
    );
    // Set initial zoom to fit the world nicely
    setInitialZoom();
  }
  
  public void setInitialZoom() {
    // Calculate zoom to show the entire world with some padding
    double worldWidth = m_model.getWorldWidthMeters();
    double worldHeight = m_model.getWorldHeightMeters();
    double canvasWidth = m_canvas.getWidth();
    double canvasHeight = m_canvas.getHeight();
    
    double zoomX = (canvasWidth * 0.9) / (worldWidth * m_pixelsPerMeter);
    double zoomY = (canvasHeight * 0.9) / (worldHeight * m_pixelsPerMeter);
    
    m_zoomLevel = Math.min(zoomX, zoomY);
    m_zoomLevel = Math.max(0.5, Math.min(3.0, m_zoomLevel)); // Apply limits
    
    // Center the viewport on the world
    m_viewportX = (worldWidth - canvasWidth / (m_pixelsPerMeter * m_zoomLevel)) / 2.0;
    m_viewportY = (worldHeight - canvasHeight / (m_pixelsPerMeter * m_zoomLevel)) / 2.0;
  }
  
  @Override
  public void setMousePosition(int x, int y) {
    m_mouseX = x;
    m_mouseY = y;
  }
  
  @Override
  public void setViewportTranslation(double dx, double dy) {
    m_viewportX += dx;
    m_viewportY += dy;
    
    // Clamp viewport to world bounds
    double maxViewportX = m_model.getWorldWidthMeters() - m_canvas.getWidth() / (m_pixelsPerMeter * m_zoomLevel);
    double maxViewportY = m_model.getWorldHeightMeters() - m_canvas.getHeight() / (m_pixelsPerMeter * m_zoomLevel);
    
    m_viewportX = Math.max(0, Math.min(maxViewportX, m_viewportX));
    m_viewportY = Math.max(0, Math.min(maxViewportY, m_viewportY));
  }
  
  @Override
  public void setZoomLevel(double zoom) {
    // Limit zoom level between 0.5 and 3.0
    m_zoomLevel = Math.max(0.5, Math.min(3.0, zoom));
  }
  
  @Override
  public double getZoomLevel() {
    return m_zoomLevel;
  }
  
  @Override
  public double getViewportX() {
    return m_viewportX;
  }
  
  @Override
  public double getViewportY() {
    return m_viewportY;
  }

  @Override
  public void focus(int px, int py) {
    // Center the view on a specific pixel point
    double metersX = pixelToMetersX(px);
    double metersY = pixelToMetersY(py);
    m_viewportX = metersX - (m_canvas.getWidth() / 2.0) / (m_pixelsPerMeter * m_zoomLevel);
    m_viewportY = metersY - (m_canvas.getHeight() / 2.0) / (m_pixelsPerMeter * m_zoomLevel);
    
    // Apply viewport bounds
    setViewportTranslation(0, 0); // This will clamp the values
  }
  
  @Override
  public double pixelToMetersX(int pixelX) {
    return m_viewportX + pixelX / (m_pixelsPerMeter * m_zoomLevel);
  }
  
  @Override
  public double pixelToMetersY(int pixelY) {
    return m_viewportY + pixelY / (m_pixelsPerMeter * m_zoomLevel);
  }
  
  @Override
  public int metersToPixelX(double metersX) {
    return (int)((metersX - m_viewportX) * m_pixelsPerMeter * m_zoomLevel);
  }
  
  @Override
  public int metersToPixelY(double metersY) {
    return (int)((metersY - m_viewportY) * m_pixelsPerMeter * m_zoomLevel);
  }
  
  @Override
  public double getMouseMetersX() {
    return pixelToMetersX(m_mouseX);
  }
  
  @Override
  public double getMouseMetersY() {
    return pixelToMetersY(m_mouseY);
  }
  
  public void setDebugMode(boolean debug) {
    m_debugMode = debug;
  }
  
  public boolean isDebugMode() {
    return m_debugMode;
  }
  
  protected double getPixelsPerMeter() {
    return m_pixelsPerMeter;
  }

  @Override
  public void paint(Canvas canvas, Graphics2D g) {
    // Save original transform
    AffineTransform originalTransform = g.getTransform();
    
    // Clear background
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    
    // Apply viewport transformations
    g.translate(-m_viewportX * m_pixelsPerMeter * m_zoomLevel, 
                -m_viewportY * m_pixelsPerMeter * m_zoomLevel);
    g.scale(m_zoomLevel, m_zoomLevel);
    
    // Always draw grid
    drawGrid(g, canvas);
    
    // Draw all entities using game-specific rendering
    Iterator<Entity> entities = m_model.entities();
    while (entities.hasNext()) {
      Entity entity = entities.next();
      drawEntity(g, canvas, entity);
    }
    
    // Restore original transform for UI elements
    g.setTransform(originalTransform);
    
    // Draw UI elements (not affected by viewport)
    if (m_debugMode) {
      drawDebugInfo(g);
    }
  }
  
  private void drawGrid(Graphics2D g, Canvas canvas) {
    g.setColor(Color.LIGHT_GRAY);
    
    double cellSizePixels = m_model.getCellSizeMeters() * m_pixelsPerMeter;
    
    // Calculate visible grid bounds
    int startCol = Math.max(0, (int)(m_viewportX / m_model.getCellSizeMeters()));
    int endCol = Math.min(m_model.ncols(), 
      (int)((m_viewportX + canvas.getWidth() / (m_pixelsPerMeter * m_zoomLevel)) / m_model.getCellSizeMeters()) + 1);
    int startRow = Math.max(0, (int)(m_viewportY / m_model.getCellSizeMeters()));
    int endRow = Math.min(m_model.nrows(), 
      (int)((m_viewportY + canvas.getHeight() / (m_pixelsPerMeter * m_zoomLevel)) / m_model.getCellSizeMeters()) + 1);
    
    // Draw vertical lines
    for (int i = startCol; i <= endCol; i++) {
      double x = i * cellSizePixels;
      g.drawLine((int)x, (int)(startRow * cellSizePixels), 
                 (int)x, (int)(endRow * cellSizePixels));
    }
    
    // Draw horizontal lines
    for (int i = startRow; i <= endRow; i++) {
      double y = i * cellSizePixels;
      g.drawLine((int)(startCol * cellSizePixels), (int)y, 
                 (int)(endCol * cellSizePixels), (int)y);
    }
  }
  
  // Abstract method for game-specific entity rendering
  protected abstract void drawEntity(Graphics2D g, Canvas canvas, Entity entity);
  
  private void drawDebugInfo(Graphics2D g) {
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.PLAIN, 12));
    int y = 20;
    g.drawString(String.format("Mouse: (%.2fm, %.2fm)", getMouseMetersX(), getMouseMetersY()), 10, y);
    y += 15;
    g.drawString(String.format("Zoom: %.2fx", m_zoomLevel), 10, y);
    y += 15;
    g.drawString(String.format("Viewport: (%.2fm, %.2fm)", m_viewportX, m_viewportY), 10, y);
    y += 15;
    g.drawString(String.format("World: %.1fm x %.1fm", m_model.getWorldWidthMeters(), m_model.getWorldHeightMeters()), 10, y);
    
    // Show controls
    y += 30;
    g.drawString("Controls:", 10, y);
    y += 15;
    g.drawString("  UP: Move forward", 10, y);
    y += 15;
    g.drawString("  DOWN: Stop", 10, y);
    y += 15;
    g.drawString("  Mouse: Auto-rotate to cursor", 10, y);
    y += 15;
    g.drawString("  Left Click: Rotate CCW", 10, y);
    y += 15;
    g.drawString("  Right Click: Rotate CW", 10, y);
    y += 15;
    g.drawString("  Space: Shoot", 10, y);
    y += 15;
    g.drawString("  Ctrl+Arrows: Pan view", 10, y);
    y += 15;
    g.drawString("  +/-: Zoom", 10, y);
    y += 15;
    g.drawString("  D: Toggle debug mode", 10, y);
  }
}