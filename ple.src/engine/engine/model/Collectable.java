package engine.model;

/**
 * Base class for items that the player can pick up (coins, health packs, etc.).
 * When the player collides with a Collectable, {@link #onPickUp(Player)} must be
 * triggered then the entity should remove itself from the model.
 *
 * Rim (Collectables) – implement concrete subclasses (Coin, HealthPickup…) that
 * define their specific effect in {@link #onPickUp(Player)}.
 */
public abstract class Collectable extends Entity {

    protected Collectable(Model model, int row, int col) {
        super(model, row, col, 0);
    }

    /**
     * Called when the player picks up this collectable.
     * Concrete subclasses must implement the corresponding effect.
     */
    public abstract void onPickUp(Player player);
} 