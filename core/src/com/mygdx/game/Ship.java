package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Ship {
    float speed;
    int shield;
    Rectangle bounds;
    float bulWidth, bulHeight;
    float bulSpeed;
    float bulDuration;
    float timeFromLastFire = 0f;
    float animationShipTimer;

    Animation<TextureRegion> shipAnimation;
    TextureRegion bulTextureRegion;

    public Ship(float xCentre, float yCentre,
                float width, float height,
                float movementSpeed,
                float laserWidth, float laserHeight, float laserMovementSpeed,
                float timeBetweenShots,
                TextureRegion[] shipTextureRegion,
                TextureRegion laserTextureRegion) {
        this.speed = movementSpeed;

        this.bounds = new Rectangle(xCentre - width/2, yCentre - height/2, width, height);

        this.bulWidth = laserWidth;
        this.bulHeight = laserHeight;
        this.bulSpeed = laserMovementSpeed;
        this.bulDuration = timeBetweenShots;
        this.shipAnimation = new Animation<>(0.5f/3, shipTextureRegion);
        this.bulTextureRegion = laserTextureRegion;
        this.animationShipTimer = 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void update(float deltaTime) {
        timeFromLastFire += deltaTime;
        animationShipTimer += deltaTime;
    }

    public boolean canFire() {
        return (timeFromLastFire - bulDuration) >= 0;
    }

    public abstract Bullet fire();

    public boolean overlaps(Rectangle otherRect) {
        return bounds.overlaps(otherRect);
    }

    public void translate(float dX, float dY) {
        bounds.setPosition(bounds.getX() + dX, bounds.getY() + dY);
    }

    public void render(Batch batch) {
        batch.draw(shipAnimation.getKeyFrame(animationShipTimer, true), bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
