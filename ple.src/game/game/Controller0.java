package game;

import engine.controller.Controller;
import engine.model.Model;
import engine.model.Player;
import engine.view.View;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;

public class Controller0 extends Controller {
  
  // Player control states
  private boolean m_upPressed = false;
  private boolean m_downPressed = false;

  private PlayerKeyListener m_keyListener;
  private UIManager m_ui;
  
  public Controller0(Canvas canvas, Model model, View view, UIManager ui) {
    super(canvas, model, view);
    this.m_ui = ui;
    m_keyListener = new PlayerKeyListener();
    canvas.set(m_keyListener);
    // register our custom mouse listener to forward clicks to UIManager
    canvas.set(new MouseListener());
    // Mouse events are handled by UIManager, not by this controller
  }
  
  @Override
  public void update(double deltaTime) {
    // Parent update handles viewport controls
    super.update(deltaTime);
    // Player movement logic is now entirely in StuntPlayer.tick()
  }
  
  @Override
  public boolean isPlayerMoving() {
    Player player = m_model.player();
    if (player != null && player.stunt instanceof game.model.StuntPlayer) {
      game.model.StuntPlayer stunt = (game.model.StuntPlayer) player.stunt;
      return stunt.isJumping() || stunt.isSliding();
    }
    return false;
  }
  
  @Override
  public boolean isPlayerRotating() {
    return false; // No rotation in runner style
  }
  
  @Override
  public double getPlayerTargetAngle() {
    return 0; // Always facing right
  }

  protected class PlayerKeyListener extends KeyListener {

    @Override
    public void pressed(Canvas canvas, int keyCode, char keyChar) {
      // First handle parent functionality (special keys, zoom, viewport)
      super.pressed(canvas, keyCode, keyChar);
      
      Player player = m_model.player();
      if (player == null || !player.isAlive()) return; // Don't process keys if player is dead
      
      // Handle movement only when not controlling viewport and game is running
      if (!m_ctrl) {
        switch (keyCode) {
          case VirtualKeyCodes.VK_UP:
          case VirtualKeyCodes.VK_SPACE: // Space bar also triggers jump
            if (!m_upPressed) {
              m_upPressed = true;
              if (player.stunt != null) {
                player.stunt.jump();
              }
            }
            break;
            
          case VirtualKeyCodes.VK_DOWN:
            if (!m_downPressed) {
              m_downPressed = true;
              if (player.stunt != null) {
                player.stunt.slide();
              }
            }
            break;
        }
      }
    }

    @Override
    public void released(Canvas canvas, int keyCode, char keyChar) {
      super.released(canvas, keyCode, keyChar);
      
      switch (keyCode) {
        case VirtualKeyCodes.VK_UP:
        case VirtualKeyCodes.VK_SPACE:
          m_upPressed = false;
          break;
        case VirtualKeyCodes.VK_DOWN:
          m_downPressed = false;
          // Stop sliding when down key is released
          Player player = m_model.player();
          if (player != null && player.stunt instanceof game.model.StuntPlayer) {
            ((game.model.StuntPlayer) player.stunt).stopSliding();
          }
          break;
      }
    }
  }

  // Override Controller's MouseListener to intercept clicks
  protected class MouseListener extends Controller.MouseListener {
    @Override
    public void pressed(Canvas canvas, int bno, int x, int y) {
      // forward to UI manager first
      if (m_ui != null) m_ui.handleMouseClick(x, y);
      // then maybe gameplay
    }
  }
} 