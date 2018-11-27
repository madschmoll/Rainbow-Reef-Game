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
public class Block extends GameObject {
    private int health;
    private boolean livesPowerUp;
   
    public Block(int x, int y, BufferedImage objImg, boolean livesPu) {
        super(x, y, objImg);
        this.livesPowerUp = livesPu;
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
        if(this.show){
            g.drawImage(objImg, x, y, obs);
        }   
    }
    
    @Override
    public void CollisionEffect(GameObject go) {
       this.show = false; 
     //  System.out.println("COLLISION BLOCK DETECTED");
     if(this.livesPowerUp){
         if(go instanceof Pop){
            ((Pop) go).addLife();
         }
     }
    }
    
    
}
