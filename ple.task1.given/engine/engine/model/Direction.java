package engine.model;

public abstract class Direction {
    private final int angle;

    protected Direction(int a) {
        angle = a;
    }

    public int degrees() {
        return angle;
    }

    public abstract boolean isRelative();
    public abstract boolean equals(Direction d);
    public abstract Direction rotate(int angleDelta);
    public abstract Direction cardinalOf();

    // Predefined constants
    public static final Direction N = new Absolute(270);
    public static final Direction E = new Absolute(0);
    public static final Direction S = new Absolute(90);
    public static final Direction W = new Absolute(180);
    public static final Direction F = new Relative(0);
    public static final Direction B = new Relative(180);
    public static final Direction L = new Relative(270);
    public static final Direction R = new Relative(90);

    private static class Absolute extends Direction {
        protected Absolute(int angle) {
            super(angle);
        }

        @Override
        public boolean isRelative() {
            return false;
        }

        @Override
        public boolean equals(Direction d) {
            return !d.isRelative() && d.degrees() == degrees();
        }

        @Override
        public Direction rotate(int angleDelta) {
            return new Absolute((degrees() + angleDelta) % 360);
        }

        @Override
        public Direction cardinalOf() {
            return this;
        }
    }

    private static class Relative extends Direction {
        protected Relative(int angle) {
            super(angle);
        }

        @Override
        public boolean isRelative() {
            return true;
        }

        @Override
        public boolean equals(Direction d) {
            return d.isRelative() && d.degrees() == degrees();
        }

        @Override
        public Direction rotate(int angleDelta) {
            return new Relative((degrees() + angleDelta) % 360);
        }

        @Override
        public Direction cardinalOf() {
            int angle = degrees();
            if (angle >= 315 || angle < 45) return E;
            if (angle >= 45 && angle < 135) return S;
            if (angle >= 135 && angle < 225) return W;
            return N;
        }
    }
}