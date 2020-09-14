package towerdefense.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class GameScreen implements Screen {

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private Texture background;

    //world parameters
    private final int WORLD_WIDTH = 1344;
    private final int WORLD_HEIGHT = 756;

    //time
    private float timeBetweenEnemySpawns = 2f;
    private float enemySpawnTimer = 0;
    private int maxMonsters;

    //game objects
    Player player;
    LinkedList<Enemy> enemyMonstersList;

    //textures
    Texture playerTexture;
    Texture walkingMonsterTexture = new Texture("monster walk animation.png");
    Texture flyingMonsterTexture = new Texture("monster fly animation.png");
    Texture dyingWalkingTexture = new Texture("walking dying.png");

    //text
    Stage stage;
    Skin skin;
    TextField textField;
    BitmapFont monsterFont;
    String typedWord;

    String dictionary[]={"wind","window","wish","with","require","research",
            "resource","respond","allow","almost","alone","along","already",
            "moment","money","monitor","month","mood","parking","paper","orange"};

    Random random = new Random();

    GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //setting up the textures
        background = new Texture("background.png");

        batch = new SpriteBatch();

        //setting up textField
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        typedWord="";

        textField = new TextField("", skin);
        textField.setMessageText("type here ...");
        textField.setPosition(500, 35);
        textField.setSize(340, 50);

        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == 13) { // the typed key is ENTER
                    typedWord=textField.getText();
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

        //setting up game objects
        player = new Player(5, 320, 320, (WORLD_WIDTH - 320) / 2, 120, new Texture("tower fire animation.png"),
                new Texture("hp.png"));
        enemyMonstersList = new LinkedList<>();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        player.update(delta);

        batch.begin();

        batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        player.draw(batch);

        //enemy
        spawnEnemyMonsters(delta);
        //update, move, draw ,delete
        ListIterator<Enemy> enemyListIterator = enemyMonstersList.listIterator();
        while (enemyListIterator.hasNext()) {
            Enemy enemy = enemyListIterator.next();
            enemy.update(delta);
            enemy.draw(batch,monsterFont);
            if(typedWord.equals(enemy.word)) {
                //TODO dying and shooting
               // enemy.drawDyingAnimation(batch);
                enemyListIterator.remove();
                typedWord="";
                continue;
            }
            if (moveMonsterAndCheck(enemy, delta)) { //enemy reaches tower
                //TODO vanishing
                player.lives--;
                enemyListIterator.remove();
                //remove one tower hp
            }
        }

        monsterFont.draw(batch,typedWord,100,100); //FIXME

        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void spawnEnemyMonsters(float delta) {
        enemySpawnTimer += delta;
        if (enemySpawnTimer > timeBetweenEnemySpawns) {
            int typeOfMonster = random.nextInt(6);
            String randomWord=dictionary[random.nextInt(dictionary.length)];
            // 0   1
            // 2   3
            // 4   5
            float positions[][] = {{0, 548}, {1216, 548}, {0, 336}, {1216, 336}, {0, 120}, {1216, 120}}; //x,y
            float directions[][] = {{1, -0.35f}, {-1, -0.35f}, {1, -0.12f}, {-1, -0.12f}, {1, 0}, {-1, 0}}; //x,y

            Texture movingTexture = typeOfMonster < 4 ? flyingMonsterTexture : walkingMonsterTexture;
            Texture dyingTexture = typeOfMonster < 4 ? flyingMonsterTexture : dyingWalkingTexture;
            enemyMonstersList.add(new Enemy(randomWord, positions[typeOfMonster][0], positions[typeOfMonster][1],
                    128, 128, directions[typeOfMonster][0], directions[typeOfMonster][1],
                    30f, movingTexture,dyingTexture));
            enemySpawnTimer -= timeBetweenEnemySpawns;
        }
    }

    private boolean moveMonsterAndCheck(Enemy enemy, float delta) {
        float leftTowerSide = 624;
        float rightTowerSide = 720;
        //enemy.posX=Math.min(enemy.posX+enemy.directionX*enemy.movementSpeed*delta,WORLD_WIDTH-128);
        enemy.posX += enemy.directionX * enemy.movementSpeed * delta;
        enemy.posY += enemy.directionY * enemy.movementSpeed * delta;
        if (enemy.posX + 128 >= leftTowerSide && enemy.posX <= rightTowerSide) return true;
        return false;
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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
