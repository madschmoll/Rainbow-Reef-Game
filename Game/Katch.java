/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author madelineschmoll
 */
public class Katch extends GameObject implements Observer{
    private boolean leftPressed, rightPressed, firePressed;
    private Pop ball;
    private int speed;
    
    public Katch(int x, int y, BufferedImage objImg) {
        super(x, y, objImg);
        this.speed = 8;
    }

    public void getPop(Pop pop){
        this.ball = pop;
    }
    
    public void toggleRightPressed(){
        this.rightPressed = true;
    }
    
    public void toggleLeftPressed(){
        this.leftPressed = true;
    }
    public void toggleFirePressed(){
        this.firePressed = true;
    }
    
    public void unToggleFirePressed(){
        this.firePressed = false;
    }
    
    public void unToggleRightPressed(){
        this.rightPressed = false;
    }
    
    public void unToggleLeftPressed(){
        this.leftPressed = false;
    }
    
    @Override
    public void update() {
        
        if(this.leftPressed){
            this.x -= speed;
        }
        if(this.rightPressed){
            this.x += speed;
        }
        if(this.firePressed){
            this.ball.setMoving(true);
        }
        
        checkBorder();
    }
    
    private void checkBorder(){
        if(this.x <= 0){
            this.x = 0;
        } else if(this.x >= 500){
            this.x = 500;
        }
    }
    
    public void draw(Graphics2D g, ImageObserver obs) {
       
        super.paintComponent(g);
        g.drawImage(objImg, x, y, obs);
     //   System.out.println("paddle at x: " + x + " ... y: " + y);

    }

    @Override
    public void update(Observable o, Object arg) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void CollisionEffect(GameObject go) {
        // collision with Pop is the only collision that will occur
        // Pop collision is dealth with in Pop class
    }
 
    
}
