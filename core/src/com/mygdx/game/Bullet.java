package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Bullet extends Entity {
    private float speed;
    public Bullet(TextureRegion texture, float x, float y, float width, float height, float speed) {
        super(texture, x, y, width, height);
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public void update(float deltaTime) {
        float newY = this.getBounds().getY() + speed*deltaTime;
        getBounds().setPosition(getBounds().getX(), newY);
    }

    public void  updateOfEnem(float deltaTime) {
        float newY = this.getBounds().getY() - speed*deltaTime;
        getBounds().setPosition(getBounds().getX(), newY);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void render(Batch batch) {
        batch.draw(this.getTexture(), this.getBounds().getX(), this.getBounds().getY(), this.getBounds().getWidth(), this.getBounds().getHeight());
    }
}
