/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

/**
 *
 * @author madelineschmoll
 */
public class PaddleControl extends Observable implements KeyListener {
    private Katch p;
    private final int right, left, fire;

    public PaddleControl(Katch p, int left, int right, int fire) {
        this.p = p;
        this.right = right;
        this.left = left;
        this.fire = fire;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        
        if (keyPressed == left) {
            this.p.toggleLeftPressed();
        }
        if (keyPressed == right) {
            this.p.toggleRightPressed();
        }
        if (keyPressed == fire) {
            this.p.toggleFirePressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        
        if (keyReleased  == left) {
            this.p.unToggleLeftPressed();
        }
        if (keyReleased  == right) {
            this.p.unToggleRightPressed();
        }
        if (keyReleased == fire) {
            this.p.unToggleFirePressed();
        }
    }
}
