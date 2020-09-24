package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tower {
    //position
    float posX, posY;

    //characteristics
    int width, height;
    enum AnimationMode {
        image, shootingAnimation, penaltyAnimation;
    }
    AnimationMode mode;

    //graphics
    Texture towerTexture;
    TextureRegion[] towerShootingAnimationFrames, towerPenaltyAnimationFrames;
    Animation towerShootingAnimation, towerPenaltyAnimation;

    float elapsedTime;

    public Tower(float posX, float posY, int width, int height,
                 Texture towerTexture, Texture towerPenaltyTexture, Texture towerShootingTexture) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.towerTexture = towerTexture;

        mode = AnimationMode.image;

        //creating animations
        TextureRegion[][] tmpFrames = TextureRegion.split(towerPenaltyTexture, width, height);
        towerPenaltyAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];

        for(int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            towerPenaltyAnimationFrames[i] = tmpFrames[0][i];
        }

        towerPenaltyAnimation = new Animation(1f/8f, towerPenaltyAnimationFrames);

        tmpFrames = TextureRegion.split(towerShootingTexture, width, height);
        towerShootingAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];

        for(int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            towerShootingAnimationFrames[i] = tmpFrames[0][i];
        }

        towerShootingAnimation = new Animation(1f/10f, towerShootingAnimationFrames);
    }

    public void draw(Batch batch) {
        if(mode.equals(AnimationMode.image)) {
            batch.draw(towerTexture, posX, posY, width, height);
        }
        else if(mode.equals(AnimationMode.shootingAnimation)) {
            batch.draw((TextureRegion) towerShootingAnimation.getKeyFrame(elapsedTime, true), posX, posY, width, height);
        }
        else {
            batch.draw((TextureRegion) towerPenaltyAnimation.getKeyFrame(elapsedTime, true), posX, posY, width, height);
        }
    }

    public void update(float delta) {

        if(!mode.equals(AnimationMode.image)) {
            elapsedTime += delta;
        }

        if(elapsedTime > 1) {
            elapsedTime = 0;
            mode = AnimationMode.image;
        }

    }

    public void setAnimationMode(boolean word) {
        if(word) {
            mode = AnimationMode.shootingAnimation;
        }
        else {
            mode = AnimationMode.penaltyAnimation;
        }
    }
}
