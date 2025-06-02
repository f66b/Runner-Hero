package engine.controller;

import engine.IController;
import engine.model.Model;
import engine.view.View;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;

public abstract class Controller implements IController {
  protected final Canvas m_canvas;
  protected final View m_view;
  protected final Model m_model;
  
  // Special key states
  protected boolean m_shift;
  protected boolean m_ctrl;
  protected boolean m_alt;
  
  // Arrow key states
  private boolean m_upPressed;
  private boolean m_downPressed;
  private boolean m_leftPressed;
  private boolean m_rightPressed;
  
  // Viewport control speed
  private static final double VIEWPORT_TRANSLATION_SPEED = 10.0; // meters per second
  private static final double ZOOM_SPEED = 0.1;

  public Controller(Canvas canvas, Model model, View view) {
    m_canvas = canvas;
    m_model = model;
    m_view = view;
    canvas.set(new MouseListener());
    canvas.set(new KeyListener());
  }
  
  @Override
  public void onTick(double deltaTime) {
    update(deltaTime);
  }
  
  @Override
  public void update(double deltaTime) {
    // Handle continuous viewport movement with ctrl+arrows
    if (m_ctrl) {
      double translation = VIEWPORT_TRANSLATION_SPEED * deltaTime;
      if (m_upPressed) {
        m_view.setViewportTranslation(0, -translation);
      }
      if (m_downPressed) {
        m_view.setViewportTranslation(0, translation);
      }
      if (m_leftPressed) {
        m_view.setViewportTranslation(-translation, 0);
      }
      if (m_rightPressed) {
        m_view.setViewportTranslation(translation, 0);
      }
    }
    
    // Update model
    m_model.update(deltaTime);
    
    // Request repaint
    m_canvas.repaint();
  }
  
  @Override
  public void handleKeyPress(int keyCode, char keyChar) {
    new KeyListener().pressed(m_canvas, keyCode, keyChar);
  }
  
  @Override
  public void handleKeyRelease(int keyCode, char keyChar) {
    new KeyListener().released(m_canvas, keyCode, keyChar);
  }
  
  @Override
  public void handleMouseMove(int px, int py) {
    new MouseListener().moved(m_canvas, px, py);
  }
  
  @Override
  public void handleMousePress(int bno, int x, int y) {
    new MouseListener().pressed(m_canvas, bno, x, y);
  }
  
  @Override
  public void handleMouseRelease(int bno, int x, int y) {
    new MouseListener().released(m_canvas, bno, x, y);
  }
  
  @Override
  public boolean isPlayerMoving() {
    return false; // Base controller doesn't handle player movement
  }
  
  @Override
  public boolean isPlayerRotating() {
    return false; // Base controller doesn't handle player rotation
  }
  
  @Override
  public double getPlayerTargetAngle() {
    return 0; // Base controller doesn't handle player targeting
  }

  protected class KeyListener implements Canvas.KeyListener {

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
        m_view.setZoomLevel(m_view.getZoomLevel() * (1 + ZOOM_SPEED));
        return;
      }
      if (keyChar == '-' || keyChar == '_') {
        m_view.setZoomLevel(m_view.getZoomLevel() / (1 + ZOOM_SPEED));
        return;
      }
      
      // Handle return to full view
      if (keyChar == '=') {
        // Reset viewport and zoom to show full grid
        m_view.setInitialZoom();
        return;
      }
      
      // Handle debug mode toggle
      if (keyChar == 'D' || keyChar == 'd') {
        m_view.setDebugMode(!m_view.isDebugMode());
        return;
      }
      
      // Handle arrow keys (for viewport control when ctrl is pressed)
      switch (keyCode) {
        case VirtualKeyCodes.VK_UP:
          m_upPressed = true;
          break;
        case VirtualKeyCodes.VK_DOWN:
          m_downPressed = true;
          break;
        case VirtualKeyCodes.VK_LEFT:
          m_leftPressed = true;
          break;
        case VirtualKeyCodes.VK_RIGHT:
          m_rightPressed = true;
          break;
      }
    }

    @Override
    public void released(Canvas canvas, int keyCode, char keyChar) {
      // Handle special modifier keys
      if (keyCode == VirtualKeyCodes.VK_SHIFT) {
        m_shift = false;
      }
      if (keyCode == VirtualKeyCodes.VK_CONTROL) {
        m_ctrl = false;
      }
      if (keyCode == VirtualKeyCodes.VK_ALT) {
        m_alt = false;
      }
      
      // Handle arrow keys
      if (keyCode == VirtualKeyCodes.VK_UP) {
        m_upPressed = false;
      }
      if (keyCode == VirtualKeyCodes.VK_DOWN) {
        m_downPressed = false;
      }
      if (keyCode == VirtualKeyCodes.VK_LEFT) {
        m_leftPressed = false;
      }
      if (keyCode == VirtualKeyCodes.VK_RIGHT) {
        m_rightPressed = false;
      }
    }

    @Override
    public void typed(Canvas canvas, char keyChar) {
      // Nothing to do here
    }
  }

  protected class MouseListener implements Canvas.MouseListener {

    @Override
    public void moved(Canvas canvas, int px, int py) {
      // Update mouse position in view
      m_view.setMousePosition(px, py);
    }

    @Override
    public void pressed(Canvas canvas, int bno, int x, int y) {
      // Base controller doesn't handle mouse presses for player control
    }

    @Override
    public void released(Canvas canvas, int bno, int x, int y) {
      // Base controller doesn't handle mouse releases for player control
    }
  }
}