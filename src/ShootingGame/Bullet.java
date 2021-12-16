package ShootingGame;

import java.awt.Graphics;

public class Bullet {

	double x1, y1;
	
	double v1, v2;
	
	static double width = 10;
	
	double height = width;
	
	public Bullet (double x1, double y1) {
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public void paint(Graphics g) {
		g.fillOval((int)x1, (int)y1, (int)width, (int)height);
	}
	
	
	
	
}