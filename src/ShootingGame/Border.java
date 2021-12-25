package ShootingGame;

import java.awt.Graphics;

public class Border {

	double x1, y1;
	
	double width, height;
	
	public Border (double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		width = x2;
		height = y2;
	}
	
	public void paint(Graphics g) {
		g.drawRect((int)x1, (int)y1, (int)width, (int)height);
	}
}
