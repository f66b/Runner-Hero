package engine.model;

/**
 * Coin gives score points when collected and then disappears.
 *
 * Rim – implement {@link #onPickUp(Player)} to increment the global score
 * (e.g., via ScoreManager) and remove the coin from the world.
 */
public class Coin extends Collectable {

    public static final int VALUE = 10;

    public Coin(Model model, int row, int col) {
        super(model, row, col);
    }

    @Override
    public void update(double deltaTime) {
        // No movement for basic coin.
    }

    @Override
    public void onPickUp(Player player) {
        // TODO (Rim): add VALUE to ScoreManager/Player, then remove this entity.
    }
} 