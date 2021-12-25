package ShootingGame;

import java.awt.Graphics;

public class Enemy {

	double x1, y1;

	static double width = 25;

	static double height = 25;

	double v1 = 0, v2 = 0;

	double centerX, centerY;

	public Enemy (double Xrange, double Yrange, double x, double y) {
		x1 = (double)(Math.random()*Xrange+x-width);
		y1 = (double)(Math.random()*Yrange+y-height);

		while (x1 > ShootingGame.PANW/2 - 50 && x1 < ShootingGame.PANW/2 + 50) {
			x1 = (double)(Math.random()*Xrange-Math.abs(x)-width);
		}

		while (y1 > ShootingGame.PANH/2 - 50 && y1 < ShootingGame.PANH/2 + 50) {
			y1 = (double)(Math.random()*Yrange-Math.abs(y)-height);
		}
	}

	void getCenterX(){
		centerX = x1 + width/2;
	}

	void getCenterY() {
		centerY = y1 + height/2;
	}

	public void paint(Graphics g) {
		g.fillOval((int)x1, (int)y1, (int)width, (int)height);
	}
}
