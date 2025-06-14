package engine.model;

public abstract class Category {
    public abstract boolean equals(Category c);
    public abstract boolean specializes(Category c);

    // Basic categories
    public static final Category Player = new CategoryPlayer();
    public static final Category Adversary = new CategoryAdversary();
    public static final Category Obstacle = new CategoryObstacle();
    public static final Category Item = new CategoryItem();
    public static final Category Danger = new CategoryDanger();
}

class CategoryPlayer extends Category {
    @Override
    public boolean equals(Category c) {
        return c instanceof CategoryPlayer;
    }

    @Override
    public boolean specializes(Category c) {
        return equals(c);
    }
}

class CategoryAdversary extends Category {
    @Override
    public boolean equals(Category c) {
        return c instanceof CategoryAdversary;
    }

    @Override
    public boolean specializes(Category c) {
        return equals(c);
    }
}

class CategoryObstacle extends Category {
    @Override
    public boolean equals(Category c) {
        return c instanceof CategoryObstacle;
    }

    @Override
    public boolean specializes(Category c) {
        return equals(c);
    }
}

class CategoryItem extends Category {
    @Override
    public boolean equals(Category c) {
        return c instanceof CategoryItem;
    }

    @Override
    public boolean specializes(Category c) {
        return equals(c);
    }
}

class CategoryDanger extends Category {
    @Override
    public boolean equals(Category c) {
        return c instanceof CategoryDanger;
    }

    @Override
    public boolean specializes(Category c) {
        return equals(c);
    }
} 
