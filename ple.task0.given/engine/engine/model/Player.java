package engine.model;

public class Player extends Entity {

  public Player(Model m, int x, int y, int o) {
    super(m, x, y, o);
    m.setPlayer(this);
  }

  /*
   * Move this entity up one row.
   */
  public void up() {
    move(-1,0);
  }

  /*
   * Move this entity down one row.
   */
  public void down() {
    move(1,0);
  }

  /*
   * Move this entity left one column.
   */
  public void left() {
	  move(0,-1);
  }

  /*
   * Move this entity right one column.
   */
  public void right() {
    move(0,1);
  }

}
