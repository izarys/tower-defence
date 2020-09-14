package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {

    //player characteristics
    int lives;

    //tower position
    float posX, posY;
    float width, height;

    //graphics
    Texture towerTexture;
    Texture bubbleTexture;
    TextureRegion[] towerAnimationFrames;
    Animation towerAnimation;

    float elapsedTime;

    public Player(int lives, float width, float height, float posX, float posY, Texture towerTexture, Texture bubbleTexture) {
        this.lives = lives;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.towerTexture = towerTexture;
        this.bubbleTexture = bubbleTexture;

        elapsedTime = 0;

        //creating tower animation
        TextureRegion[][] tmpFrames = TextureRegion.split(towerTexture, 320, 320);
        towerAnimationFrames = new TextureRegion[tmpFrames.length*tmpFrames[0].length];

        for(int i = 0; i < tmpFrames.length*tmpFrames[0].length; i++) {
            towerAnimationFrames[i] = tmpFrames[0][i];
        }

        towerAnimation = new Animation(1f/10f, towerAnimationFrames);
    }

    public void draw(Batch batch) {
        batch.draw((TextureRegion)towerAnimation.getKeyFrame(elapsedTime, true),posX, posY, width, height);

        for(int i = 0; i < lives; i++) {
            batch.draw(bubbleTexture, posX + i*64, posY + height, 64, 64);
        }
    }

    public void update(float delta) {
        elapsedTime += delta;

        if(elapsedTime > 10) {
            elapsedTime = 0;
        }
    }
}
