package game;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.Color;

import engine.view.View;
import engine.model.Model;
import engine.model.Player;
import engine.model.Entity;
import engine.model.Projectile;
import oop.graphics.Canvas;

public class View0 extends View {
  
  public View0(Canvas canvas, Model model) {
    super(canvas, model);
  }
  
  @Override
  protected void drawEntity(Graphics2D g, Canvas canvas, Entity entity) {
    double x = entity.getX() * getPixelsPerMeter();
    double y = entity.getY() * getPixelsPerMeter();
    double size = m_model.getCellSizeMeters() * getPixelsPerMeter();
    
    // Draw differently based on entity type
    if (entity instanceof Player) {
      drawPlayer(g, (Player)entity, x, y, size);
    } else if (entity instanceof Projectile) {
      drawProjectile(g, (Projectile)entity, x, y, size);
    } else {
      // Draw other entities as green circles
      drawGenericEntity(g, entity, x, y, size);
    }
  }
  
  private void drawPlayer(Graphics2D g, Player player, double x, double y, double size) {
    g.setColor(Color.BLUE);
    
    // Calculate triangle size
    double triangleSize = size * 0.4;
    
    // Create triangle polygon
    Polygon triangle = createTriangle(triangleSize);
    
    // Apply rotation and translation
    paintPlayer(g, player, x, y, triangle);
    
    // Draw direction indicator
    g.setColor(Color.RED);
    double angle = Math.toRadians(player.orientation());
    int lineLength = (int)(size * 0.5);
    g.drawLine((int)x, (int)y, 
               (int)(x + Math.cos(angle) * lineLength), 
               (int)(y + Math.sin(angle) * lineLength));
    
    // Draw mouse cursor "Hello" text near player if in debug mode
    if (isDebugMode()) {
      drawHelloText(g, x, y);
    }
  }
  
  private void drawProjectile(Graphics2D g, Projectile projectile, double x, double y, double size) {
    g.setColor(Color.ORANGE);
    
    // Draw projectile as a small circle
    int radius = (int)(size * 0.15);
    g.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
    
    // Draw motion trail
    g.setColor(new Color(255, 200, 0, 128)); // Semi-transparent orange
    double angle = Math.toRadians(projectile.orientation());
    int trailLength = (int)(size * 0.3);
    g.drawLine((int)x, (int)y, 
               (int)(x - Math.cos(angle) * trailLength), 
               (int)(y - Math.sin(angle) * trailLength));
  }
  
  private void drawGenericEntity(Graphics2D g, Entity entity, double x, double y, double size) {
    g.setColor(Color.GREEN);
    int radius = (int)(size * 0.4);
    g.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
    
    // Draw a small border
    g.setColor(Color.DARK_GRAY);
    g.drawOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
  }
  
  private Polygon createTriangle(double size) {
    int[] xPoints = {(int)size, (int)(-size/2), (int)(-size/2)};
    int[] yPoints = {0, (int)(-size/2), (int)(size/2)};
    return new Polygon(xPoints, yPoints, 3);
  }
  
  private void paintPlayer(Graphics2D g, Player p, double x, double y, Polygon pg) {
    double d = p.orientation();
    double rot = Math.toRadians(d);
    AffineTransform saved = g.getTransform();
    g.translate(x, y);
    g.rotate(rot);
    g.fillPolygon(pg);
    
    // Add a border to the player
    g.setColor(Color.DARK_GRAY);
    g.drawPolygon(pg);
    
    g.setTransform(saved);
  }
  
  private void drawHelloText(Graphics2D g, double playerX, double playerY) {
    // Position the hello text relative to the player in world coordinates
    int textX = (int)(playerX + 30);
    int textY = (int)(playerY - 30);
    
    // Draw blue background
    g.setColor(Color.BLUE);
    g.fillRect(textX, textY - 20, 50, 25);
    
    // Draw yellow text
    g.setColor(Color.YELLOW);
    g.setFont(new Font("Arial", Font.BOLD, 16));
    g.drawString("Hello", textX + 5, textY);
  }
}