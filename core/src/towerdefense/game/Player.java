package towerdefense.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.LinkedList;
import java.util.ListIterator;


public class Player {

    //player characteristics
    int lives, maxLives;
    HealthPoint[] healthPointBar;
    Tower tower;
    LinkedList<Bullet> bulletList;

    Texture bulletTexture;

    public Player(int lives, int towerPosX, int towerPosY,
                  Texture towerTexture, Texture towerPenaltyTexture, Texture towerShootingTexture,
                  Texture healthPointTexture, Texture healthPointAnimationTexture, Texture bulletTexture) {
        this.lives = lives;
        this.maxLives = lives;
        this.bulletTexture = bulletTexture;

        //creating tower
        tower = new Tower(towerPosX, towerPosY, towerTexture.getWidth(), towerTexture.getHeight(), towerTexture, towerPenaltyTexture, towerShootingTexture);

        //creating hp bar
        healthPointBar = new HealthPoint[lives];

        for (int i = 0; i < lives; i++) {
            healthPointBar[i] = new HealthPoint(towerPosX + i * 64, towerPosY + towerTexture.getHeight(), healthPointTexture, healthPointAnimationTexture);
        }

        bulletList = new LinkedList<>();
    }

    public void draw(Batch batch) {

        tower.draw(batch);

        for (int i = 0; i < maxLives; i++) {
            if (!healthPointBar[i].isAnimationFinished()) {
                healthPointBar[i].draw(batch);
            }
        }

        ListIterator<Bullet> bulletListIterator = bulletList.listIterator();
        while(bulletListIterator.hasNext()) {
            Bullet bullet = bulletListIterator.next();
            bullet.draw(batch);
        }
    }

    public void update(float delta) {

        tower.update(delta);

        for (int i = 0; i < maxLives; i++) {
            if (!healthPointBar[i].isAnimationFinished()) {
                healthPointBar[i].update(delta);
            }
        }

        ListIterator<Bullet> bulletListIterator = bulletList.listIterator();
        while(bulletListIterator.hasNext()) {
            Bullet bullet = bulletListIterator.next();
            bullet.update(delta);
            if(bullet.isFinished()) {
                bulletListIterator.remove();
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

    public void shootEnemy(float posX, float posY) {
        bulletList.add(new Bullet(tower.posX + tower.width / 2 - 32, tower.posY + tower.height / 2, posX, posY, 5f, bulletTexture));
    }
}
