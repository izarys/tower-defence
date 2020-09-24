package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HealthPoint {
    //position
    float posX, posY;

    //characteristics
    int width, height;
    boolean isAnimationOn;

    //graphics
    Texture healthPointTexture;
    TextureRegion[] healthPointAnimationFrames;
    Animation healthPointAnimation;

    float elapsedTime;

    public HealthPoint(float posX, float posY, Texture healthPointTexture, Texture healthPointAnimationTexture) {
        this.posX = posX;
        this.posY = posY;
        this.width = 64;
        this.height = 64;
        this.healthPointTexture = healthPointTexture;

        elapsedTime = 0;
        isAnimationOn = false;

        //creating animation
        TextureRegion[][] tmpFrames = TextureRegion.split(healthPointAnimationTexture, width, height);
        healthPointAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];

        for(int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            healthPointAnimationFrames[i] = tmpFrames[0][i];
        }

        healthPointAnimation = new Animation(1f/10f, healthPointAnimationFrames);
    }

    public void draw(Batch batch) {
        if(!isAnimationOn) {
            batch.draw(healthPointTexture, posX, posY, width, height);
        }
        else {
            batch.draw((TextureRegion) healthPointAnimation.getKeyFrame(elapsedTime, false), posX, posY, width, height);
        }
    }

    public void update(float delta) {
        if(isAnimationOn) {
            elapsedTime += delta;
        }
    }

    public boolean isAnimationFinished() {
        return elapsedTime < 1 ? false : true;
    }

    public void startAnimation() {
        isAnimationOn = true;
    }
}
