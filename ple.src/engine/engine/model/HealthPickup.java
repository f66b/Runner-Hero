package engine.model;

/**
 * HealthPickup restores one life point (up to the player maximum) when collected.
 *
 * Driss – implement {@link #onPickUp(Player)} so that it increases player's
 * health and handles invincibility frames or visual feedback if required.
 */
public class HealthPickup extends Collectable {

    public HealthPickup(Model model, int row, int col) {
        super(model, row, col);
    }

    @Override
    public void update(double deltaTime) {
        // No movement – stays in place until collected.
    }

    @Override
    public void onPickUp(Player player) {
        // TODO (Driss): increment player HP (max 3) then remove this entity
        // from the model.
    }
} 