package game;

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
        
        // Clear the canvas first
        g2.setColor(java.awt.Color.BLACK);
        g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Let the game handle all painting (including UI)
        m_game.paint(canvas, g2);
      } else {
        // Show loading screen while game initializes
        java.awt.Graphics2D g2 = g.getGraphics2D();
        g2.setColor(java.awt.Color.BLACK);
        g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        g2.setColor(java.awt.Color.WHITE);
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        String loading = "Loading...";
        int textWidth = g2.getFontMetrics().stringWidth(loading);
        g2.drawString(loading, (canvas.getWidth() - textWidth) / 2, canvas.getHeight() / 2);
      }
    }

    @Override
    public void visible(Canvas canvas) {
      // Initialize the game when canvas becomes visible
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
