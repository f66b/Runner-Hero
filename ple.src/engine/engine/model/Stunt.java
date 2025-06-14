package engine.model;

public abstract class Stunt {
    protected Model m;
    protected Entity e;
    protected Action action;
    protected int progress; // [0-100]%

    // Action kinds
    public static final int MOVE = 0;
    public static final int ROTATE = 1;

    public interface Action {
        int kind();
        void tick(int elapsed);
        boolean isDone();
        
        // For MOVE actions
        default int getDeltaRow() { return 0; }
        default int getDeltaCol() { return 0; }
        
        // For ROTATE actions
        default double getStartAngle() { return 0; }
        default double getTargetAngle() { return 0; }
    }

    protected Stunt(Model m, Entity e) {
        this.e = e;
        this.m = m;
        e.stunt = this;
    }

    public Action action() {
        return action;
    }

    public int progress() {
        return progress;
    }

    public boolean move(int dr, int dc) {
        if (action != null) {
            return false;
        }
        action = new Motion(dr, dc);
        progress = 0;
        return true;
    }

    public boolean rotate(double angle) {
        if (action != null) {
            return false;
        }
        action = new Rotation(angle);
        progress = 0;
        return true;
    }

    public void tick(int elapsedMs) {
        if (action == null) {
            e.bot.think(elapsedMs);
        } else {
            action.tick(elapsedMs);
            if (action.isDone()) {
                action = null;
            }
        }
    }

    private class Motion implements Action {
        private final int dr;
        private final int dc;
        private final int duration = 500; // 500ms for movement
        private int elapsed = 0;
        private int step = 0;

        Motion(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
        }

        @Override
        public int kind() {
            return MOVE;
        }

        @Override
        public void tick(int elapsedMs) {
            elapsed += elapsedMs;
            progress = Math.min(100, elapsed * 100 / duration);

            if (elapsed < duration / 2) {
                // Wait for halfway point
                return;
            }

            if (step == 0) {
                // Execute move at halfway point
                m.move(e, dr, dc);
                step = 1;
            }
        }

        @Override
        public boolean isDone() {
            return elapsed >= duration;
        }
        
        @Override
        public int getDeltaRow() {
            return dr;
        }
        
        @Override
        public int getDeltaCol() {
            return dc;
        }
    }

    private class Rotation implements Action {
        private final double targetAngle;
        private final int duration = 500; // 500ms for rotation
        private int elapsed = 0;
        private int step = 0;
        private final double startAngle;

        Rotation(double angle) {
            this.targetAngle = angle;
            this.startAngle = e.orientation();
        }

        @Override
        public int kind() {
            return ROTATE;
        }

        @Override
        public void tick(int elapsedMs) {
            elapsed += elapsedMs;
            progress = Math.min(100, elapsed * 100 / duration);

            if (elapsed < duration / 2) {
                // Smooth rotation until halfway point
                double t = (double)elapsed / (duration / 2);
                double currentAngle = startAngle + (targetAngle - startAngle) * t;
                e.face(currentAngle);
                return;
            }

            if (step == 0) {
                // Set final orientation at halfway point
                e.face(targetAngle);
                step = 1;
            }
        }

        @Override
        public boolean isDone() {
            return elapsed >= duration;
        }
        
        @Override
        public double getStartAngle() {
            return startAngle;
        }
        
        @Override
        public double getTargetAngle() {
            return targetAngle;
        }
    }

    // Abstract methods that must be implemented by subclasses
    public abstract void rotateLeft();
    public abstract void rotateRight();
    public abstract void left();
    public abstract void right();
    public abstract void up();
    public abstract void down();
    public abstract void startMovingLeft();
    public abstract void startMovingRight();
    public abstract void startMovingUp();
    public abstract void startMovingDown();
    public abstract void stopMoving();
    public abstract void moveForward(double distance);
    public abstract void moveBackward(double distance);
    public abstract void setStepSize(double stepSize);
    public abstract void strafeLeft();
    public abstract void strafeRight();
    public abstract void strafeUp();
    public abstract void strafeDown();
    public abstract void startStrafingLeft();
    public abstract void startStrafingRight();
    public abstract void startStrafingUp();
    public abstract void startStrafingDown();
    public abstract void strafeInDirection(double direction);
} 