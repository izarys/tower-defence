package towerdefense.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;

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

    //game objects
    Player player;

    //textures
    Texture playerTexture;

    Stage stage;
    Skin skin;
    TextField textField;

    GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //setting up the textures
        background = new Texture("background (1).png");

        batch = new SpriteBatch();

        //setting up textField
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        textField = new TextField("", skin);
        textField.setMessageText("type here ...");
        textField.setPosition(500,35);
        textField.setSize(340,50);

        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == 13) { // the typed key is ENTER
                    System.out.println(textField.getText());
                    textField.setText("");
                }
            }
        });
        stage.addActor(textField);

        //setting up game objects
        player = new Player(5, 320, 320, (WORLD_WIDTH - 320)/2, 120, new Texture("tower fire animation.png"),
                new Texture("hp.png"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        player.update(delta);

        batch.begin();

        batch.draw(background,0,0,WORLD_WIDTH,WORLD_HEIGHT);
        player.draw(batch);

        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
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
