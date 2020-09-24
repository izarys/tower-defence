package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;


public class Player {

    //player characteristics
    int lives, maxLives;
    HealthPoint[] healthPointBar;
    Tower tower;

    public Player(int lives, int towerPosX, int towerPosY,
                  Texture towerTexture, Texture towerPenaltyTexture, Texture towerShootingTexture,
                  Texture healthPointTexture, Texture healthPointAnimationTexture) {
        this.lives = lives;
        this.maxLives = lives;

        //creating tower
        tower = new Tower(towerPosX, towerPosY, towerTexture.getWidth(), towerTexture.getHeight(), towerTexture, towerPenaltyTexture, towerShootingTexture);


        //creating hp bar
        healthPointBar = new HealthPoint[lives];

        for (int i = 0; i < lives; i++) {
            healthPointBar[i] = new HealthPoint(towerPosX + i * 64, towerPosY + towerTexture.getHeight(), healthPointTexture, healthPointAnimationTexture);
        }


    }

    public void draw(Batch batch) {

        tower.draw(batch);

        for (int i = 0; i < maxLives; i++) {
            if (!healthPointBar[i].isAnimationFinished()) {
                healthPointBar[i].draw(batch);
            }
        }
    }

    public void update(float delta) {

        tower.update(delta);

        for (int i = 0; i < maxLives; i++) {
            if (!healthPointBar[i].isAnimationFinished()) {
                healthPointBar[i].update(delta);
            }
        }
    }

    public void removeHealthPoint() {
        lives--;
        healthPointBar[lives].startAnimation();
    }

    public Tower getTower() {
        return tower;
    }
}
