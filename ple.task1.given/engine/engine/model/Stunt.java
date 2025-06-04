package engine.model;

public abstract class Stunt {
	protected Model m;
	protected Entity e;
	
	protected Stunt(Model m,Entity e) {
		this.e=e;
		this.m=m;
		e.stunt=this;
	}
	
	 public void move(int nrows,int ncols) {
		m.move(e,nrows,ncols);
	 }
	 public void rotate(double angle) {
	 angle = angle + e.orientation();
	 orient(angle);
	 }
	 protected void orient (double angle) {
		 e.face(angle);
	 }
	 
	 public abstract void rotateLeft();
	 public abstract void rotateRight();
	 public abstract void left();
	    public abstract void right();
	    public abstract void up();
	    public abstract void down();
}