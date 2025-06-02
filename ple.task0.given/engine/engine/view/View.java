package engine.view;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Player;
import oop.graphics.Canvas;

public class View {
    private Canvas m_canvas;
    private Model m_model;
    private int m_mouseX = 0;
    private int m_mouseY = 0;
    
    // Viewport properties
    private double m_zoom = 1.0;
    private int m_viewportX = 0;
    private int m_viewportY = 0;
    private final double ZOOM_FACTOR = 1.2;
    private final double MIN_ZOOM = 0.1;
    private final double MAX_ZOOM = 5.0;
    
    public View(Canvas canvas, Model model) {
        m_canvas = canvas;
        m_model = model;
    }
    
    public void setMousePosition(int x, int y) {
        m_mouseX = x;
        m_mouseY = y;
    }
    
    // Viewport control methods
    public void zoomIn() {
        if (m_zoom < MAX_ZOOM) {
            m_zoom *= ZOOM_FACTOR;
        }
    }
    
    public void zoomOut() {
        if (m_zoom > MIN_ZOOM) {
            m_zoom /= ZOOM_FACTOR;
        }
    }
    
    public void zoomOrigin() {
        // Reset zoom to show full grid
        m_zoom = 1.0;
        m_viewportX = 0;
        m_viewportY = 0;
        
        // Optional: Calculate optimal zoom to fit entire grid in view
        if (m_canvas != null && m_model != null) {
            int canvasWidth = m_canvas.getWidth();
            int canvasHeight = m_canvas.getHeight();
            
            // Calculate what zoom level would show the entire grid
            int gridWidth = canvasWidth; // Grid already fits canvas width
            int gridHeight = canvasHeight; // Grid already fits canvas height
            
            // Since the grid is designed to fit the canvas, zoom = 1.0 shows full view
            // But we could also calculate a zoom that shows some padding around the grid:
            // double zoomX = canvasWidth / (double)(gridWidth * 1.1); // 10% padding
            // double zoomY = canvasHeight / (double)(gridHeight * 1.1);
            // m_zoom = Math.min(zoomX, zoomY);
        }
    }
    
    
    public void translateViewport(int deltaX, int deltaY) {
        m_viewportX += deltaX * 20; // Multiply by 20 for smoother translation
        m_viewportY += deltaY * 20;
    }
    
    public void paint(Canvas canvas, Graphics2D g) {
        // Save original transform
        AffineTransform originalTransform = g.getTransform();
        
        // Apply viewport transformations
        g.translate(-m_viewportX, -m_viewportY);
        g.scale(m_zoom, m_zoom);
        
        // Clear the canvas (in transformed space)
        g.setColor(Color.WHITE);
        int canvasWidth = (int)(canvas.getWidth() / m_zoom);
        int canvasHeight = (int)(canvas.getHeight() / m_zoom);
        g.fillRect(m_viewportX, m_viewportY, canvasWidth + Math.abs(m_viewportX), canvasHeight + Math.abs(m_viewportY));
        
        // Draw grid
        drawGrid(g, canvas);
        
        // Draw all entities
        Iterator<Entity> entities = m_model.entities();
        while (entities.hasNext()) {
            Entity entity = entities.next();
            drawEntity(g, canvas, entity);
        }
        
        // Restore original transform for UI elements
        g.setTransform(originalTransform);
        
        // Draw "hello" text following the cursor (not affected by viewport)
        drawHelloText(g);
        
        // Draw viewport info
        drawViewportInfo(g, canvas);
    }
    
    private void drawGrid(Graphics2D g, Canvas canvas) {
        g.setColor(Color.LIGHT_GRAY);
        
        int cellWidth = canvas.getWidth() / m_model.ncols();
        int cellHeight = canvas.getHeight() / m_model.nrows();
        
        // Calculate visible area
        int startCol = Math.max(0, m_viewportX / cellWidth - 1);
        int endCol = Math.min(m_model.ncols(), (m_viewportX + (int)(canvas.getWidth() / m_zoom)) / cellWidth + 2);
        int startRow = Math.max(0, m_viewportY / cellHeight - 1);
        int endRow = Math.min(m_model.nrows(), (m_viewportY + (int)(canvas.getHeight() / m_zoom)) / cellHeight + 2);
        
        // Draw vertical lines
        for (int i = startCol; i <= endCol; i++) {
            int x = i * cellWidth;
            g.drawLine(x, startRow * cellHeight, x, endRow * cellHeight);
        }
        
        // Draw horizontal lines
        for (int i = startRow; i <= endRow; i++) {
            int y = i * cellHeight;
            g.drawLine(startCol * cellWidth, y, endCol * cellWidth, y);
        }
    }
    
    private void drawEntity(Graphics2D g, Canvas canvas, Entity entity) {
        int cellWidth = canvas.getWidth() / m_model.ncols();
        int cellHeight = canvas.getHeight() / m_model.nrows();
        
        int x = entity.col() * cellWidth;
        int y = entity.row() * cellHeight;
        
        // Draw differently based on entity type
        if (entity instanceof Player) {
            drawPlayer(g, entity, x, y, cellWidth, cellHeight);
        } else {
            // Draw other entities as green rectangles
            g.setColor(Color.GREEN);
            g.fillRect(x + 1, y + 1, cellWidth - 2, cellHeight - 2);
        }
    }
    
    private void drawPlayer(Graphics2D g, Entity player, int x, int y, int cellWidth, int cellHeight) {
        g.setColor(Color.BLUE);
        
        int centerX = x + cellWidth / 2;
        int centerY = y + cellHeight / 2;
        
        // Calculate triangle size (adjust for zoom)
        int triangleSize = (int)(Math.min(cellWidth, cellHeight) / 3);
        
        // Get orientation angle (convert to radians)
        double angle = Math.toRadians(player.orientation());
        
        // Create triangle points relative to center
        // Base triangle points (pointing right at 0 degrees)
        int[] baseX = {triangleSize, -triangleSize/2, -triangleSize/2};
        int[] baseY = {0, -triangleSize/2, triangleSize/2};
        
        // Rotate triangle points based on orientation
        int[] rotatedX = new int[3];
        int[] rotatedY = new int[3];
        
        for (int i = 0; i < 3; i++) {
            // Rotate around origin
            double rotX = baseX[i] * Math.cos(angle) - baseY[i] * Math.sin(angle);
            double rotY = baseX[i] * Math.sin(angle) + baseY[i] * Math.cos(angle);
            
            // Translate to center position
            rotatedX[i] = centerX + (int)rotX;
            rotatedY[i] = centerY + (int)rotY;
        }
        
        // Create and fill the triangle
        Polygon triangle = new Polygon(rotatedX, rotatedY, 3);
        g.fillPolygon(triangle);
        
        // Draw triangle outline
        g.setColor(Color.DARK_GRAY);
        g.drawPolygon(triangle);
    }
    
    private void drawHelloText(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Draw "hello" text at mouse position with slight offset
        g.drawString("hello", m_mouseX + 10, m_mouseY - 10);
    }
    
    private void drawViewportInfo(Graphics2D g, Canvas canvas) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Display zoom and viewport info
        String zoomInfo = String.format("Zoom: %.1fx", m_zoom);
        String viewportInfo = String.format("Viewport: (%d, %d)", m_viewportX, m_viewportY);
        String controlsInfo = "Controls: Ctrl+Arrows=Move, +/-=Zoom";
        
        g.drawString(zoomInfo, 10, 20);
        g.drawString(viewportInfo, 10, 35);
        g.drawString(controlsInfo, 10, canvas.getHeight() - 10);
    }
}