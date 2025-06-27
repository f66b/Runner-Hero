package game;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.image.BufferedImage;

import engine.view.View;
import engine.model.Model;
import engine.model.Player;
import engine.model.Entity;
import engine.model.Projectile;
import engine.model.Rock;
import engine.model.Pigeon;
import engine.model.EnemyShooter;
import oop.graphics.Canvas;
import engine.view.Avatar;
import game.view.PlayerAvatar;
import game.view.ProjectileAvatar;
import game.model.StuntPlayer;
import engine.model.Coin;
import engine.model.HealthPickup;
import engine.utils.ImageLoader;

public class View0 extends View {
  
  private ImageLoader imageLoader;
  private long frameCounter = 0; // For animation timing
  private double backgroundOffset = 0.0; // For scrolling background
  
  public View0(Canvas canvas, Model model) {
    super(canvas, model);
    imageLoader = ImageLoader.getInstance();
  }
  
  @Override
  public void paint(Canvas canvas, Graphics2D g) {
    frameCounter++;
    
    // Clear the entire canvas first to prevent trails
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    
    // Store original transform for restoration later
    AffineTransform originalTransform = g.getTransform();
    
    // Draw scrolling background
    drawScrollingBackground(g, canvas);
    
    // Apply camera transformation for world coordinates
    g.translate(-m_viewportX * m_pixelsPerMeter * m_zoomLevel, 
                -m_viewportY * m_pixelsPerMeter * m_zoomLevel);
    g.scale(m_zoomLevel, m_zoomLevel);
    
    // Draw all entities using a snapshot to avoid ConcurrentModificationException
    // Create a snapshot of entities to avoid concurrent modification
    java.util.List<Entity> entitiesSnapshot = new java.util.ArrayList<>();
    java.util.List<Entity> projectiles = new java.util.ArrayList<>();
    java.util.List<Entity> otherEntities = new java.util.ArrayList<>();
    
    try {
      java.util.Iterator<Entity> iter = m_model.entities();
      while (iter.hasNext()) {
        Entity entity = iter.next();
        // Separate projectiles from other entities for proper rendering order
        if (entity instanceof Projectile) {
          projectiles.add(entity);
        } else {
          otherEntities.add(entity);
        }
      }
    } catch (Exception e) {
      // If we get an exception, just skip this frame
      System.err.println("Error creating entity snapshot: " + e.getMessage());
      g.setTransform(originalTransform);
      return;
    }
    
    // Draw projectiles first (in background)
    for (Entity ent : projectiles) {
      try {
        drawEntity(g, canvas, ent);
      } catch (Exception e) {
        // If drawing an entity fails, continue with others
        System.err.println("Error drawing projectile " + ent.getClass().getSimpleName() + ": " + e.getMessage());
      }
    }
    
    // Then draw all other entities (including enemy shooters) on top
    for (Entity ent : otherEntities) {
      try {
        drawEntity(g, canvas, ent);
      } catch (Exception e) {
        // If drawing an entity fails, continue with others
        System.err.println("Error drawing entity " + ent.getClass().getSimpleName() + ": " + e.getMessage());
      }
    }
    
    // Restore original transform for UI elements
    g.setTransform(originalTransform);
    
    // Draw debug info if enabled
    if (m_debugMode) {
      drawDebugInfo(g);
    }
  }
  
