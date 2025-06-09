package game;

import engine.controller.Controller;
import engine.model.Model;
import engine.model.Player;
import engine.view.View;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;
import engine.model.Stunt;

public class Controller0 extends Controller {
  
  // Player control states
  private boolean m_leftMousePressed;
  private boolean m_rightMousePressed;
  private boolean m_playerMoving;
  private boolean m_shift = false;
  private boolean m_nPressed = false; // New: N key state for strafe mode
  private boolean m_playerRotating;
  private double m_playerTargetAngle;
  private double m_gameTime = 0;
  private PlayerKeyListener m_keyListener;
  private PlayerMouseListener m_mouseListener;
  
  // Movement key states for continuous movement
  private boolean m_upPressed = false;
  private boolean m_downPressed = false;
  private boolean m_leftPressed = false;
  private boolean m_rightPressed = false;
  
  public Controller0(Canvas canvas, Model model, View view) {
    super(canvas, model, view);
    // Create and set our custom listeners
    m_keyListener = new PlayerKeyListener();
    m_mouseListener = new PlayerMouseListener();
    canvas.set(m_keyListener);
    canvas.set(m_mouseListener);
  }
  
  @Override
  public void update(double deltaTime) {
    m_gameTime += deltaTime;
    
    // Call parent update for viewport controls
    super.update(deltaTime);
    
    // Handle continuous movement based on pressed keys
    Player player = m_model.player();
    if (player != null) {
      
      // Handle continuous movement when keys are held down
      if (m_upPressed || m_downPressed || m_leftPressed || m_rightPressed) {
        if (m_nPressed) {
          // N + Arrow: Strafe movement (without changing orientation)
          if (m_upPressed) {
            player.stunt.strafeUp();
          }
          if (m_downPressed) {
            player.stunt.strafeDown();
          }
          if (m_leftPressed) {
            player.stunt.strafeLeft();
          }
          if (m_rightPressed) {
            player.stunt.strafeRight();
          }
        } else {
          // Arrow only: Turn and move fluidly
          if (m_upPressed) {
            player.stunt.startMovingUp();
          }
          if (m_downPressed) {
            player.stunt.startMovingDown();
          }
          if (m_leftPressed) {
            player.stunt.startMovingLeft();
          }
          if (m_rightPressed) {
            player.stunt.startMovingRight();
          }
        }
        m_playerMoving = true;
      } else {
        // No movement keys pressed, stop moving
        if (m_playerMoving) {
          player.stunt.stopMoving();
          m_playerMoving = false;
        }
      }
      
      // Handle mouse facing only when not moving and not in special modes
      if (!m_leftMousePressed && !m_rightMousePressed && !m_shift && !m_playerMoving && !m_playerRotating && !m_nPressed) {
        // Make player face mouse immediately (no smooth rotation)
        double mouseX = m_view.getMouseMetersX();
        double mouseY = m_view.getMouseMetersY();
        if (mouseX >= 0 && mouseX <= m_model.getWorldWidthMeters() && 
            mouseY >= 0 && mouseY <= m_model.getWorldHeightMeters()) {
          double dx = mouseX - player.getX();
          double dy = mouseY - player.getY();
          double targetAngle = Math.toDegrees(Math.atan2(dy, dx));
          player.face(targetAngle); // Instant rotation instead of smooth rotation
        }
      }
    }
  }
  
  @Override
  public boolean isPlayerMoving() {
    return m_playerMoving;
  }
  
  @Override
  public boolean isPlayerRotating() {
    return m_playerRotating;
  }
  
  @Override
  public double getPlayerTargetAngle() {
    return m_playerTargetAngle;
  }

  protected class PlayerKeyListener extends KeyListener {

