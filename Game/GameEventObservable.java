/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.Observable;

/**
 *
 * @author madelineschmoll
 */

/*
    Why make this class if it is just calling super in single implemented method???
    
    --- when in the main method, Observable's setChanged method cannot be 
        accessed directly. To fix this create this method that will allow 
        access to Observable's setChanged method from the main
    
*/

public class GameEventObservable extends Observable {
    // omitted constructor ?
    
    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }
}
