package game;

import engine.controller.Controller;
import engine.model.Model;
import engine.model.Player;
import engine.view.View;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;

public class Controller0 extends Controller {
  
  // Player control states
  private boolean m_leftMousePressed;
  private boolean m_rightMousePressed;
  private boolean m_playerMoving;
  private boolean m_playerRotating;
  private double m_playerTargetAngle;
  private double m_gameTime = 0;
  
  public Controller0(Canvas canvas, Model model, View view) {
    super(canvas, model, view);
    // Override the listeners to add player control functionality
    canvas.set(new PlayerKeyListener());
    canvas.set(new PlayerMouseListener());
  }
  
  @Override
  public void update(double deltaTime) {
    m_gameTime += deltaTime;
    
    // Call parent update for viewport controls
    super.update(deltaTime);
    
    // Handle player-specific updates
    Player player = m_model.player();
    if (player != null && !m_leftMousePressed && !m_rightMousePressed) {
      // Make player face mouse immediately (no smooth rotation)
      double mouseX = m_view.getMouseMetersX();
      double mouseY = m_view.getMouseMetersY();
      double dx = mouseX - player.getX();
      double dy = mouseY - player.getY();
      double targetAngle = Math.toDegrees(Math.atan2(dy, dx));
      player.face(targetAngle); // Instant rotation instead of smooth rotation
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

  private class PlayerKeyListener extends KeyListener {

    @Override
    public void pressed(Canvas canvas, int keyCode, char keyChar) {
      // First handle parent functionality (special keys, zoom, viewport)
      super.pressed(canvas, keyCode, keyChar);
      
      // Then handle player-specific controls
      Player player = m_model.player();
      if (player == null) return;
      
      // Handle shooting with space
      if (keyCode == VirtualKeyCodes.VK_SPACE) {
        player.shoot(m_gameTime);
        return;
      }
      
      // Handle player movement (only when not controlling viewport)
      if (!m_ctrl) {
        switch (keyCode) {
          case VirtualKeyCodes.VK_UP:
            player.startMovingCardinal();
            m_playerMoving = true;
            break;
          case VirtualKeyCodes.VK_DOWN:
            player.stopMoving();
            m_playerMoving = false;
            break;
        }
      }
    }

    @Override
    public void released(Canvas canvas, int keyCode, char keyChar) {
      // Handle parent functionality first
      super.released(canvas, keyCode, keyChar);
      
      // Handle player movement releases
      Player player = m_model.player();
      if (player != null) {
        if (keyCode == VirtualKeyCodes.VK_UP) {
          player.stopMoving();
          m_playerMoving = false;
        }
        // DOWN key handles stopping immediately when pressed, no need to handle release
      }
    }
  }

  private class PlayerMouseListener extends MouseListener {

    @Override
    public void moved(Canvas canvas, int px, int py) {
      // Handle parent functionality (update mouse position)
      super.moved(canvas, px, py);
      
      // Update player target angle
      Player player = m_model.player();
      if (player != null) {
        double mouseX = m_view.getMouseMetersX();
        double mouseY = m_view.getMouseMetersY();
        double dx = mouseX - player.getX();
        double dy = mouseY - player.getY();
        m_playerTargetAngle = Math.toDegrees(Math.atan2(dy, dx));
      }
    }

    @Override
    public void pressed(Canvas canvas, int bno, int x, int y) {
      Player player = m_model.player();
      if (player == null) return;
      
      if (bno == 1) { // Left button
        m_leftMousePressed = true;
        player.startRotatingLeft();
        m_playerRotating = true;
      } else if (bno == 3) { // Right button
        m_rightMousePressed = true;
        player.startRotatingRight();
        m_playerRotating = true;
      }
    }

    @Override
    public void released(Canvas canvas, int bno, int x, int y) {
      Player player = m_model.player();
      if (player == null) return;
      
      if (bno == 1) { // Left button
        m_leftMousePressed = false;
      } else if (bno == 3) { // Right button
        m_rightMousePressed = false;
      }
      
      // Stop rotating if no buttons pressed
      if (!m_leftMousePressed && !m_rightMousePressed) {
        player.stopRotating();
        m_playerRotating = false;
      }
    }
  }
}