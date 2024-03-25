package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class PlayerShip extends Ship{

    int lives;

    public PlayerShip(float xCentre, float yCentre,
                      float width, float height,
                      float movementSpeed,
                      float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion[] shipTextureRegion,
                      TextureRegion laserTextureRegion) {
        super(xCentre, yCentre, width, height, movementSpeed, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots, shipTextureRegion, laserTextureRegion);
        lives = 3;
    }

    public void touchPosition(Camera camera) {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set((float) Gdx.input.getX(),(float) Gdx.input.getY(), 0f);
            camera.unproject(touchPos);
            this.bounds.setPosition(touchPos.x, touchPos.y);
        }
    }
    @Override
    public Bullet fire() {
        timeFromLastFire = 0;
        return new Bullet(bulTextureRegion, bounds.x + 5f, bounds.y + 5f, bulWidth, bulHeight, bulSpeed);
    }
}
