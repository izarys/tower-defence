package towerdefense.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MenuScreen implements Screen {

    TowerDefenseGame game;
    private final int newGameButtonX = 56;
    private final int exitButtonX = 908;
    private final int resumeButtonX = 488;
    private final int buttonHeight = 80;
    private final int buttonWidth = 364;
    private final int buttonTopY = 656;


    private Texture background;
    private Texture tower;

    BitmapFont buttonsFont;
    BitmapFont titleFont;

    private final int WORLD_WIDTH = TowerDefenseGame.WORLD_WIDTH;
    private final int WORLD_HEIGHT = TowerDefenseGame.WORLD_HEIGHT;


    public MenuScreen(TowerDefenseGame game) {
        this.game = game;

        background = new Texture("background.png");
        tower = new Texture("treetower.png");

        buttonsFont = new BitmapFont();
        buttonsFont.getData().setScale(2f);

        titleFont = new BitmapFont();
        titleFont.getData().setScale(5f);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        game.batch.begin();
        game.batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        game.batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        game.batch.draw(tower, (WORLD_WIDTH - 320) / 2, 120);
        buttonsFont.draw(game.batch, "New Game", 150, 75);
        buttonsFont.draw(game.batch, "Resume", 585, 75);
        buttonsFont.draw(game.batch, "Exit", 1000, 75);
        titleFont.draw(game.batch, "Main Menu", 500, 630);


        if (Gdx.input.getX() >= newGameButtonX && Gdx.input.getX() < newGameButtonX + buttonWidth
                && Gdx.input.getY() > buttonTopY && Gdx.input.getY() < buttonTopY + buttonHeight) {
            //System.out.println(Gdx.input.getY());
            if (Gdx.input.justTouched()) {
                game.gameScreen=new GameScreen(game);
                game.setScreen(game.gameScreen);
            }
        }
        if (Gdx.input.getX() >= exitButtonX && Gdx.input.getX() < exitButtonX + buttonWidth
                && Gdx.input.getY() > buttonTopY && Gdx.input.getY() < buttonTopY + buttonHeight) {
            if (Gdx.input.justTouched()) {
                Gdx.app.exit();
            }
        }
        if (Gdx.input.getX() >= resumeButtonX && Gdx.input.getX() < resumeButtonX + buttonWidth
                && Gdx.input.getY() > buttonTopY && Gdx.input.getY() < buttonTopY + buttonHeight) {
            if (Gdx.input.justTouched()) {
                game.setScreen(game.gameScreen);
            }
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
