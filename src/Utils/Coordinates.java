/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author paulojeunon
 */
public class Coordinates {
    
    private int _x;
    private int _y;
    
    public Coordinates(int x, int y) {
        _x = x;
        _y = y;
    }
    
    public Coordinates(Coordinates c) {
        _x = c.x();
        _y = c.y();
    }
    
    public void setX(int x) {
        _x = x;
    }
    
    public void setY(int y) {
        _y = y;
    } 
    
    public int x() {
        return _x;
    }
    
    public int y() {
        return _y;
    }
        
}
