package engine.model;

public abstract class Direction {
	 public static final Direction N; // north
	 public static final Direction E; // east
	 public static final Direction S; // south
	 public static final Direction W; // west
	 public static final Direction F; // forward
	 public static final Direction B; // backward
	 public static final Direction L; // left
	 public static final Direction R; // right
	 public abstract boolean isRelative();
	 public abstract int degrees();
	 public abstract boolean equals(Direction d);
	 public abstract Direction rotate(int angle);
	 public abstract Direction cardinalOf();
	 static {
		 N = new Absolute(270);
		 E = new Absolute(0);
		 S = new Absolute(90);
		 W = new Absolute(180);
		 F = new Relative(0);
		 B = new Relative(180);
		 L = new Relative(270);
		 R = new Relative(90);
		 }
	 final private int angle;
	 protected Direction(int a) {
	 angle = a;
	 }
	 // must be static
	 // should be private
	 private static
	 class Absolute extends Direction {
	 
	 }
	 // must be static
	 // should be private
	 private static
	 class Relative extends Direction {
	 
	 }

}
