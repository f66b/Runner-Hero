package engine.model;
import engine.IBrain;
import engine.model.*;
public abstract class Bot implements IBrain.IBot {
    protected Brain b;
    protected Entity e;
    private boolean wait;
    protected int delay; // in ms

    protected Bot(Brain b, Entity e) {
        this.b = b;
        this.e = e;
        if(e != null) {
        	e.bot = this;
        }
        this.delay = 200; // Default delay of 200ms
        this.wait = false;
    }

    @Override
    public Entity entity() {
        return e;
    }

    public void _think(int elapsed) {
        if (wait) {
            delay -= elapsed;
            wait = (delay > 0);
        }
        if (!wait) {
            think(elapsed);
            wait = (delay > 0);
        }
    }

    @Override
    public abstract void think(int elapsed);

    // Utility methods for actions
    protected void move(Direction d) {
        if (e.stunt != null) {
            int dx = 0, dy = 0;
            double angle = d.isRelative() ? 
                (e.orientation() + d.degrees()) % 360 : 
                d.degrees();
            
            // Convert angle to dx, dy
            if (angle == 0) dx = 1;
            else if (angle == 90) dy = 1;
            else if (angle == 180) dx = -1;
            else if (angle == 270) dy = -1;
            
            e.stunt.move(dy, dx);
        }
    }

    protected void turn(Direction d) {
        if (e.stunt != null) {
            double newAngle = d.isRelative() ? 
                (e.orientation() + d.degrees()) % 360 : 
                d.degrees();
            e.stunt.rotate(newAngle);
        }
    }

    // Utility methods for conditions
    protected Entity cell(Direction d) {
        if (e.m_model == null) return null;
        
        double angle = d.isRelative() ? 
            (e.orientation() + d.degrees()) % 360 : 
            d.degrees();
            
        int row = e.row();
        int col = e.col();
        
        if (angle == 0) col++;
        else if (angle == 90) row++;
        else if (angle == 180) col--;
        else if (angle == 270) row--;
        
        return e.m_model.entity(row, col);
    }

    protected Entity cell(Direction d, Category c) {
        Entity entity = cell(d);
        if (entity != null) {
            // Note: Category system is not implemented in Entity
            // This method would need the category system to be implemented
            return entity;
        }
        return null;
    }

    protected Entity closest(Category c) {
        if (e.m_model == null) return null;
        
        Entity closest = null;
        double minDist = Double.MAX_VALUE;
        
        java.util.Iterator<Entity> iter = e.m_model.entities();
        while (iter.hasNext()) {
            Entity other = iter.next();
            if (other != e) {
                // Note: Category system is not implemented in Entity
                // This method would need the category system to be implemented
                double dist = Math.sqrt(
                    Math.pow(other.row() - e.row(), 2) + 
                    Math.pow(other.col() - e.col(), 2)
                );
                if (dist < minDist) {
                    minDist = dist;
                    closest = other;
                }
            }
        }
        
        return closest;
    }
} 