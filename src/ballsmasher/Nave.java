/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballsmasher;

import java.awt.Rectangle;

/**
 *
 * @author chema
 */
public class Nave{
    int x;
    int y;
    int speed;
    int width;
    int height;

    public Nave(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width=width;
        this.height=height;
    }    

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    
    
    public int getSpeed() {
        return speed;
    }
    
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    
    
}
