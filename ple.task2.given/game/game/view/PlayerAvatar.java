package game.view;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import engine.view.Avatar;
import engine.view.View;
import engine.model.Entity;
import engine.model.Player;

public class PlayerAvatar extends Avatar {
    public PlayerAvatar(View view, Entity entity) {
        super(view, entity);
    }

    @Override
    public void render(Graphics2D g) {
        Player player = (Player)entity;
        double x = player.getX() * view.getPixelsPerMeter();
        double y = player.getY() * view.getPixelsPerMeter();
        double size = view.getModel().getCellSizeMeters() * view.getPixelsPerMeter() * 0.4;
        
        // Create triangle polygon
        Polygon triangle = createTriangle(size);
        
        // Save current transform
        AffineTransform saved = g.getTransform();
        
        // Apply player position and rotation
        g.translate(x, y);
        g.rotate(Math.toRadians(player.orientation()));
        
        // Draw the player triangle
        g.setColor(Color.BLUE);
        g.fillPolygon(triangle);
        
        // Add a border
        g.setColor(Color.DARK_GRAY);
        g.drawPolygon(triangle);
        
        // Draw direction indicator
        g.setColor(Color.RED);
        int lineLength = (int)(size * 1.25);
        g.drawLine(0, 0, lineLength, 0);
        
        // Restore transform
        g.setTransform(saved);
    }
    
    private Polygon createTriangle(double size) {
        int[] xPoints = {(int)size, (int)(-size/2), (int)(-size/2)};
        int[] yPoints = {0, (int)(-size/2), (int)(size/2)};
        return new Polygon(xPoints, yPoints, 3);
    }
} 