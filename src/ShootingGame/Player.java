package ShootingGame;

import java.awt.Graphics;

public class Player {
	double x1, width, y1, height;
	
	double speedX = 0, speedY = 0;
	
	double centerX, centerY;
	
	String filename = "main player.png";
	
	Image imgPlayer = loadImage(filename);
	
	
	public Player (double x1, double y1, double x2, double y2){
		this.x1=x1;
		this.y1=y1;
		width=x2;
		height=y2;
		
		centerX = x1 + width/2;
		centerY = y1 + height/2;
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

	public void paintImg(Graphics g) {

		int imgW = imgPlayer.getWidth(null);
		int imgH = imgPlayer.getHeight(null);

		g.drawImage(imgPlayer, (int) x1, (int) y1, (int) (x1 + width), (int) (y1 + height), 0, 0, imgW, imgH, null);
		g.drawRect((int) x1, (int) y1, (int) width, (int) height);
	}
	
	


}
