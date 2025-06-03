package engine.model;

public abstract class Stunt {
	protected Model m;
	protected Entity e;
	
	protected Stunt(Model m,Entity e) {
		this.e=e;
		this.m=m;
		e.Stunt=this;
	}
	
	 public void move(int nrows,int ncols) {
		m.move(e,nrows,ncols);
	 }
	 public void rotate(double angle) {
	 angle = angle + e.orientation();
	 e.orient(angle);
	 }
}