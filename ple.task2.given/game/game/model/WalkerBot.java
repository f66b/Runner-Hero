package game.model;
import engine.model.*;
public class WalkerBot extends Bot {
    public WalkerBot(Brain b, Entity e) {
        super(b, e);
        delay = 200; // Think every 200ms
    }

    @Override
    public void think(int elapsed) {
        if (cell(Direction.F) == null) {
            move(Direction.F);
        } else if (cell(Direction.L) == null) {
            turn(Direction.L);
        } else if (cell(Direction.R) == null) {
            turn(Direction.R);
        } else if (cell(Direction.B) == null) {
            turn(Direction.B);
        }
        delay = 200; // Reset delay after thinking
    }
} 