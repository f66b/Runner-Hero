package engine.model;

/**
 * DeathZone is an invisible obstacle representing an instant-kill area
 * (e.g., a pit or lethal wall). When the player collides with it the game ends.
 *
 * Aya – complete {@link #onPlayerCollision(Player)} to trigger the defeat logic
 * (e.g., set player HP to 0, notify Game over screen…).
 */
public class DeathZone extends Obstacle {

    public DeathZone(Model model, int row, int col) {
        super(model, row, col, 0);
    }

    @Override
    public void update(double deltaTime) {
        // Stationary hazard – no movement required.
    }

    @Override
    public void onPlayerCollision(Player player) {
        // TODO (Aya): implement defeat condition – immediate game over.
    }
} 