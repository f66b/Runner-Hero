package engine;

/**
 * IController interface defines the contract for user input handling.
 * Controllers translate user input into model actions.
 */
public interface IController extends Ticker.TickListener {
    // Key handling
    void handleKeyPress(int keyCode, char keyChar);
    void handleKeyRelease(int keyCode, char keyChar);
    
    // Mouse handling
    void handleMouseMove(int px, int py);
    void handleMousePress(int bno, int x, int y);
    void handleMouseRelease(int bno, int x, int y);
    
    // Update control state
    void update(double deltaTime);
    
    // Control state queries
    boolean isPlayerMoving();
    boolean isPlayerRotating();
    double getPlayerTargetAngle();
}
