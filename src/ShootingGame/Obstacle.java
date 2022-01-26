package ShootingGame;

import java.awt.Color;
import java.awt.Graphics;


public class Obstacle {
	double x, y, w, h;    
    
    // default 40;
    public Obstacle (double x, double y , double w, double h){
    	this.x = x;
    	this.y = y;
    	this.w = w;
    	this.h = h;
    }

    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect((int)x, (int)y, (int)w, (int)h);
    }
}