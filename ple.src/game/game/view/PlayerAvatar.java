package game.view;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import engine.view.Avatar;
import engine.view.View;
import engine.model.Entity;
import engine.model.Player;
import engine.model.Stunt;

public class PlayerAvatar extends Avatar {
    private static final int CELL_SIZE = 1; // 1 meter per cell

    public PlayerAvatar(View view, Entity entity) {
        super(view, entity);
    }

    @Override
    public void render(Graphics2D g) {
        Player player = (Player)entity;
        Stunt.Action action = player.stunt.action();
        int progress = player.stunt.progress();
        
        // Base position in pixels
        double baseX = player.getX() * view.getPixelsPerMeter();
        double baseY = player.getY() * view.getPixelsPerMeter();
        double size = view.getModel().getCellSizeMeters() * view.getPixelsPerMeter() * 0.4;
        
        // Create triangle polygon
        Polygon triangle = createTriangle(size);
        
        // Save current transform
        AffineTransform saved = g.getTransform();
        
        // Calculate interpolated position and rotation
        double x = baseX;
        double y = baseY;
        double angle = player.orientation();
        
        if (action != null) {
            float p = progress / 100f;
            
            if (action.kind() == Stunt.MOVE) {
                // Get movement parameters from Action interface
                int dr = action.getDeltaRow();
                int dc = action.getDeltaCol();
                
                // Calculate pixel offsets
                double dx = dc * CELL_SIZE * view.getPixelsPerMeter() * p;
                double dy = dr * CELL_SIZE * view.getPixelsPerMeter() * p;
                
                // Apply interpolated position
                x = baseX + dx;
                y = baseY + dy;
            } 
            else if (action.kind() == Stunt.ROTATE) {
                // Get rotation parameters from Action interface
                double startAngle = action.getStartAngle();
                double targetAngle = action.getTargetAngle();
                
                // Interpolate angle
                angle = startAngle + (targetAngle - startAngle) * p;
            }
        }
        
        // Apply position and rotation
        g.translate(x, y);
        g.rotate(Math.toRadians(angle));
        
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