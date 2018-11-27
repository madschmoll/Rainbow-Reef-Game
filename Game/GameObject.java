/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;

/**
 *
 * @author madelineschmoll
 */
public abstract class GameObject extends JComponent implements Drawable, Collidable{
    protected final BufferedImage objImg;
    protected int x, y;
    protected boolean show;
    protected Rectangle objRect;

    @Override
    public boolean getShow() {
        return show;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x,y, this.getWidth(), this.getHeight());
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    
    GameObject(int x, int y, BufferedImage objImg){
        this.x = x;
        this.y = y;
        this.objImg = objImg;
        this.show = true;
    }

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public int getWidth(){
        return objImg.getWidth();
    }
    
    @Override
    public int getHeight(){
        return objImg.getHeight();
    }
   
    public abstract void update();
    @Override
    public abstract void draw(Graphics2D g, ImageObserver obs);
    @Override
    public abstract void CollisionEffect(GameObject go);
}
