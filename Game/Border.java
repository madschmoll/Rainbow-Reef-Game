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
public class Border extends Block {

    public Border(int x, int y, BufferedImage objImg) {
        super(x, y, objImg,false);
    }

    @Override
    public void CollisionEffect(GameObject go) {
        if(go instanceof Pop){
            ((Pop)go).switchDirection();
        }
    }

    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
        super.paintComponent(g);
        g.drawImage(objImg,x,y,obs);
    }

    @Override
    public boolean getShow() {
        return true;
    }

    @Override
    public void update() {} 
    
}
