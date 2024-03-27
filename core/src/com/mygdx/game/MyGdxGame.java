package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Stage endStage;
	private AndroidAction androidAction;
	private String name;
	private int model;
	private float screenWidth;
	private float screenHeight;
	private float backToMenuTime;
	private final float WORLD_WIDTH = 72;
	private final float WORLD_HEIGHT = 128;
	private GameState gameState = GameState.Running;
	boolean gyroscopeAvail;
	private int points;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Label timeLabel;
	private Rectangle bgRect;
	private TextureRegion[] frames;
	private TextureRegion[] enemyTextureRegions;
	private TextureRegion[] explosionTextureRegions;
	private Texture playerBullet;
	private Texture enemyBullet;
	private Texture backgroundTexture;
	private Texture stargroundTexture;
	private Texture explosionSheet;
	private Texture enemyTextureSheet;
	private Texture hearth;
	private Music gameMusic;
	private Sound exploreSound;
	BitmapFont font;
	//Game timer
	private float enemySpawnTimer;
	private float enemySpawnGap;
	//Game object
	private PlayerShip playerShip;
	private Array<EnemyShip> enemyShipArray = new Array<>();
	private Array<Bullet> playerBulletArray = new Array<>();
	private Array<Bullet> enemyBulletArray = new Array<>();
	private Array<Explosion> explosionArray = new Array<>();

	public MyGdxGame(AndroidAction androidAction, String name, int model) {
		this.androidAction = androidAction;
		this.name = name;
		this.model = model;
	}

	@Override
	public void create () {
		gyroscopeAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
		batch = new SpriteBatch();
		endStage = new Stage();
		prepareHUD();
		backToMenuTime = 5f;
		enemySpawnGap = 1f;
		enemySpawnTimer = 0f;
		points = 0;
		//
		hearth = new Texture(Gdx.files.internal("heart.png"));
		//camera
		camera = new OrthographicCamera();
		viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

		//player ship animation
//		Texture texture1 = new Texture(Gdx.files.internal("player_1_1.png"));
//		Texture texture2 = new Texture(Gdx.files.internal("player_2_1.png"));
//		Texture texture3 = new Texture(Gdx.files.internal("player_3_1.png"));
//		TextureRegion textureRegion1 = new TextureRegion(texture1);
//		TextureRegion textureRegion2 = new TextureRegion(texture2);
//		TextureRegion textureRegion3 = new TextureRegion(texture3);
//		frames = new TextureRegion[] {textureRegion1, textureRegion2, textureRegion3};

		//load explosion sprite sheet
		explosionSheet = new Texture(Gdx.files.internal("explosion-spritesheet.png"));

		//background
		bgRect = new Rectangle();
		bgRect.setX(0f);
		bgRect.setY(0f);

		//other texture
		enemyTextureSheet = new Texture(Gdx.files.internal("spritesheet_enemy_1.png"));
		backgroundTexture = new Texture(Gdx.files.internal("background2.png"));
		stargroundTexture = new Texture(Gdx.files.internal("stars_1.png"));
		playerBullet = new Texture(Gdx.files.internal("player_bullet.png"));
		enemyBullet = new Texture(Gdx.files.internal("projectile_2.png"));
		createPlayer();

//		playerShip = new PlayerShip(WORLD_WIDTH/2,
//				WORLD_HEIGHT/4, 9.6f,
//				11.6f, 70,
//				1f, 4,
//				150, 0.1f,
//				frames, new TextureRegion(playerBullet));

		//Enemy texture region
		enemyTextureRegions = loadEnemyAnimation(enemyTextureSheet);
		explosionTextureRegions = loadExplosionAnimation(explosionSheet);

		//sound
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("starwarbgsound.mp3"));
		gameMusic.setVolume(0.2f);
		exploreSound = Gdx.audio.newSound(Gdx.files.internal("laser.wav"));
		gameMusic.setLooping(true);
		gameMusic.play();
	}
	private void createPlayer() {
		String path_1 = "player_1_" + model + ".png";
		String path_2 = "player_2_" + model + ".png";
		String path_3 = "player_3_" + model + ".png";

		Texture texture1 = new Texture(Gdx.files.internal(path_1));
		Texture texture2 = new Texture(Gdx.files.internal(path_2));
		Texture texture3 = new Texture(Gdx.files.internal(path_3));

		TextureRegion textureRegion1 = new TextureRegion(texture1);
		TextureRegion textureRegion2 = new TextureRegion(texture2);
		TextureRegion textureRegion3 = new TextureRegion(texture3);

		frames = new TextureRegion[] {textureRegion1, textureRegion2, textureRegion3};

		playerShip = new PlayerShip(WORLD_WIDTH/2,
				WORLD_HEIGHT/4, 9.6f,
				11.6f, 70,
				1f, 4,
				150, 0.1f,
				frames, new TextureRegion(playerBullet));
	}

	public void prepareHUD() {
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("thaleah.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		fontParameter.size = 72;
		fontParameter.borderWidth = 3.6f;
		fontParameter.color = new Color(1, 1, 1, 0.3f);
		fontParameter.borderColor = new Color(0, 0, 0, 0.3f);
		font = fontGenerator.generateFont(fontParameter);

		font.getData().setScale(0.08f);
	}

	public TextureRegion[] loadEnemyAnimation(Texture texture) {
		TextureRegion[][] textureRegions2D = TextureRegion.split(texture, 48, 48);
		TextureRegion[] textureRegions1D = new TextureRegion[3];
		int index = 0;
		for (int i=0; i<3; i++) {
            textureRegions1D[index] = textureRegions2D[0][i];
            index++;
        }
		return textureRegions1D;
	}

	public TextureRegion[] loadExplosionAnimation(Texture texture) {
		TextureRegion[][] textureRegions2D = TextureRegion.split(texture, 48, 48);
		TextureRegion[] textureRegions1D = new TextureRegion[3];
		int index = 0;
		for (int i=0; i<3; i++) {
			textureRegions1D[index] = textureRegions2D[0][i];
			index++;
		}
		return textureRegions1D;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		if (playerShip.lives <= 0) {
			gameOverHud();
			gameState = GameState.Pause;
		}

		if (gameState == GameState.Running) {
			update(Gdx.graphics.getDeltaTime());
			draw();
		}
	}

	public void update(float deltaTime) {
		handleInput(deltaTime);
		playerShip.update(deltaTime);
		if (enemyShipArray.size <= 10) {
			spawnEnemy(deltaTime);
		}
		spawnBullet(deltaTime);
		//move star background
		bgRect.setY(bgRect.getY()-30*deltaTime);
		if (bgRect.getY() <= -WORLD_HEIGHT) {
			bgRect.setY(0f);
		}
		//move enemy
		Iterator<EnemyShip> iter = enemyShipArray.iterator();
		while (iter.hasNext()) {
			EnemyShip enemy = iter.next();
			moveEnemy(enemy, deltaTime);
			enemy.update(deltaTime);
		}
		checkCollision();
	}

	public void draw() {
		batch.begin();

		//draw textures
		batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH,WORLD_HEIGHT);
		batch.draw(stargroundTexture, 0, bgRect.getY(), WORLD_WIDTH, WORLD_HEIGHT*2);
		playerShip.render(batch);

		//draw enemies
		for (EnemyShip enemyShip:enemyShipArray) {
			enemyShip.render(batch);
		}
		//draw bullets
		for (Bullet playerBul:playerBulletArray) {
			playerBul.render(batch);
		}
		for (Bullet enemyBul:enemyBulletArray) {
			enemyBul.render(batch);
		}

		renderExplosion();

		for (int i=0; i<playerShip.lives; i++) {
			batch.draw(hearth, WORLD_WIDTH/20 + 9.6f*i, (float) (WORLD_HEIGHT*18.5/20), 6.4f, 6.4f);
		}
		font.draw(batch, "score : " + points, WORLD_WIDTH/20, WORLD_HEIGHT-13);
		font.draw(batch, "name : " + name, WORLD_WIDTH/20, WORLD_HEIGHT-20);

		batch.end();
	}

	public void moveEnemy(EnemyShip enemyShip, float deltaTime) {
		float leftLimit, rightLimit, upLimit, downLimit;
		leftLimit = -enemyShip.bounds.x;
		downLimit = WORLD_HEIGHT / 2 - enemyShip.bounds.y;
		rightLimit = WORLD_WIDTH - enemyShip.bounds.x - enemyShip.bounds.width;
		upLimit = WORLD_HEIGHT - enemyShip.bounds.y - enemyShip.bounds.height;


		float xMove = enemyShip.getDirVector().x * enemyShip.speed * deltaTime;
		float yMove = enemyShip.getDirVector().y * enemyShip.speed * deltaTime;

		if (xMove > 0) xMove = Math.min(xMove, rightLimit);
		else xMove = Math.max(xMove, leftLimit);

		if (yMove > 0) yMove = Math.min(yMove, upLimit);
		else yMove = Math.max(yMove, downLimit);

		enemyShip.translate(xMove, yMove);
	}

	public void spawnBullet(float deltaTime) {
		if (playerShip.canFire()) {
			Bullet bullet = playerShip.fire();
			playerBulletArray.add(bullet);
		}
		Iterator<EnemyShip> liver = enemyShipArray.iterator();
		while (liver.hasNext()) {
			EnemyShip enemyShip = liver.next();
			enemyShip.update(deltaTime);
			if (enemyShip.canFire()) {
				Bullet bullet = enemyShip.fire();
				enemyBulletArray.add(bullet);
			}
		}

		Iterator<Bullet> iterMilan = playerBulletArray.iterator();
		while (iterMilan.hasNext()) {
			Bullet bullet = iterMilan.next();
			bullet.update(deltaTime);
			if (bullet.getBounds().y > WORLD_HEIGHT) iterMilan.remove();
		}
		Iterator<Bullet> mancity = enemyBulletArray.iterator();
		while (mancity.hasNext()) {
			Bullet bullet = mancity.next();
			bullet.updateOfEnem(deltaTime);
			if (bullet.getBounds().y + bullet.getBounds().height < 0) mancity.remove();
		}
	}

	public void renderExplosion() {
		Iterator<Explosion> iterator = explosionArray.iterator();
		while (iterator.hasNext()) {
			Explosion explosion = iterator.next();
			explosion.update(Gdx.graphics.getDeltaTime());
			if (explosion.isFinished()) {
				iterator.remove();
			} else {
				explosion.render(batch);
			}
		}
	}
	public void handleInput(float deltaTime) {
		float rightLimit, leftLimit, upLimit, downLimit;
		leftLimit = -playerShip.bounds.x;
		downLimit = -playerShip.bounds.y;
		rightLimit = WORLD_WIDTH - playerShip.bounds.x - playerShip.bounds.width;
		upLimit = WORLD_HEIGHT - playerShip.bounds.y - playerShip.bounds.height;

//		if (gyroscopeAvail) {
//			float gyroY = Gdx.input.getGyroscopeY();
//			if (gyroY > 0) {
//				playerShip.translate(deltaTime*playerShip.speed/100000, playerShip.bounds.y);
//			} else if (gyroY < 0) {
//				playerShip.translate(-playerShip.speed*deltaTime/100000, playerShip.bounds.y);
//			}
//		}

		if (Gdx.input.isTouched()) {
			float xTouchPixels = Gdx.input.getX();
			float yTouchPixels = Gdx.input.getY();

			//convert to world position
			Vector2 touchPoint = new Vector2(xTouchPixels, yTouchPixels);
			touchPoint = viewport.unproject(touchPoint);

			Vector2 playerShipCentre = new Vector2(
					playerShip.bounds.x + playerShip.bounds.width / 2,
					playerShip.bounds.y + playerShip.bounds.height / 2);

			float touchDistance = touchPoint.dst(playerShipCentre);

			if (touchDistance > 5f) {
				float xTouchDifference = touchPoint.x - playerShipCentre.x;
				float yTouchDifference = touchPoint.y - playerShipCentre.y;

				//scale to the maximum speed of the ship
				float xMove = xTouchDifference / touchDistance * playerShip.speed * deltaTime;
				float yMove = yTouchDifference / touchDistance * playerShip.speed * deltaTime;

				if (xMove > 0) xMove = Math.min(xMove, rightLimit);
				else xMove = Math.max(xMove, leftLimit);

				if (yMove > 0) yMove = Math.min(yMove, upLimit);
				else yMove = Math.max(yMove, downLimit);

				playerShip.translate(xMove, yMove);
			}
		}
	}

	public void spawnEnemy(float deltaTime) {
		enemySpawnTimer += deltaTime;
		if (enemySpawnTimer > enemySpawnGap) {
			enemyShipArray.add(new EnemyShip(MathUtils.random.nextFloat() * (WORLD_WIDTH-10) + 5,
					WORLD_HEIGHT - 5, 10, 10,
					40, 5,
					4, 80,
					2f, enemyTextureRegions, new TextureRegion(enemyBullet)));
			enemySpawnTimer -= enemySpawnGap;
		}
	}

	public void checkCollision() {
		Iterator<EnemyShip> enemyShipIterator = enemyShipArray.iterator();
		while (enemyShipIterator.hasNext()) {
			EnemyShip enemyShip = enemyShipIterator.next();
			if (playerShip.overlaps(enemyShip.getBounds())) {
				enemyShipIterator.remove();
				playerShip.lives -= 1;
			}
			Iterator<Bullet> bulletIterator = playerBulletArray.iterator();
			while (bulletIterator.hasNext()) {
				Bullet playerBullet = bulletIterator.next();
				if (enemyShip.overlaps(playerBullet.getBounds())) {
					exploreSound.play();
					explosionArray.add(
							new Explosion(explosionTextureRegions,
									new Rectangle(enemyShip.getBounds()),
									0.5f));
					enemyShipIterator.remove();
					bulletIterator.remove();
					points += 10;
				}
			}
		}

		Iterator<Bullet> enemyBulletIterator = enemyBulletArray.iterator();
		while (enemyBulletIterator.hasNext()) {
			Bullet enemyBullet = enemyBulletIterator.next();
			if (playerShip.overlaps(enemyBullet.getBounds())) {
				exploreSound.play();
				playerShip.lives -= 1;
				enemyBulletIterator.remove();
			}
		}
	}

	public void gameOverHud() {
		if (endStage == null) {
			endStage = new Stage();
			Gdx.input.setInputProcessor(endStage);
		} else {
			endStage.clear();
		}

		Skin skin = new Skin(Gdx.files.internal("star-soldier-ui.json"));
		Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));
		Image bgImage = new Image(backgroundTexture);

		bgImage.setSize(endStage.getWidth(), endStage.getHeight());

		endStage.addActor(bgImage);

		backToMenuTime -= Gdx.graphics.getDeltaTime();
		if (backToMenuTime <= 0) {
			Gdx.app.exit();
			androidAction.returnMainScreenWithScore(points, name);
		}
		int seconds = Math.max(0, (int) backToMenuTime);

		Table table = new Table();
		table.setFillParent(true);

		Label gameOverLabel = new Label("Game Over!", skin);
		gameOverLabel.setFontScale(6f, 6f);
		table.add(gameOverLabel).center().padBottom(20).row();

		// Display score here
		Label scoreLabel = new Label("Your Score: " + points, skin);
		scoreLabel.setFontScale(4f, 4f);
		table.add(scoreLabel).center().padBottom(20).row();

		timeLabel = new Label("", skin);
		timeLabel.setText("Turn back to Menu in : " + seconds );
		timeLabel.setFontScale(3f, 3f);
		table.add(timeLabel).center().padTop(20).row();

		endStage.addActor(table);
		endStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		endStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}
	@Override
	public void dispose () {
		batch.dispose();
		endStage.dispose();
	}

}
