package ShootingGame;

import java.awt.Graphics;

public class Player {
	double x1, width, y1, height;
	
	double speedX = 0, speedY = 0;
	
	public Player (double x1, double y1, double x2, double y2){
		this.x1=x1;
		this.y1=y1;
		width=x2;
		height=y2;
	}
	
	
	public void paint(Graphics g) {
		g.fillRect((int)x1, (int)y1, (int)width, (int)height);
	}
	
//	void setSpeedX(int n) {
//		speedX = n;
//	}
//	
//	void setSpeedY(int n) {
//		speedY = n;
//	}

	
	


}
