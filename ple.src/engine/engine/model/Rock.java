package engine.model;

public class Rock extends Entity {

    public Rock(Model model, int row, int col) {
        super(model, row, col, 0); // orientation irrelevant for static rock
        // Rock is immobile; no velocity
    }

    @Override
    public void update(double deltaTime) {
        // Rock remains static, so no position or orientation updates are necessary.
    }
} 