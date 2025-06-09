package game.model;

import engine.model.*;

public class StuntPlayer extends Stunt{
	public double north, south, east, west;
	public StuntPlayer (Model m,Entity e) {
		super(m,e);
	}
	
	
	@Override
	 public void rotate(double angle) {
	 angle = cardinalOf(angle);
	 super.rotate(angle);
	 }
	
	private double cardinalOf(double angle) {
		if (angle == north) {
			angle=270;
		}
		else if (angle == east) {
			angle =0;
		}
		else if (angle==south) {
			angle = 90;
		}
		else if (angle ==west) {
			angle=180;
		}
		return angle;
	}
	
	 // Rotation methods for Shift+Left/Right
    public void rotateLeft() {
        // Rotate 90 degrees counter-clockwise
        rotate(-90);
    }

    public void rotateRight() {
        // Rotate 90 degrees clockwise
        rotate(90);
    }
	public void left() {
		 orient(180); m.move(e,0,-1);
		 }
		 public void right() {
		 orient(0); m.move(e,0,+1);
		 }
		 public void up() {
		 orient(270); m.move(e,-1,0);
		 }
		 public void down() {
		 orient(90); m.move(e,1,0);
		 }
}
