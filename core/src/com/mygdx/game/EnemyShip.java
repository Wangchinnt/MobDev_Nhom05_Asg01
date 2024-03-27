package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class EnemyShip extends Ship{
    Vector2 dirVector;
    float timeFromLastMove;
    float moveF = 2f;
    private Animation<TextureRegion> shipAnimation;
    public EnemyShip(float xCentre, float yCentre,
                      float width, float height,
                      float movementSpeed,
                      float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion[] shipTextureRegion,
                      TextureRegion laserTextureRegion) {
        super(xCentre, yCentre, width, height, movementSpeed, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots, shipTextureRegion, laserTextureRegion);

        dirVector = new Vector2(0, -1);
    }

    public Vector2 getDirVector() {
        return dirVector;
    }

    private void randomDirVector() {
        double bearing = MathUtils.random.nextDouble()*6.283185;
        dirVector.x = (float) Math.sin(bearing);
        dirVector.y = (float) Math.cos(bearing);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeFromLastMove += deltaTime;
        if (timeFromLastMove > moveF) {
            randomDirVector();
            timeFromLastMove -= moveF;
        }
//        float newY = bounds.y - deltaTime*speed;
//        bounds.setPosition(bounds.x, newY);
    }

    @Override
    public Bullet fire() {
        timeFromLastFire = 0;
        return new Bullet(bulTextureRegion, bounds.x + 4f, bounds.y, bulWidth, bulHeight, bulSpeed);
    }
}
