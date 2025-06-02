package engine;

import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.tasks.Task;

public class Painter implements Runnable {
  private Game m_game;
  private Canvas m_canvas;
  private int m_ncols, m_nrows;
  private int m_fps = 24;
  private Task m_task;

  Painter(Canvas canvas, int nr, int nc) {
    m_nrows = nr;
    m_ncols = nc;
    m_canvas = canvas;
    m_task = Task.task();
    canvas.set(new PaintListener());
  }

  

  @Override
  public void run() {
        m_canvas.repaint();
        m_task.post(this, 40);
  }

  class PaintListener implements Canvas.PaintListener {

    @Override
    public void paint(Canvas canvas, Graphics g) {
        if (m_game != null) {
            java.awt.Graphics2D g2 = g.getGraphics2D(); 
            m_game.paint(canvas, g2);
          }
    }

    @Override
    public void visible(Canvas canvas) {
        m_game = new Game(canvas, m_nrows, m_ncols);

        m_canvas.repaint();

        m_task.post(Painter.this);
    }

    @Override
    public void revoked(Canvas canvas) {
      System.exit(0);
    }

  }

}