  /**
   * Draw scrolling background
   */
  private void drawScrollingBackground(Graphics2D g, Canvas canvas) {
    BufferedImage background = imageLoader.getBackground();
    
    if (background != null) {
      // Calculate background scroll based on elapsed time instead of player position
      // This ensures smooth scrolling even when player is fixed
      // Background scrolls slower than the world for parallax effect
      backgroundOffset += 1.5; // Slower background scroll for parallax
      
      int bgWidth = background.getWidth();
      int bgHeight = background.getHeight();
      int canvasWidth = canvas.getWidth();
      int canvasHeight = canvas.getHeight();
      
      // Scale background to fit canvas height
      double scale = (double) canvasHeight / bgHeight;
      int scaledWidth = (int) (bgWidth * scale);
      int scaledHeight = canvasHeight;
      
      // Calculate how many background tiles we need
      int startX = -(int)(backgroundOffset % scaledWidth);
      
      // Draw repeating background tiles
      for (int x = startX; x < canvasWidth + scaledWidth; x += scaledWidth) {
        g.drawImage(background, x, 0, scaledWidth, scaledHeight, null);
      }
    } else {
      // Fallback gradient background if image not found
      drawGradientBackground(g, canvas);
    }
  }
  
  /**
   * Draw gradient background as fallback
   */
  private void drawGradientBackground(Graphics2D g, Canvas canvas) {
    java.awt.GradientPaint gradient = new java.awt.GradientPaint(
        0, 0, new Color(135, 206, 235), // Sky blue
        0, canvas.getHeight(), new Color(34, 139, 34) // Forest green
    );
    g.setPaint(gradient);
    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }
  
  @Override
  protected void drawEntity(Graphics2D g, Canvas canvas, Entity entity) {
    double x = entity.getX() * getPixelsPerMeter();
    double y = entity.getY() * getPixelsPerMeter();
    double size = m_model.getCellSizeMeters() * getPixelsPerMeter();
    
    // Draw differently based on entity type using images
    if (entity instanceof Player) {
      drawPlayer(g, (Player)entity, x, y, size);
    } else if (entity instanceof Projectile) {
      drawProjectile(g, (Projectile)entity, x, y, size);
    } else if (entity instanceof Rock) {
      drawRock(g, (Rock)entity, x, y, size);
    } else if (entity instanceof Pigeon) {
      drawPigeon(g, (Pigeon)entity, x, y, size);
    } else if (entity instanceof EnemyShooter) {
      drawEnemyShooter(g, (EnemyShooter)entity, x, y, size);
    } else if (entity instanceof Coin) {
      drawCoin(g, (Coin)entity, x, y, size);
    } else if (entity instanceof HealthPickup) {
      drawHealthPickup(g, (HealthPickup)entity, x, y, size);
    } else {
      // Draw other entities as colored circles
      drawGenericEntity(g, entity, x, y, size);
    }
  }
  
  /**
   * Draw player using images based on state with proper positioning
   */
  private void drawPlayer(Graphics2D g, Player player, double x, double y, double size) {
    // Get player animation state based on current actions
    String animationState = getPlayerAnimationState(player);
    
    // Calculate animation speed based on world scroll speed
    // Base animation switches every 10 frames, but gets faster with higher speed
    double speedMultiplier = m_model.getWorldScrollSpeed() / 4.0; // 4.0 is base speed
    int animationFrames = Math.max(3, (int)(10 / speedMultiplier)); // Minimum 3 frames, maximum 10
    boolean alternateFrame = (frameCounter / animationFrames) % 2 == 0;
    
    // Debug output for animation speed changes (every 60 frames to avoid spam)
    if (frameCounter % 60 == 0 && speedMultiplier != 1.0) {
      System.out.println("Player animation speed: " + String.format("%.1f", speedMultiplier) + "x" +
                        " (frames per transition: " + animationFrames + 
                        ", world speed: " + String.format("%.1f", m_model.getWorldScrollSpeed()) + " m/s)");
    }
    
    // Get appropriate player image (already scaled)
    BufferedImage playerImage = imageLoader.getPlayerImage(animationState.equals("jump"), animationState.equals("slide"), alternateFrame);
    
    if (playerImage != null) {
      // Position player at bottom-left of cell for runner style
      int imageWidth = playerImage.getWidth();
      int imageHeight = playerImage.getHeight();
      
      // Position player at bottom of the cell (ground level)
      int drawX = (int) (x - imageWidth / 2);
      int drawY = (int) (y + size/2 - imageHeight); // Bottom-aligned
      
      // Apply transparency if player is invincible
      if (player.isInvincible()) {
        // Create transparent composite
        java.awt.Composite oldComposite = g.getComposite();
        g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.5f));
        g.drawImage(playerImage, drawX, drawY, null);
        g.setComposite(oldComposite);
        
        // Draw invincibility sparkle effect
        drawInvincibilityEffect(g, x, y, size);
      } else {
        g.drawImage(playerImage, drawX, drawY, null);
      }
      
