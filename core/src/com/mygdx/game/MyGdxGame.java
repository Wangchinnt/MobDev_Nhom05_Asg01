package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private float screenWidth;
	private float screenHeight;
	private float stateTime;
	private OrthographicCamera camera;
	private Rectangle pilotRect;
	private Rectangle bgRect;
	private Animation<TextureRegion> animation;
	private TextureRegion[] frames;
	private Texture enemyTexture;
	private Texture playerBullet;
	private Texture backgroundTexture;
	private Texture stargroundTexture;
	private Music gameMusic;
	private Sound exploreSound;
	private Long lastEnemyTime = 0L;
	private Long lastPlayerBullTime = 0L;
	private Array<Rectangle> enemysArray = new Array<Rectangle>();
	private Array<Rectangle> playerBulletArray = new Array<Rectangle>();
	BitmapFont font;

	private void renderRandomEnemy() {
		Rectangle enemy = new Rectangle();
		enemy.setY(screenHeight);
		enemy.setX(MathUtils.random(200, screenWidth-200));
		enemy.setWidth(192f);
		enemy.setHeight(232f);

		lastEnemyTime = TimeUtils.nanoTime();

		enemysArray.add(enemy);
	}
	private void renderPlayerBullet() {
		Rectangle playerBul = new Rectangle();
		playerBul.setX(pilotRect.getX() + 62f);
		playerBul.setY(pilotRect.getY() + 232f);
		playerBul.setWidth(64f);
		playerBul.setHeight(128f);

		lastPlayerBullTime = TimeUtils.nanoTime();

		playerBulletArray.add(playerBul);
	}

	@Override
	public void create () {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		batch = new SpriteBatch();

		font = new BitmapFont();
		font.getData().setScale(4f, 4f);

		//camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);

		//player
		pilotRect = new Rectangle();
		pilotRect.width = 192f;
		pilotRect.height = 232f;
		pilotRect.x = screenWidth/2 - 96f/2;
		pilotRect.y = 50f;

		//animation
		Texture texture1 = new Texture(Gdx.files.internal("player_1.png"));
		Texture texture2 = new Texture(Gdx.files.internal("player_2.png"));
		Texture texture3 = new Texture(Gdx.files.internal("player_3.png"));

		TextureRegion textureRegion1 = new TextureRegion(texture1);
		TextureRegion textureRegion2 = new TextureRegion(texture2);
		TextureRegion textureRegion3 = new TextureRegion(texture3);

		frames = new TextureRegion[] {textureRegion1, textureRegion2, textureRegion3};
		animation = new Animation<TextureRegion>(0.1f, frames);

		stateTime = 0f;

		//background
		bgRect = new Rectangle();
		bgRect.setX(0f);
		bgRect.setY(0f);

		enemyTexture = new Texture(Gdx.files.internal("enemy_1_1.png"));
		backgroundTexture = new Texture(Gdx.files.internal("background2.png"));
		stargroundTexture = new Texture(Gdx.files.internal("stars_1.png"));
		playerBullet = new Texture(Gdx.files.internal("player_bullet.png"));

		//sound
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("starwarbgsound.mp3"));
		exploreSound = Gdx.audio.newSound(Gdx.files.internal("laser.wav"));

		gameMusic.setLooping(true);
		gameMusic.play();
	}

	@Override
	public void render () {
		ScreenUtils.clear(26/100f, 2/100f, 56/100f, 1);

		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set((float) Gdx.input.getX(),(float) Gdx.input.getY(), 0f);
			camera.unproject(touchPos);
			pilotRect.setX(touchPos.x);
			pilotRect.setY(touchPos.y);
		}

		if(TimeUtils.nanoTime() - lastEnemyTime > 2000000000) renderRandomEnemy();
		if(TimeUtils.nanoTime() - lastPlayerBullTime > 400000000) renderPlayerBullet();

		//move star background
		bgRect.setY(bgRect.getY()-300*Gdx.graphics.getDeltaTime());
		if (bgRect.getY() <= -Gdx.graphics.getHeight()) {
			bgRect.setY(0f);
		}

		//move enemy
		Iterator<Rectangle> iter = enemysArray.iterator();
		while (iter.hasNext()) {
			Rectangle enemy = iter.next();
			enemy.setY(enemy.getY()-400*Gdx.graphics.getDeltaTime());

			if (enemy.getY() < 0) iter.remove();
			if (enemy.overlaps(pilotRect)) {
				iter.remove();
				exploreSound.play(0.5f);
			}
		}
		//move playerBullet
		Iterator<Rectangle> iter2 = playerBulletArray.iterator();
		while (iter2.hasNext()) {
			Rectangle playerBul = iter2.next();
			playerBul.setY(playerBul.getY()+1000*Gdx.graphics.getDeltaTime());

			if (playerBul.getY() > screenHeight) iter2.remove();
		}

		//draw textures
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(stargroundTexture, 0, bgRect.getY(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*2);
		batch.draw(currentFrame, pilotRect.x, pilotRect.y, pilotRect.getWidth(), pilotRect.getHeight());
		font.draw(batch, String.valueOf(TimeUtils.nanoTime()), 10, screenHeight - 20);
		font.draw(batch, String.valueOf(TimeUtils.nanoTime()-lastEnemyTime), screenWidth-300, screenHeight-20);

		//draw enemies
		for (Rectangle enemy:enemysArray) {
			batch.draw(enemyTexture, enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
		}

		//draw bullets
		for (Rectangle playerBul:playerBulletArray) {
			batch.draw(playerBullet, playerBul.getX(), playerBul.getY(), playerBul.getWidth(), playerBul.getHeight());
		}
		
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
