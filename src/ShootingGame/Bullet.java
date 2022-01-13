package ShootingGame;

import java.awt.Graphics;

public class Bullet {

	double x1, y1;
	
	double v1, v2;
	
	double v1per, v2per;
	
	static double width = 10;
	
	static double height = width;
	
	public Bullet () {
		
	}
	
	public Bullet (double x1, double y1) {
		this.x1 = x1;
		this.y1 = y1;
		
		v1per = 7;
		v2per = 7;
	}
	
	void enemyBullet (double x1, double y1) {
		this.x1 = x1;
		this.y1 = y1;
		
		v1per = 5;
		v2per = 5;
	}
	
	public void paint(Graphics g) {
		g.fillOval((int)x1, (int)y1, (int)width, (int)height);
	}
	
	void shooting() {
		x1 += v1;
		y1 += v2;
	}
	
	
}
