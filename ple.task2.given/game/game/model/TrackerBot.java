package game.model;
import engine.model.*;
public class TrackerBot extends Bot {
    public TrackerBot(Brain b, Entity e) {
        super(b, e);
        delay = 100; // Think every 100ms for faster reaction
    }

    @Override
    public void think(int elapsed) {
        Entity adv = closest(Category.Adversary);
        if (adv == null) {
            delay = 200; // Longer delay when no adversary found
            return;
        }

        // Calculate direction to adversary
        int dx = adv.col() - e.col();
        int dy = adv.row() - e.row();
        
        // Move in the cardinal direction that gets us closer
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                move(Direction.E);
            } else {
                move(Direction.W);
            }
        } else {
            if (dy > 0) {
                move(Direction.S);
            } else {
                move(Direction.N);
            }
        }
        
        delay = 100; // Reset to fast reaction time
    }
} 
