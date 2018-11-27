/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

/**
 *
 * @author madelineschmoll
 */
public abstract interface Drawable {
    public void draw(Graphics2D g, ImageObserver obs);
    public boolean getShow();
}
