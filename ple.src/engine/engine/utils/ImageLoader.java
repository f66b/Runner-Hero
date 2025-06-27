package engine.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * ImageLoader handles loading and caching of PNG images for game entities.
 * Images are loaded from an absolute filesystem path.
 */
public class ImageLoader {
    private static ImageLoader instance = null;
    private Map<String, BufferedImage> imageCache;

    // Base directory for image files (relative to project root)
    private static final String IMAGE_DIR = "/home/aminekcm/AgileLearning/oop/workspace/ple.src/resources/images/";

    // Image file names
    public static final String PLAYER_POSITION1 = "playerposition1.png";
    public static final String PLAYER_POSITION2 = "playerposition2.png";
    public static final String PLAYER_JUMP      = "playerjump.png";
    public static final String PLAYER_SLIDE     = "playerglisse.png";
    public static final String BACKGROUND       = "backround.png";
    public static final String PIGEON           = "pigeon.png";
    public static final String ENEMY_SHOOTER    = "enemyshooter.png";
    public static final String ROCK             = "rock.png";
    public static final String PROJECTILE       = "projectile.png";
    public static final String COIN             = "coin.png";
    public static final String HEALTH           = "health.png";

    private ImageLoader() {
        imageCache = new HashMap<>();
        loadAllImages();
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    /**
     * Load all required images into cache
     */
    private void loadAllImages() {
        loadImage(PLAYER_POSITION1);
        loadImage(PLAYER_POSITION2);
        loadImage(PLAYER_JUMP);
        loadImage(PLAYER_SLIDE);
        loadImage(BACKGROUND);
        loadImage(PIGEON);
        loadImage(ENEMY_SHOOTER);
        loadImage(ROCK);
        loadImage(PROJECTILE);
        loadImage(COIN);
        loadImage(HEALTH);
    }

    /**
     * Load an image from the filesystem (absolute path)
     */
    private void loadImage(String fileName) {
        File imgFile = new File(IMAGE_DIR + fileName);
        if (!imgFile.exists()) {
            System.err.println("Warning: Could not find image file: " + imgFile.getAbsolutePath());
            imageCache.put(fileName, createPlaceholder(fileName));
            return;
        }
        try {
            BufferedImage image = ImageIO.read(imgFile);
            if (image != null) {
                imageCache.put(fileName, image);
                System.out.println("Loaded image: " + fileName);
            } else {
                System.err.println("Failed to load image: " + fileName);
                imageCache.put(fileName, createPlaceholder(fileName));
            }
        } catch (IOException e) {
            System.err.println("Error loading image " + fileName + ": " + e.getMessage());
            imageCache.put(fileName, createPlaceholder(fileName));
        }
    }

    /**
     * Create a colored placeholder rectangle when image is not found
     */
    private BufferedImage createPlaceholder(String fileName) {
        BufferedImage placeholder = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = placeholder.createGraphics();

        // Set color based on entity type
        if (fileName.contains("player")) {
            g.setColor(java.awt.Color.BLUE);
        } else if (fileName.contains("pigeon")) {
            g.setColor(java.awt.Color.GRAY);
        } else if (fileName.contains("enemy")) {
            g.setColor(java.awt.Color.RED);
        } else if (fileName.contains("rock")) {
            g.setColor(java.awt.Color.BLACK);
        } else if (fileName.contains("projectile")) {
            g.setColor(java.awt.Color.ORANGE);
        } else if (fileName.contains("coin")) {
            g.setColor(java.awt.Color.YELLOW);
        } else if (fileName.contains("health")) {
            g.setColor(java.awt.Color.PINK);
        } else if (fileName.contains("backround")) {
            g.setColor(java.awt.Color.LIGHT_GRAY);
        } else {
            g.setColor(java.awt.Color.MAGENTA);
        }

        g.fillRect(0, 0, 64, 64);
        g.setColor(java.awt.Color.BLACK);
        g.drawRect(0, 0, 63, 63);

        // Draw text label
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 8));
        String label = fileName.substring(0, Math.min(8, fileName.length()));
        g.drawString(label, 2, 32);

        g.dispose();
        return placeholder;
    }

    /**
     * Get an image by filename
     */
    public BufferedImage getImage(String fileName) {
        BufferedImage image = imageCache.get(fileName);
        if (image == null) {
            System.err.println("Image not found in cache: " + fileName);
            return createPlaceholder(fileName);
        }
        return image;
    }

    /**
     * Check if an image exists in cache
     */
    public boolean hasImage(String fileName) {
        return imageCache.containsKey(fileName);
    }

    /**
     * Get the background image
     */
    public BufferedImage getBackground() {
        return getImage(BACKGROUND);
    }

    /**
     * Get player image based on state with proper scaling
     */
    public BufferedImage getPlayerImage(boolean isJumping, boolean isSliding, boolean alternateFrame) {
        BufferedImage originalImage;
        if (isJumping) {
            originalImage = getImage(PLAYER_JUMP);
        } else if (isSliding) {
            originalImage = getImage(PLAYER_SLIDE);
        } else {
            // Alternate between running frames
            originalImage = getImage(alternateFrame ? PLAYER_POSITION2 : PLAYER_POSITION1);
        }
        
        // Scale image to appropriate size for runner game (taller than wide)
        return scaleImageForPlayer(originalImage);
    }
    
    /**
     * Scale image specifically for player (runner proportions)
     */
    private BufferedImage scaleImageForPlayer(BufferedImage original) {
        if (original == null) return null;
        
        // Player should be taller and positioned at bottom-left of the cell
        int targetWidth = 48;  // Narrower for runner style
        int targetHeight = 64; // Taller for runner style
        
        BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        
        return scaled;
    }
    
    /**
     * Scale image for obstacles (square proportions)
     */
    private BufferedImage scaleImageForObstacle(BufferedImage original) {
        if (original == null) return null;
        
        int targetSize = 56; // Square obstacles
        
        BufferedImage scaled = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, targetSize, targetSize, null);
        g.dispose();
        
        return scaled;
    }
    
    /**
     * Scale image for collectibles (smaller, round)
     */
    private BufferedImage scaleImageForCollectible(BufferedImage original) {
        if (original == null) return null;
        
        int targetSize = 32; // Smaller collectibles
        
        BufferedImage scaled = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, targetSize, targetSize, null);
        g.dispose();
        
        return scaled;
    }
    
    /**
     * Get obstacle image with proper scaling
     */
    public BufferedImage getObstacleImage(String fileName) {
        BufferedImage original = getImage(fileName);
        return scaleImageForObstacle(original);
    }
    
    /**
     * Get collectible image with proper scaling
     */
    public BufferedImage getCollectibleImage(String fileName) {
        BufferedImage original = getImage(fileName);
        return scaleImageForCollectible(original);
    }
    
    /**
     * Get projectile image (small and fast)
     */
    public BufferedImage getProjectileImage() {
        BufferedImage original = getImage(PROJECTILE);
        if (original == null) return null;
        
        int targetSize = 16; // Very small projectiles
        
        BufferedImage scaled = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, targetSize, targetSize, null);
        g.dispose();
        
        return scaled;
    }
}