      // Draw health indicator above player
      drawHealthIndicator(g, player, x, y - size * 0.8);
      
    } else {
      // Fallback to geometric shape if image not available
      drawPlayerFallback(g, player, x, y, size, animationState.equals("jump"), animationState.equals("slide"));
    }
  }
  
  /**
   * Get player animation state based on current actions
   */
  private String getPlayerAnimationState(Player player) {
    if (player.stunt instanceof game.model.StuntPlayer) {
      game.model.StuntPlayer stunt = (game.model.StuntPlayer) player.stunt;
      
      // Call fallback timeout check to ensure player doesn't get stuck
      // This works even if the game loop isn't running properly
      stunt.checkFallbackTimeouts();
      
      String state;
      boolean jumping = stunt.isJumping();
      boolean sliding = stunt.isSliding();
      
      if (jumping) {
        state = "jump";
      } else if (sliding) {
        state = "slide";
      } else {
        // Default to running when not jumping or sliding
        state = "running";
      }
      
      // Debug output for state changes (every 30 frames to avoid spam)
      if (frameCounter % 30 == 0) {
        System.out.println("Player animation state: " + state + 
                          " (jumping: " + jumping + ", sliding: " + sliding + 
                          ", Y: " + String.format("%.2f", player.getY()) + ")");
      }
      
      return state;
    }
    
    // Fallback to running if no stunt or wrong type
    if (frameCounter % 30 == 0) {
      System.out.println("Player has no StuntPlayer - defaulting to running state");
    }
    return "running";
  }
  
  /**
   * Draw invincibility sparkle effect
   */
  private void drawInvincibilityEffect(Graphics2D g, double x, double y, double size) {
    g.setColor(new Color(255, 255, 255, 150));
    for (int i = 0; i < 5; i++) {
      double angle = (frameCounter * 0.2 + i * Math.PI * 2 / 5);
      double sparkleX = x + Math.cos(angle) * size * 0.4;
      double sparkleY = y + Math.sin(angle) * size * 0.4;
      g.fillOval((int)sparkleX - 2, (int)sparkleY - 2, 4, 4);
    }
  }
  
  /**
   * Draw health indicator above entity - larger and more prominent hearts
   */
  private void drawHealthIndicator(Graphics2D g, Player player, double x, double y) {
    int health = player.getHealth();
    int maxHealth = player.getMaxHealth();
    
    // Position hearts above the player (higher up)
    double heartY = y - 40; // 40 pixels above player
    double heartSize = 12; // Larger hearts
    double heartSpacing = 20; // More spacing between hearts
    
    // Center the hearts above the player
    double startX = x - (maxHealth - 1) * heartSpacing / 2.0;
    
    // Draw health hearts
    for (int i = 0; i < maxHealth; i++) {
      double heartX = startX + i * heartSpacing;
      
      if (i < health) {
        // Full heart - bright red with outline
        g.setColor(Color.RED);
        g.fillOval((int)(heartX - heartSize/2), (int)(heartY - heartSize/2), (int)heartSize, (int)heartSize);
        
        // Add white outline for visibility
    g.setColor(Color.WHITE);
        g.setStroke(new java.awt.BasicStroke(2.0f));
        g.drawOval((int)(heartX - heartSize/2), (int)(heartY - heartSize/2), (int)heartSize, (int)heartSize);
      } else {
        // Empty heart - dark red outline only
        g.setColor(new Color(100, 0, 0)); // Dark red
        g.setStroke(new java.awt.BasicStroke(2.0f));
        g.drawOval((int)(heartX - heartSize/2), (int)(heartY - heartSize/2), (int)heartSize, (int)heartSize);
      }
    }
    
    // Reset stroke
    g.setStroke(new java.awt.BasicStroke(1.0f));
  }
  
  /**
   * Draw rock using properly scaled image
   */
  private void drawRock(Graphics2D g, Rock rock, double x, double y, double size) {
    BufferedImage rockImage = imageLoader.getObstacleImage(ImageLoader.ROCK);
    
    if (rockImage != null) {
      int imageWidth = rockImage.getWidth();
      int imageHeight = rockImage.getHeight();
      
      // Center the obstacle in the cell
      int drawX = (int) (x - imageWidth / 2);
      int drawY = (int) (y - imageHeight / 2);
      g.drawImage(rockImage, drawX, drawY, null);
    } else {
      drawRockFallback(g, rock, x, y, size);
    }
  }
  
  /**
   * Draw pigeon using properly scaled image - rendered above player for visual appeal
   * while collision detection happens at player level
   */
  private void drawPigeon(Graphics2D g, Pigeon pigeon, double x, double y, double size) {
    BufferedImage pigeonImage = imageLoader.getObstacleImage(ImageLoader.PIGEON);
    
    if (pigeonImage != null) {
      int imageWidth = pigeonImage.getWidth();
      int imageHeight = pigeonImage.getHeight();
      
      // Render pigeon with animated vertical movement - bigger distance between altitudes
      double baseVisualOffsetY = -size * 2.0; // Increased from 1.5 to 3.0 for bigger altitude difference
      double animationRange = size * 1.0; // Animation range of 1.5 cell sizes
      
      // Create bobbing animation that moves between player level and high above
      double animationSpeed = 0.8 * (m_model.getWorldScrollSpeed() / 4.0); // Scale with game speed
      double verticalAnimation = Math.sin(frameCounter * animationSpeed * 0.05) * animationRange;
      double visualY = y + baseVisualOffsetY + verticalAnimation;
      
      // Center the pigeon in the cell (horizontally) but animate vertically
      int drawX = (int) (x - imageWidth / 2);
      int drawY = (int) (visualY - imageHeight / 2);
      
      // Add flapping wings animation - faster with game speed
      double flapSpeed = 0.3 * (m_model.getWorldScrollSpeed() / 4.0);
      AffineTransform transform = new AffineTransform();
      transform.translate(x, visualY);
      transform.scale(1.0 + 0.1 * Math.sin(frameCounter * flapSpeed), 1.0); // Slight horizontal scaling
      transform.translate(-imageWidth/2, -imageHeight/2);
      
      g.drawImage(pigeonImage, transform, null);
      
      // Optional: Draw collision area and jump collision zone for debugging
      if (m_debugMode) {
        // Collision area at player level (slide to avoid)
        g.setColor(new Color(255, 0, 0, 80)); // Semi-transparent red
        g.fillOval((int)(x - 20), (int)(y - 10), 40, 20);
        g.setColor(Color.RED);
        g.drawString("Slide Zone", (int)(x - 25), (int)(y + 25));
        
        // Jump collision zone above player
        double jumpZoneY = y - size * 2.0; // Jump collision zone
        g.setColor(new Color(255, 165, 0, 80)); // Semi-transparent orange
        g.fillOval((int)(x - 20), (int)(jumpZoneY - 10), 40, 20);
        g.setColor(Color.ORANGE);
        g.drawString("Jump Zone", (int)(x - 25), (int)(jumpZoneY + 25));
      }
    } else {
      // Fallback with visual offset and animation
      double baseVisualOffsetY = -size * 3.0;
      double animationRange = size * 1.5;
      double animationSpeed = 0.8 * (m_model.getWorldScrollSpeed() / 4.0);
      double verticalAnimation = Math.sin(frameCounter * animationSpeed * 0.05) * animationRange;
      double visualY = y + baseVisualOffsetY + verticalAnimation;
      
      drawPigeonFallback(g, pigeon, x, visualY, size);
    }
  }
  
  /**
   * Draw enemy shooter using properly scaled image
   */
  private void drawEnemyShooter(Graphics2D g, EnemyShooter enemy, double x, double y, double size) {
    BufferedImage enemyImage = imageLoader.getObstacleImage(ImageLoader.ENEMY_SHOOTER);
    
    if (enemyImage != null) {
      int imageWidth = enemyImage.getWidth();
      int imageHeight = enemyImage.getHeight();
      
      // Center the enemy in the cell
      int drawX = (int) (x - imageWidth / 2);
      int drawY = (int) (y - imageHeight / 2);
      g.drawImage(enemyImage, drawX, drawY, null);
    } else {
      drawEnemyShooterFallback(g, enemy, x, y, size);
    }
  }
  
  /**
   * Draw projectile using small scaled image
   */
  private void drawProjectile(Graphics2D g, Projectile projectile, double x, double y, double size) {
    BufferedImage projectileImage = imageLoader.getProjectileImage();
    
    if (projectileImage != null) {
      int imageWidth = projectileImage.getWidth();
      int imageHeight = projectileImage.getHeight();
      
      // Center the projectile
      int drawX = (int) (x - imageWidth / 2);
      int drawY = (int) (y - imageHeight / 2);
      
      // Rotate projectile based on direction
      AffineTransform transform = new AffineTransform();
      transform.translate(x, y);
      transform.rotate(Math.toRadians(projectile.orientation()));
      transform.translate(-imageWidth/2, -imageHeight/2);
      
      g.drawImage(projectileImage, transform, null);
    } else {
      drawProjectileFallback(g, projectile, x, y, size);
    }
  }
  
  /**
   * Draw coin using properly scaled collectible image
   */
  private void drawCoin(Graphics2D g, Coin coin, double x, double y, double size) {
    BufferedImage coinImage = imageLoader.getCollectibleImage(ImageLoader.COIN);
    
    if (coinImage != null) {
      int imageWidth = coinImage.getWidth();
      int imageHeight = coinImage.getHeight();
      
      // Add spinning animation
      AffineTransform transform = new AffineTransform();
      transform.translate(x, y);
      transform.rotate(frameCounter * 0.1); // Slow rotation
      transform.translate(-imageWidth/2, -imageHeight/2);
      
      g.drawImage(coinImage, transform, null);
      
      // Add shine effect
      g.setColor(new Color(255, 255, 255, 100));
      g.fillOval((int)(x - imageWidth/4), (int)(y - imageHeight/4), imageWidth/2, imageHeight/2);
    } else {
      drawCoinFallback(g, coin, x, y, size);
    }
  }
  
  /**
   * Draw health pickup using properly scaled collectible image
   */
  private void drawHealthPickup(Graphics2D g, HealthPickup health, double x, double y, double size) {
    BufferedImage healthImage = imageLoader.getCollectibleImage(ImageLoader.HEALTH);
    
    if (healthImage != null) {
      int imageWidth = healthImage.getWidth();
      int imageHeight = healthImage.getHeight();
      
      // Add pulsing animation
      double pulse = 1.0 + 0.2 * Math.sin(frameCounter * 0.2);
      int pulsedWidth = (int) (imageWidth * pulse);
      int pulsedHeight = (int) (imageHeight * pulse);
      
      int drawX = (int)(x - pulsedWidth/2);
      int drawY = (int)(y - pulsedHeight/2);
      
      g.drawImage(healthImage, drawX, drawY, pulsedWidth, pulsedHeight, null);
    } else {
      drawHealthPickupFallback(g, health, x, y, size);
    }
  }
  
  // Fallback drawing methods when images are not available
  private void drawPlayerFallback(Graphics2D g, Player player, double x, double y, double size, boolean isJumping, boolean isSliding) {
    if (isJumping) {
      g.setColor(new Color(255, 215, 0)); // Gold for jumping
    } else if (isSliding) {
      g.setColor(new Color(138, 43, 226)); // Blue violet for sliding
    } else {
      g.setColor(new Color(0, 150, 255)); // Blue for normal
    }
    
    // Draw simple triangle
    int[] xPoints = {(int)(x + size/3), (int)(x - size/3), (int)(x - size/3)};
    int[] yPoints = {(int)y, (int)(y - size/3), (int)(y + size/3)};
    g.fillPolygon(xPoints, yPoints, 3);
  }
  
  private void drawRockFallback(Graphics2D g, Rock rock, double x, double y, double size) {
    g.setColor(new Color(139, 69, 19));
    g.fillRect((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);
  }
  
  private void drawPigeonFallback(Graphics2D g, Pigeon pigeon, double x, double y, double size) {
    // Use the same animation system as the main drawing method
    double baseVisualOffsetY = -size * 2.0; // Bigger distance between altitudes
    double animationRange = size * 1.0; // Animation range
    double animationSpeed = 0.8 * (m_model.getWorldScrollSpeed() / 4.0);
    double verticalAnimation = Math.sin(frameCounter * animationSpeed * 0.05) * animationRange;
    double visualY = y + baseVisualOffsetY + verticalAnimation;
    
    g.setColor(Color.GRAY);
    g.fillOval((int)(x - size/2), (int)(visualY - size/2), (int)size, (int)size);
    
    // Optional debug indicators for collision areas
    if (m_debugMode) {
      // Ground collision area (slide to avoid)
      g.setColor(new Color(255, 0, 0, 100)); // Semi-transparent red
      g.drawOval((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);
      
      // Jump collision zone
      double jumpZoneY = y - size * 2.0;
      g.setColor(new Color(255, 165, 0, 100)); // Semi-transparent orange
      g.drawOval((int)(x - size/2), (int)(jumpZoneY - size/2), (int)size, (int)size);
    }
  }
  
  private void drawEnemyShooterFallback(Graphics2D g, EnemyShooter enemy, double x, double y, double size) {
    g.setColor(Color.RED);
    g.fillRect((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);
  }
  
  private void drawProjectileFallback(Graphics2D g, Projectile projectile, double x, double y, double size) {
    g.setColor(Color.ORANGE);
    g.fillOval((int)(x - size/4), (int)(y - size/4), (int)(size/2), (int)(size/2));
  }
  
  private void drawCoinFallback(Graphics2D g, Coin coin, double x, double y, double size) {
    g.setColor(Color.YELLOW);
    g.fillOval((int)(x - size/3), (int)(y - size/3), (int)(size*2/3), (int)(size*2/3));
  }
  
  private void drawHealthPickupFallback(Graphics2D g, HealthPickup health, double x, double y, double size) {
    g.setColor(Color.PINK);
    g.fillOval((int)(x - size/3), (int)(y - size/3), (int)(size*2/3), (int)(size*2/3));
  }
  
  private void drawGenericEntity(Graphics2D g, Entity entity, double x, double y, double size) {
    g.setColor(Color.MAGENTA);
    g.fillOval((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);
  }
  
  private void drawDebugInfo(Graphics2D g) {
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.PLAIN, 12));
    g.drawString("Frame: " + frameCounter, 10, 200);
    g.drawString("Viewport: " + String.format("%.2f, %.2f", m_viewportX, m_viewportY), 10, 220);
    g.drawString("Zoom: " + String.format("%.2f", m_zoomLevel), 10, 240);
  }
  
  @Override
  protected Avatar createAvatarFor(Entity entity) {
    if (entity instanceof Player) {
      return new PlayerAvatar(this, entity);
    } else if (entity instanceof Projectile) {
      return new ProjectileAvatar(this, entity);
    }
    return null; // No avatar for unknown entity types
  }
}