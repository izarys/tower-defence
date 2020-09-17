package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy {
    String word;

    //position bottom left corner
    float posX, posY;
    float width, height;

    //movement
    float directionX;
    float directionY;
    float movementSpeed;

    boolean dying = false;

    //graphics
    //TODO
    TextureRegion[] movingAnimationFrames;
    TextureRegion[] dyingAnimationFrames;
    TextureRegion[] spawningAnimationFrames;
    // TextureRegion[] reachingTowerAnimationFrames;
    Animation movingAnimation;
    Animation dyingAnimation;
    Animation spawningAnimation;

    //time
    float elapsedTime = 0;

    public Enemy(String word, float posX, float posY, float width, float height,
                 float directionX, float directionY, float movementSpeed,
                 Texture movingTexture, Texture dyingTexture, Texture spawningTexture) {
        this.word = word;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.directionX = directionX;
        this.directionY = directionY;
        this.movementSpeed = movementSpeed;

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

        //spawn animation
        tmpFrames = TextureRegion.split(spawningTexture, 128, 128);
        spawningAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];
        for (int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            spawningAnimationFrames[i] = tmpFrames[0][i];
            if (posX > 672) {
                spawningAnimationFrames[i].flip(true, false);
            }
        }
        spawningAnimation = new Animation(1f / 16f, spawningAnimationFrames);
    }

    public void draw(Batch batch, BitmapFont font) {
        if (!dying) {
            if(!spawningAnimation.isAnimationFinished(elapsedTime))
                batch.draw((TextureRegion) spawningAnimation.getKeyFrame(elapsedTime, false), posX, posY, width, height);
            else {
                batch.draw((TextureRegion) movingAnimation.getKeyFrame(elapsedTime, true), posX, posY, width, height);
                font.draw(batch, word, posX + 40, posY + 140);
            }
        } else {
            batch.draw((TextureRegion) dyingAnimation.getKeyFrame(elapsedTime, false), posX, posY, width, height);
        }
    }

    public boolean isDyingAnimationFinished() {
        return dying && dyingAnimation.isAnimationFinished(elapsedTime);
    }

    public boolean isSpawningAnimationFinished() {
        return spawningAnimation.isAnimationFinished(elapsedTime);
    }


    public void update(float delta) {
        elapsedTime += delta;
    }
}
