package ShootingGame;

import java.awt.Graphics;

public class Bullet {

	double x1, y1;
	
	double v1, v2;
	
	double v1per, v2per;
	
	static double width = 10;
	
	static double height = width;
	
	String filename = "laser bullet.png";

	Image imgPlayerBullet = loadImage(filename);
	
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
	
	Image loadImage(String filename) {
		Image image = null;

		URL imageURL = this.getClass().getResource("/" + filename);

		InputStream inputStr = ShootingGame.class.getClassLoader().getResourceAsStream(filename);

		if (imageURL != null) {
			ImageIcon icon = new ImageIcon(imageURL);
			image = icon.getImage();
		} else {
			JOptionPane.showMessageDialog(null, "An image failed to load: " + filename, "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}

		return image;
	}

	public void paintImg3(Graphics g) {

		int imgW = imgPlayerBullet.getWidth(null);
		int imgH = imgPlayerBullet.getHeight(null);

		g.drawImage(imgPlayerBullet, (int) x1, (int) y1, (int) (x1 + width), (int) (y1 + height), 0, 0, imgW, imgH, null);
		g.drawRect((int) x1, (int) y1, (int) width, (int) height);
	}
	
	
}
