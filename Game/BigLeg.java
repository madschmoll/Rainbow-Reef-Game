/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 *
 * @author madelineschmoll
 */
public class BigLeg extends GameObject{
    private int health;
    
    public BigLeg(int x, int y, int health, BufferedImage objImg) {
        super(x, y, objImg);
        this.health = health;
    }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
        if(this.show){
            g.drawImage(objImg, x, y, obs);
        }
    }
    
    private void subtractHealth(){
        if(this.health > 0)
            this.health--;
        else
            this.show = false;
    }

    @Override
    public void CollisionEffect(GameObject go) {
        if(go instanceof Pop){
            this.subtractHealth();
        }
    }

    
}
