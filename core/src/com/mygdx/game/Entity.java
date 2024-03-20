package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {
    private Rectangle bounds;
    private TextureRegion texture;

    public Entity(TextureRegion texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public abstract void update(float deltaTime);
    public abstract void destroy();

    public abstract void render(Batch batch);
}
