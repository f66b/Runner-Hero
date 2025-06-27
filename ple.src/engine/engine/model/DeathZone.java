package engine.model;

/**
 * DeathZone is an invisible obstacle representing an instant-kill area
 * (e.g., a pit or lethal wall). When the player collides with it the game ends.
 *
 * Aya – complete {@link #onPlayerCollision(Player)} to trigger the defeat logic
 * (e.g., set player HP to 0, notify Game over screen…).
 */
public class DeathZone extends Entity {

    public DeathZone(Model model, int row, int col) {
        super(model, row, col, 0);
    }

    @Override
    public void update(double deltaTime) {
        // Stationary hazard – no movement required.
        
        // Check if player is in the same cell
        Player player = m_model.player();
        if (player != null && player.row() == this.row() && player.col() == this.col()) {
            onPlayerCollision(player);
        }
    }

    /**
     * Handle collision with player - instant death
     */
    public void onPlayerCollision(Player player) {
        // Implement defeat condition – immediate game over
        System.out.println("Player entered death zone - Game Over!");
        player.die(); // Instant death
    }
} 