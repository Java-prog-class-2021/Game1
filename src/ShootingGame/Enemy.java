package ShootingGame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Enemy {

	double x1, y1;

	static double width = 40;

	static double height = 40;

	double v1 = 0, v2 = 0;
	
	
	String filename = "enemy trooper.gif";
	
	Image imgSoilder = loadImage(filename);

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
	
	Image loadImage(String filename) {
		Image image = null;
		
		URL imageURL = this.getClass().getResource("/" + filename); 
		
		InputStream inputStr = ShootingGame.class.getClassLoader().getResourceAsStream(filename);
		
		if (imageURL != null) {
			ImageIcon icon = new ImageIcon(imageURL);				
			image = icon.getImage();
		} else {
			JOptionPane.showMessageDialog(null, "An image failed to load: " + filename , "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		return image;
		
	}
	
	public void paintImg(Graphics g) {
		
		int imgW = imgSoilder.getWidth(null);
		int imgH = imgSoilder.getHeight(null);
		
		g.drawImage(imgSoilder, (int)x1, (int)y1, (int)(x1+width), (int)(y1+height),   
					0, 0, imgW, imgH, null);
	}
}
