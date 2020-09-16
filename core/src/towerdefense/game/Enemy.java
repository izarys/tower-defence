package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy {
    //word to guess
    String word;
    //BitmapFont font = new BitmapFont();

    //position bottom left corner
    float posX, posY;
    float width, height;

    //movement
    float directionX;
    float directionY;
    float movementSpeed;

    boolean dying = false;

    //graphics
    // Texture monsterTexture;
    Texture movingTexture;
    Texture dyingTexture;
    //TODO
    TextureRegion[] movingAnimationFrames;
    TextureRegion[] dyingAnimationFrames;
    //  TextureRegion[] spawningAnimationFrames;
    // TextureRegion[] reachingTowerAnimationFrames;
    Animation movingAnimation;
    Animation dyingAnimation;

    //time
    float elapsedTime = 0;

    public Enemy(String word, float posX, float posY, float width, float height,
                 float directionX, float directionY, float movementSpeed,
                 Texture movingTexture, Texture dyingTexture) {
        this.word = word;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.directionX = directionX;
        this.directionY = directionY;
        this.movementSpeed = movementSpeed;
        this.movingTexture = movingTexture;
        this.dyingTexture = dyingTexture;

        //walk animation
        TextureRegion[][] tmpFrames = TextureRegion.split(movingTexture, 128, 128);
        movingAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];
        for (int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            movingAnimationFrames[i] = tmpFrames[0][i];
            if (posX > 672) {
                movingAnimationFrames[i].flip(true, false);
            }
        }
        movingAnimation = new Animation(1f / 6f, movingAnimationFrames);

        //correct word animation
        tmpFrames = TextureRegion.split(dyingTexture, 128, 128);
        dyingAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];
        for (int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            dyingAnimationFrames[i] = tmpFrames[0][i];
            if (posX > 672) {
                dyingAnimationFrames[i].flip(true, false);
            }
        }
        dyingAnimation = new Animation(1f / 6f, dyingAnimationFrames);
    }

    public void draw(Batch batch, BitmapFont font) {
        //batch.draw(monsterTexture, posX, posY);
        if (!dying) {
            batch.draw((TextureRegion) movingAnimation.getKeyFrame(elapsedTime, true), posX, posY, width, height);
            font.draw(batch, word, posX + 40, posY + 140);
        } else {
            batch.draw((TextureRegion) dyingAnimation.getKeyFrame(elapsedTime, false), posX, posY, width, height);
        }
    }

    public boolean isDyingAnimationFinished() {
        return dying && dyingAnimation.isAnimationFinished(elapsedTime);
    }

    public void drawDyingAnimation(Batch batch) {
        batch.draw((TextureRegion) dyingAnimation.getKeyFrame(elapsedTime, false), posX, posY, width, height);
    }

    public void update(float delta) {
        elapsedTime += delta;
    }
}
