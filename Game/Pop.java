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
public class Pop extends GameObject {
    private final int speed, r, damage;
    private int direction, vx, vy;
    private boolean moving, alive;
    private int collisionCount;
    private int lives, score;
    private Katch katch;
    
    public void getKatch(Katch katch){
        this.katch = katch;
    }
    
    public int getLives(){
        return this.lives;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isAlive() {
        return alive;
    }
    
    public void switchDirection(){
        this.direction += 90;
    }
    
    public Pop(int x, int y, BufferedImage objImg) {
        super(x, y, objImg);
        this.damage = 1;
        this.r = 5;
        this.speed = 2;
        direction = 30;
        alive = true;
        collisionCount = 0;
        this.lives = 3;
        this.score = 0;
    }

    @Override
    public void update() {
        
        vx = (int) -Math.round( r * Math.cos( Math.toRadians( direction ) ) );
        vy = (int) -Math.round( r * Math.sin( Math.toRadians( direction ) ) );
       
        if(moving){
            x += vx/ (speed);// + (collisionCount / 100));
            y += vy / (speed);// + (collisionCount / 100));
        }
        else{
            this.x = katch.getX();
            this.y = katch.getY() - 30;
        }
        
        checkBorder();
        collisionCount = 0;
    }

    public void addLife(){
        if(lives < 3)
            this.lives++;
    }
    
    public void checkBorder(){
        if(x >= 600 - 35 || y <= 0 || x < 0){
              switchDirection();
        }
        if(y >= 400){
          if(lives > 0){
            lives--;
            popReset();
          }
          else
            this.alive = false;
        }
        
      //  collisionCount = 0;
    }
    
    public void popReset(){
            this.x = katch.getX();
            this.y = katch.getY() - 30;
            setMoving(false);
            katch.unToggleFirePressed();
           // this.collisionCount = 0;
            this.direction = 30;
    }
    
    public void checkAngle(){
        if(this.direction < 0 || this.direction > 360){
           // System.out.println("ANGLE CHANGED");
           // System.out.println("ANGLE BEFORE --- " + direction);
            this.direction = 20;
        }
        else if(this.direction > 180){
           // System.out.println("ANGLE CHANGED");
           // System.out.println("ANGLE BEFORE --- " + direction);
            this.direction = 150;
        }
    }
    
    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
        super.paintComponent(g);
        g.drawImage(objImg, x, y, obs);
    }

    @Override
    public void CollisionEffect(GameObject go) {
        
        if(go instanceof Katch){
            // increment direction based on what part of katch is collided with
            int offset = this.x - go.getX();
            if(offset < 10){
               this.direction = 20;
            }
            else if(offset >= 10 && offset < 20){
               this.direction = 45;
            }
            else if(offset >= 20 && offset < 30){
               this.direction = 70;
            } 
            else if(offset >= 30 && offset < 40){
               this.direction = 90;
            } 
            else if(offset >= 40 && offset <= 50){
               this.direction = 90;
            }
            else if(offset >= 50 && offset < 60){
               this.direction = 115;
            }
            else if(offset >= 60 && offset < 70){
               this.direction = 140;
            } 
            else if(offset >= 70 && offset < 80){
               this.direction = 165;
            } 
            
            //if(offset > 8){
            //    direction -= offset;
            //}
            //checkAngle();
            
          //  System.out.println("Pop x: " + this.x + " Katch x: " + go.getX());
          //  System.out.println("Offset" + offset);
          //  System.out.println("\n" + "Angle" + direction + " \n\n");
        }
        else if(go instanceof Block){
            this.score += 10;
            this.direction += 180;
        }
        else if(go instanceof BigLeg){
            this.score += 50;
            this.direction += 180;
        }
        else{
            this.direction += 180;
        }
    }
    
}
