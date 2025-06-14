package game.view;

import java.awt.Graphics2D;
import java.awt.Color;
import engine.view.Avatar;
import engine.view.View;
import engine.model.Entity;
import engine.model.Projectile;

public class ProjectileAvatar extends Avatar {
    public ProjectileAvatar(View view, Entity entity) {
        super(view, entity);
    }

    @Override
    public void render(Graphics2D g) {
        Projectile projectile = (Projectile)entity;
        double x = projectile.getX() * view.getPixelsPerMeter();
        double y = projectile.getY() * view.getPixelsPerMeter();
        double size = view.getModel().getCellSizeMeters() * view.getPixelsPerMeter() * 0.15;
        
        // Draw projectile as a small circle
        g.setColor(Color.ORANGE);
        int radius = (int)size;
        g.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
        
        // Draw motion trail
        g.setColor(new Color(255, 200, 0, 128)); // Semi-transparent orange
        double angle = Math.toRadians(projectile.orientation());
        int trailLength = (int)(size * 2);
        g.drawLine((int)x, (int)y, 
                   (int)(x - Math.cos(angle) * trailLength), 
                   (int)(y - Math.sin(angle) * trailLength));
    }
} 