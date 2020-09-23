package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {

    //player characteristics
    int lives;

    enum Mode {
        image, rightWord, wrongWord;
    }

    boolean isBubbleAnimationON;
    Mode mode;

    //tower position
    float posX, posY;
    float width, height;

    //graphics
    Texture towerTexture, towerWrongTexture, towerRightTexture;
    Texture bubbleTexture, bubbleAnimationTexture;
    TextureRegion[] towerWrongAnimationFrames, towerRightAnimationFrames, bubbleAnimationFrames;
    Animation towerWrongAnimation, towerRightAnimation, bubbleAnimation;

    float elapsedTime, elapsedTimeForBubble;

    public Player(int lives, float width, float height, float posX, float posY,
                  Texture towerTexture, Texture towerWrongTexture, Texture towerRightTexture,
                  Texture bubbleTexture, Texture bubbleAnimationTexture) {
        this.lives = lives;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.towerTexture = towerTexture;
        this.towerWrongTexture = towerWrongTexture;
        this.towerRightTexture = towerRightTexture;
        this.bubbleTexture = bubbleTexture;
        this.bubbleAnimationTexture = bubbleAnimationTexture;

        mode = Mode.image;
        isBubbleAnimationON = false;

        elapsedTime = 0;
        elapsedTimeForBubble = 0;

        //creating animations
        TextureRegion[][] tmpFrames = TextureRegion.split(towerWrongTexture, 320, 320);
        towerWrongAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];

        for(int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            towerWrongAnimationFrames[i] = tmpFrames[0][i];
        }

        towerWrongAnimation = new Animation(1f/8f, towerWrongAnimationFrames);

        tmpFrames = TextureRegion.split(towerRightTexture, 320, 320);
        towerRightAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];

        for(int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            towerRightAnimationFrames[i] = tmpFrames[0][i];
        }

        towerRightAnimation = new Animation(1f/10f, towerRightAnimationFrames);

        tmpFrames = TextureRegion.split(bubbleAnimationTexture, 64, 64);
        bubbleAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];

        for(int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            bubbleAnimationFrames[i] = tmpFrames[0][i];
        }

        bubbleAnimation = new Animation(1f/10f, bubbleAnimationFrames);

    }

    public void draw(Batch batch) {

        if(mode.equals(Mode.image)) {
            batch.draw(towerTexture, posX, posY, width, height);
        }
        else if(mode.equals(Mode.rightWord)) {
            batch.draw((TextureRegion) towerRightAnimation.getKeyFrame(elapsedTime, true), posX, posY, width, height);
        }
        else {
            batch.draw((TextureRegion) towerWrongAnimation.getKeyFrame(elapsedTime, true), posX, posY, width, height);
        }

        for(int i = 0; i < lives; i++) {
            batch.draw(bubbleTexture, posX + i*64, posY + height, 64, 64);
        }

        if(isBubbleAnimationON) {
            batch.draw((TextureRegion) bubbleAnimation.getKeyFrame(elapsedTimeForBubble, true), posX + lives*64, posY + height, 64, 64);
        }

    }

    public void update(float delta) {

        elapsedTimeForBubble += delta;

        if(!mode.equals(Mode.image)) {
            elapsedTime += delta;
        }

        if(elapsedTime > 1) {
            elapsedTime = 0;
            mode = Mode.image;
        }

        if(elapsedTimeForBubble > 1) {
            elapsedTimeForBubble = 0;
            isBubbleAnimationON = false;
        }
    }

    public void setAnimationMode(boolean word) {
        if(word) {
            mode = Mode.rightWord;
        }
        else {
            mode = Mode.wrongWord;
        }
    }
}
