package game.view;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.model.Entity;
import engine.model.Projectile;
import engine.view.Avatar;
import engine.view.View;

public class BotAvatar extends Avatar {
    public BotAvatar(View view, Entity entity) {
        super(view, entity);
    }

    @Override
    public void render(Graphics2D g) {
        Projectile projectile = (Projectile)entity;
        double x = projectile.getX() * view.getPixelsPerMeter();
        double y = projectile.getY() * view.getPixelsPerMeter();
        double size = view.getModel().getCellSizeMeters() * view.getPixelsPerMeter() * 0.15;
        
        // Draw projectile as a small circle
        g.setColor(Color.RED);
        int radius = (int)size;
        g.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
        
       
    }
    
}