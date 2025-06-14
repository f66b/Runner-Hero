package game.model;
import engine.model.*;

public class WalkerBot extends Bot {
    private static final int RETRY_DELAY = 50; // ms to wait before retrying a failed move
    private static final int NORMAL_DELAY = 200; // Normal thinking delay

    public WalkerBot(Brain b, Entity e) {
        super(b, e);
        delay = NORMAL_DELAY; // Think every 200ms
    }

    @Override
    public void think(int elapsed) {
        // Try to move forward first
        if (cell(Direction.F) == null) {
            move(Direction.F);
        }
        // If can't move forward, try turning left
        else if (cell(Direction.L) == null) {
            turn(Direction.L);
        }
        // If can't turn left, try turning right
        else if (cell(Direction.R) == null) {
            turn(Direction.R);
        }
        // If all else fails, try turning around
        else if (cell(Direction.B) == null) {
            turn(Direction.B);
        }

        // Reset to normal delay after thinking
        delay = NORMAL_DELAY;
    }
}