    @Override
    public void pressed(Canvas canvas, int keyCode, char keyChar) {
      if (keyCode == VirtualKeyCodes.VK_SHIFT) {
        m_shift = true;
        return;
      }
      
      // Handle N key for strafe mode
      if (keyChar == 'N' || keyChar == 'n') {
        m_nPressed = true;
        return;
      }
      
      // First handle parent functionality (special keys, zoom, viewport)
      super.pressed(canvas, keyCode, keyChar);
      
      // Then handle player-specific controls
      Player player = m_model.player();
      if (player == null) return;
      
      // Handle movement only when not controlling viewport
      if (!m_ctrl) {
        switch (keyCode) {
          case VirtualKeyCodes.VK_UP:
            if (!m_upPressed) { // Only trigger on initial press
              m_upPressed = true;
              if (m_nPressed) {
                // N + Up: Strafe up without changing orientation
                player.stunt.strafeUp();
              } else {
                // Just Up: Face up and start moving
                player.stunt.startMovingUp();
              }
              m_playerMoving = true;
            }
            break;
            
          case VirtualKeyCodes.VK_DOWN:
            if (!m_downPressed) { // Only trigger on initial press
              m_downPressed = true;
              if (m_nPressed) {
                // N + Down: Strafe down without changing orientation
                player.stunt.strafeDown();
              } else {
                // Just Down: Face down and start moving
                player.stunt.startMovingDown();
              }
              m_playerMoving = true;
            }
            break;
            
          case VirtualKeyCodes.VK_LEFT:
            if (!m_leftPressed) { // Only trigger on initial press
              m_leftPressed = true;
              if (m_shift) {
                // Shift+Left: rotate counter-clockwise
                player.stunt.rotateLeft();
                m_playerRotating = true;
              } else if (m_nPressed) {
                // N + Left: Strafe left without changing orientation
                player.stunt.strafeLeft();
                m_playerMoving = true;
              } else {
                // Just Left: Face left and start moving
                player.stunt.startMovingLeft();
                m_playerMoving = true;
              }
            }
            break;
            
          case VirtualKeyCodes.VK_RIGHT:
            if (!m_rightPressed) { // Only trigger on initial press
              m_rightPressed = true;
              if (m_shift) {
                // Shift+Right: rotate clockwise
                player.stunt.rotateRight();
                m_playerRotating = true;
              } else if (m_nPressed) {
                // N + Right: Strafe right without changing orientation
                player.stunt.strafeRight();
                m_playerMoving = true;
              } else {
                // Just Right: Face right and start moving
                player.stunt.startMovingRight();
                m_playerMoving = true;
              }
            }
            break;
        }
      }
    }

    @Override
    public void released(Canvas canvas, int keyCode, char keyChar) {
      // Handle parent functionality first
      super.released(canvas, keyCode, keyChar);
      
      if (keyCode == VirtualKeyCodes.VK_SHIFT) {
        m_shift = false;
        return;
      }
      
      // Handle N key release
      if (keyChar == 'N' || keyChar == 'n') {
        m_nPressed = false;
        return;
      }
      
      // Handle movement key releases
      Player player = m_model.player();
      if (player != null) {
        switch (keyCode) {
          case VirtualKeyCodes.VK_UP:
            m_upPressed = false;
            break;
          case VirtualKeyCodes.VK_DOWN:
            m_downPressed = false;
            break;
          case VirtualKeyCodes.VK_LEFT:
            m_leftPressed = false;
            if (m_shift) {
              m_playerRotating = false;
            }
            break;
          case VirtualKeyCodes.VK_RIGHT:
            m_rightPressed = false;
            if (m_shift) {
              m_playerRotating = false;
            }
            break;
        }
        
        // If no movement keys are pressed, stop moving
        if (!m_upPressed && !m_downPressed && !m_leftPressed && !m_rightPressed) {
          if (m_playerMoving) {
            player.stunt.stopMoving();
            m_playerMoving = false;
          }
        }
      }
    }
  }

  protected class PlayerMouseListener extends MouseListener {

    @Override
    public void moved(Canvas canvas, int px, int py) {
      // Just update mouse position in view, no auto-rotation when moving or in N mode
      super.moved(canvas, px, py);
    }

    @Override
    public void pressed(Canvas canvas, int bno, int x, int y) {
      // Mouse controls disabled for grid-based movement
    }

    @Override
    public void released(Canvas canvas, int bno, int x, int y) {
      // Mouse controls disabled for grid-based movement
    }
  }
}