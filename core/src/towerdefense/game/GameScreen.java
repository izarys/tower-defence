package towerdefense.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class GameScreen implements Screen {

    TowerDefenseGame game;

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
  //  private SpriteBatch batch;
    private Texture background;

    //world parameters
    private final int WORLD_WIDTH = TowerDefenseGame.WORLD_WIDTH;
    private final int WORLD_HEIGHT = TowerDefenseGame.WORLD_HEIGHT;


    private final int mainMenuButtonX = 908;
    private final int buttonHeight = 80;
    private final int buttonWidth = 364;
    private final int buttonTopY = 656;

    //time
    private final float timeBetweenEnemySpawns = 1.5f;
    private float enemySpawnTimer = 0;

    private int maxMonsters = 5;
    private int monsterCount = maxMonsters;
    private int level = 1;
    private float levelTimeCounter = 0;

    //game objects
    Player player;
    LinkedList<Enemy> enemyMonstersList;

    //textures
    Texture walkingMonsterTexture = new Texture("monster_walk_animation.png");
    Texture flyingMonsterTexture = new Texture("monster_fly_animation.png");
    Texture dyingWalkingTexture = new Texture("walking dying.png");
    Texture dyingFlyingTexture = new Texture("flying_dying.png");
    Texture spawningMonsterTexture=new Texture("spawning_animation.png");

    //text
    Stage stage;
    Skin skin;
    TextField textField;
    BitmapFont monsterFont;
    BitmapFont levelFont;
    BitmapFont buttonsFont;
    String typedWord;

    String longWordsDict[];
    String mediumWordsDict[];

    Random random = new Random();

    GameScreen(TowerDefenseGame game) {

        this.game=game;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);


        //setting up the textures
        background = new Texture("background.png");

       // batch = new SpriteBatch();

        //setting up textField
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        typedWord = "";

        textField = new TextField("", skin);
        textField.setMessageText("type here ...");
        textField.setPosition(500, 35);
        textField.setSize(340, 50);

        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == 13) { // the typed key is ENTER
                    typedWord = textField.getText();
                    //System.out.println(textField.getText());
                    System.out.println(typedWord);
                    textField.setText("");
                }
            }
        });
        stage.addActor(textField);

        monsterFont = new BitmapFont();
        monsterFont.getData().setScale(1.3f);
        // font.setColor(Color.RED);
        levelFont = new BitmapFont();
        levelFont.getData().setScale(3f);

        buttonsFont = new BitmapFont();
        buttonsFont.getData().setScale(2f);

        //setting up dictionaries
        //2k long popular english words
        FileHandle file = Gdx.files.internal("long_words.txt");
        String tmpText = file.readString();
        longWordsDict = tmpText.split("\r\n");
        //2k medium popular english words
        file = Gdx.files.internal("medium_words.txt");
        tmpText = file.readString();
        mediumWordsDict = tmpText.split("\r\n");


        //setting up game objects
        player = new Player(5,  (WORLD_WIDTH - 320) / 2, 120, new Texture("treetower.png"),
                new Texture("tower_wrong_word_animation.png"), new Texture("tower_fire_animation.png"),
                new Texture("hp.png"), new Texture("hp_animation.png"), new Texture("bullet_animation.png"));
        enemyMonstersList = new LinkedList<>();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        player.update(delta);
        player.draw(game.batch);

        if(player.lives == 0) {
            levelFont.draw(game.batch, "GAME OVER", 540, 630);
            ListIterator<Enemy> enemyListIterator = enemyMonstersList.listIterator();
            while (enemyListIterator.hasNext()) {
                Enemy enemy = enemyListIterator.next();
                enemy.draw(game.batch, monsterFont);
                if(enemy.isDyingAnimationFinished()) {
                    enemyListIterator.remove();
                    continue;
                }
            }
        }
        else if (!levelUpdate(delta)) {
            levelTimeCounter = 0;

            //enemy update, move, draw, delete
            spawnEnemyMonster(delta);
            ListIterator<Enemy> enemyListIterator = enemyMonstersList.listIterator();
            while (enemyListIterator.hasNext()) {
                Enemy enemy = enemyListIterator.next();
                enemy.update(delta);
                enemy.draw(game.batch, monsterFont);
                if(enemy.isDyingAnimationFinished()) {
                    enemyListIterator.remove();
                    continue;
                }
                if (typedWord.equals(enemy.word)) {
                    player.getTower().setAnimationMode(true);
                    player.shootEnemy(enemy.posX, enemy.posY);
                    enemy.dying=true;
                    enemy.elapsedTime=0;
                    typedWord = "";
                    continue;
                }
                if (moveMonsterAndCheck(enemy, delta)) { //enemy reaches tower
                    enemyListIterator.remove();
                    player.removeHealthPoint();
                }
            }

            if(!typedWord.equals("")) {
                player.getTower().setAnimationMode(false);
                typedWord = "";
            }

        }

        if (Gdx.input.getX() >= mainMenuButtonX && Gdx.input.getX() < mainMenuButtonX + buttonWidth
                && Gdx.input.getY() > buttonTopY && Gdx.input.getY() < buttonTopY + buttonHeight) {
            if (Gdx.input.justTouched()) {
                game.setScreen(game.menuScreen);
            }
        }
        buttonsFont.draw(game.batch, "Main Menu", 1000, 75);
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private boolean levelUpdate(float delta) {
        if (enemyMonstersList.isEmpty() && monsterCount == maxMonsters) {
            if (levelTimeCounter < 2f) {
                levelFont.draw(game.batch, "LEVEL " + level, 570, 630);
                levelTimeCounter += delta;
                return true;
            }
            level++;
            monsterCount = 0;
            maxMonsters++;
        }
        return false;
    }

    private void spawnEnemyMonster(float delta) {
        enemySpawnTimer += delta;
        if (enemySpawnTimer > timeBetweenEnemySpawns && monsterCount < maxMonsters) {
            monsterCount++;
            int typeOfMonster = random.nextInt(6);
            String randomWord = mediumWordsDict[random.nextInt(mediumWordsDict.length)];
            if (level > 5) { //TODO
                randomWord = longWordsDict[random.nextInt(longWordsDict.length)];
            }
            // 0   1
            // 2   3
            // 4   5
            float positions[][] = {{0, 548}, {1216, 548}, {0, 336}, {1216, 336}, {0, 120}, {1216, 120}}; //x,y
            float directions[][] = {{1, -0.35f}, {-1, -0.35f}, {1, -0.12f}, {-1, -0.12f}, {1, 0}, {-1, 0}}; //x,y

            Texture movingTexture = typeOfMonster < 4 ? flyingMonsterTexture : walkingMonsterTexture;
            Texture dyingTexture = typeOfMonster < 4 ? dyingFlyingTexture : dyingWalkingTexture;
            enemyMonstersList.add(new Enemy(randomWord, positions[typeOfMonster][0], positions[typeOfMonster][1],
                    128, 128, directions[typeOfMonster][0], directions[typeOfMonster][1],
                    60f, movingTexture, dyingTexture, spawningMonsterTexture));
            enemySpawnTimer = 0;
        }
    }

    private boolean moveMonsterAndCheck(Enemy enemy, float delta) {
        if(enemy.dying||!enemy.isSpawningAnimationFinished()) return false;
        float leftTowerSide = 624;
        float rightTowerSide = 720;
        enemy.posX += enemy.directionX * enemy.movementSpeed * delta;
        enemy.posY += enemy.directionY * enemy.movementSpeed * delta;
        if (enemy.posX + 128 >= leftTowerSide && enemy.posX <= rightTowerSide) return true;
        return false;
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}