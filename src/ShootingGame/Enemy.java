package ShootingGame;

import java.awt.Graphics;

public class Enemy {
	
	double x1, y1;
	
	double width = 25, height = 25;
	
	double v1 = 0, v2 = 0;
	
	double centerX, centerY;
	
	public Enemy () {
		x1 = (double)(Math.random()*(ShootingGame.PANW-width));
		y1 = (double)(Math.random()*(ShootingGame.PANH-height));
		
		while (x1 > ShootingGame.PANW/2 - 50 && x1 < ShootingGame.PANW/2 + 50) {
			x1 = (double)(Math.random()*(ShootingGame.PANW-width));
		}
		
		while (y1 > ShootingGame.PANH/2 - 50 && y1 < ShootingGame.PANH/2 + 50) {
			y1 = (double)(Math.random()*(ShootingGame.PANH-height));
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
