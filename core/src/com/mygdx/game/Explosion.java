package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {
    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;
    private Rectangle bounds;

    public Explosion(TextureRegion[] textureRegions1D, Rectangle bounds, float totalAnimationTime) {
        this.bounds = bounds;
        explosionAnimation = new Animation<TextureRegion>(totalAnimationTime/8, textureRegions1D);
        explosionTimer = 0;
    }

    public void update(float deltaTime) {
        explosionTimer += deltaTime;
    }

    public void render(SpriteBatch batch) {
        batch.draw(explosionAnimation.getKeyFrame(explosionTimer),
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height);
    }

    public boolean isFinished() {
        return explosionAnimation.isAnimationFinished(explosionTimer);
    }
}
