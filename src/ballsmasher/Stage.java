/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballsmasher;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author chema
 */
public class Stage extends JPanel{
    private Image img;

    public Stage(Image img) {
        this.img = img;
        Dimension dimension=new Dimension(Window.frameWidth, Window.frameHeight);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.setSize(dimension);
        
    }

    public Stage() {
        super();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        g.drawImage(img, 0, 0, null);
    }
}
