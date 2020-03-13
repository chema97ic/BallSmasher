/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballsmasher;

import java.awt.Rectangle;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author chema
 */
public class Pelota extends JButton{
    
    private int n;
    private int x;
    private int y;
    private int speed;
    
    public Pelota(int n, int x, int y, int speed) {
    this.n=n;
    this.x=x;
    this.y=y;
    this.speed=speed;
    }

    public int getN() {
        return n;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setN(int n) {
        this.n = n;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void setSpeed() {
        this.speed=speed;
    }
    
    public Rectangle getRectangle(){
        Rectangle r=new Rectangle(getX(),getY(),getWidth(),getHeight());
        
        return r;
    }
}
