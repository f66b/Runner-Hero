package engine.controller;

import engine.model.Model;
import engine.view.View;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;

public class Controller {
  private Canvas m_canvas;
  private View m_view;
  private Model m_model;
  private boolean m_shift;
  private boolean m_ctrl;
  private boolean m_alt;
  
  public Controller(Canvas canvas, Model model, View view) {
    m_canvas = canvas;
    m_model = model;
    m_view = view;
    canvas.set(new MouseListener());
    canvas.set(new KeyListener());
  }
  
  class KeyListener implements Canvas.KeyListener {
    @Override
    public void pressed(Canvas canvas, int keyCode, char keyChar) {
      // Handle special modifier keys
      if (keyCode == VirtualKeyCodes.VK_SHIFT) {
        m_shift = true;
        return;
      }
      if (keyCode == VirtualKeyCodes.VK_CONTROL) {
        m_ctrl = true;
        return;
      }
      if (keyCode == VirtualKeyCodes.VK_ALT) {
        m_alt = true;
        return;
      }
      
      // Handle zoom controls
      if (keyChar == '+') {
        m_view.zoomIn();
        canvas.repaint();
        return;
      }
        if (keyChar == '=' ) {
        	m_view.zoomOrigin();
        	canvas.repaint();
        	return;
        }
      
      if (keyChar == '-') {
        m_view.zoomOut();
        canvas.repaint();
        return;
      }
      
      // Handle viewport translation with ctrl+arrows
      if (m_ctrl) {
        switch (keyCode) {
          case VirtualKeyCodes.VK_LEFT:
            m_view.translateViewport(-1, 0);
            break;
          case VirtualKeyCodes.VK_RIGHT:
            m_view.translateViewport(1, 0);
            break;
          case VirtualKeyCodes.VK_UP:
            m_view.translateViewport(0, -1);
            break;
          case VirtualKeyCodes.VK_DOWN:
            m_view.translateViewport(0, 1);
            break;
        }
        canvas.repaint();
        return;
      }
      
      // Handle player controls
      engine.model.Player player = m_model.player();
      if (player == null) return;
      
      if (!m_shift) {
        // Move player with arrow keys
        switch (keyCode) {
          case VirtualKeyCodes.VK_LEFT:
            player.left();
            break;
          case VirtualKeyCodes.VK_RIGHT:
            player.right();
            break;
          case VirtualKeyCodes.VK_UP:
            player.up();
            break;
          case VirtualKeyCodes.VK_DOWN:
            player.down();
            break;
        }
      } else {
        // Rotate player with Shift + LEFT or RIGHT
        switch (keyCode) {
          case VirtualKeyCodes.VK_LEFT:
            player.rotate(-90); // counter-clockwise
            break;
          case VirtualKeyCodes.VK_RIGHT:
            player.rotate(90); // clockwise
            break;
        }
      }
      // Request a repaint after action
      canvas.repaint();
    }
    
    @Override
    public void released(Canvas canvas, int keyCode, char keyChar) {
      if (keyCode == VirtualKeyCodes.VK_SHIFT) {
        m_shift = false;
      }
      if (keyCode == VirtualKeyCodes.VK_CONTROL) {
        m_ctrl = false;
      }
      if (keyCode == VirtualKeyCodes.VK_ALT) {
        m_alt = false;
      }
    }
    
    @Override
    public void typed(Canvas canvas, char keyChar) {
      // Non utilisé
    }
  }
  
  class MouseListener implements Canvas.MouseListener {
    @Override
    public void moved(Canvas canvas, int px, int py) {
      // Update mouse position in view for "hello" text
      m_view.setMousePosition(px, py);
      
      // Calcul de la cellule pointée (row, col)
      int cellWidth = canvas.getWidth() / m_model.ncols();
      int cellHeight = canvas.getHeight() / m_model.nrows();
      int col = px / cellWidth;
      int row = py / cellHeight;
      // Si besoin : m_model.setFocus(row, col);
      m_canvas.repaint(); // pour afficher le focus éventuellement
    }
    
    @Override
    public void pressed(Canvas canvas, int bno, int x, int y) {
      // Rien à faire ici pour l'instant
    }
    
    @Override
    public void released(Canvas canvas, int bno, int x, int y) {
      // Rien à faire ici non plus
    }
  }
}