package engine.view;

import java.awt.Graphics2D;
import engine.model.Entity;

public abstract class Avatar {
    protected View view;
    protected Entity entity;

    public Avatar(View view, Entity entity) {
        this.view = view;
        this.entity = entity;
        entity.avatar=this;
    }

    public abstract void render(Graphics2D g);
} 