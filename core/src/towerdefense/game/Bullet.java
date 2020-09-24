package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.abs;


public class Bullet {

    //position
    float posX, posY;
    float targetPosX, targetPosY;
    float deltaX, deltaY;

    //characteristics
    float movementSpeed; //world units per second
    int width, height;

    //graphics
    TextureRegion[] bulletAnimationFrames;
    Animation bulletAnimation;

    float elapsedTime;

    public Bullet(float posX, float posY, float targetPosX, float targetPosY, float movementSpeed, Texture bulletTexture) {
        this.posX = posX;
        this.posY = posY;
        this.targetPosX = targetPosX;
        this.targetPosY = targetPosY;

        deltaX = targetPosX - posX;
        deltaY = targetPosY - posY;

        this.movementSpeed = movementSpeed;
        width = 64;
        height = 64;

        elapsedTime = 0;

        //creating animation
        TextureRegion[][] tmpFrames = TextureRegion.split(bulletTexture, width, height);
        bulletAnimationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];

        for(int i = 0; i < tmpFrames.length * tmpFrames[0].length; i++) {
            bulletAnimationFrames[i] = tmpFrames[0][i];
        }

        bulletAnimation = new Animation(1f/10f, bulletAnimationFrames);

    }

    public void draw(Batch batch) {
        batch.draw((TextureRegion) bulletAnimation.getKeyFrame(elapsedTime, true),posX, posY, width, height);
    }

    public boolean isFinished() {
        return ((deltaX < 0 && posX <= targetPosX) || (deltaX > 0 && posX >= targetPosX)) ? true : false;
    }

    public void update(float delta) {
        elapsedTime += delta;

        posX += deltaX * movementSpeed * delta;
        posY += deltaY * movementSpeed * delta;

    }
}
