/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;
import java.awt.Rectangle;

/**
 *
 * @author madelineschmoll
 */
public class CollisionDetector {
    private Rectangle r1, r2;
    
    public void checkCollision(GameObject go1, GameObject go2){
        r1 = go1.getRectangle();
        r2 = go2.getRectangle();
        if(r1.intersects(r2)){
            go1.CollisionEffect(go2);
            go2.CollisionEffect(go1);
        }
    }
}
