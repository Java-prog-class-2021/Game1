package ShootingGame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;



public class ShootingGame implements MouseListener, MouseMotionListener, KeyListener{

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ShootingGame(); 
			}
		});
	}

	static final int PANW = 900;
	static final int PANH = 500;

	boolean generateEnemies = true;

	static ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	static ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	static ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();

	Player player = new Player(PANW/2,PANH/2, 40, 40);

	Border border = new Border(-PANW, -PANH, 3*PANW, 3*PANH);

	int health = 100;

	int healthBar;

	int round = 0;

	DrawingPanel panelGame = new DrawingPanel();

	int my, mx;

	JFrame frame;

	Timer timer;

	int time = 0;

	ShootingGame(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panelGame.addMouseListener(this);
		panelGame.addMouseMotionListener(this);
		frame.addKeyListener(this);

		frame.add(panelGame);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		

		timer = new Timer(10, new TimerAL());
		timer.setInitialDelay(70);
		timer.start();
	}


	private class TimerAL implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			time++;

			moving();
			shooting();
			enemyMovement();

			if (enemies.size()==0) {
				generateEnemies = true;
			}

			if(time%100 == 0) {
				enemyBullets();
			}

			enemyShooting();
			generateEnemies();

			panelGame.repaint();
		}
	}

	void shooting() {
		for (int i = 0 ; i < bullets.size() ; i++) {
			Bullet b = bullets.get(i);
			b.x1 += b.v1;
			b.y1 += b.v2;
		}


	}

	void enemyBullets() {
		double angle;
		for(int i = 0 ; i < enemies.size() ; i++) {
			Enemy e = enemies.get(i);
			angle = getEnemyAngle(e.x1 + Enemy.width/2, e.y1 + Enemy.height/2);

			Bullet b = new Bullet(e.x1 + Enemy.width/2, e.y1 + Enemy.height/2);
			setVelocity(e.x1 + Enemy.width/2, e.y1 + Enemy.height/2, angle, b);

			enemyBullets.add(b);
		}
	}

	void enemyShooting () {
		for(int i = 0 ; i < enemyBullets.size() ; i++) {
			Bullet b = enemyBullets.get(i);
			b.x1 -= b.v1;
			b.y1 -= b.v2;
		}
	}

	void moving() {

		if(player.x1 <= border.x1 && player.speedX<0) {
			player.speedX = 0;
		}
		if(player.x1+player.width >= border.x1+border.width	&& player.speedX>0) {
			player.speedX = 0;
		}

		if(player.y1 <= border.y1 && player.speedY<0) {
			player.speedY = 0;
		}
		if(player.y1+player.height >= border.y1+border.height && player.speedY>0) {
			player.speedY = 0;
		}

		border.x1 -= player.speedX;
		border.y1 -= player.speedY;

		for(int i = 0 ; i < enemies.size() ; i++) {
			Enemy e = enemies.get(i);
			e.x1 -= player.speedX;
			e.y1 -= player.speedY;
		}

		for(int i = 0 ; i < bullets.size() ; i++) {
			Bullet b = bullets.get(i);
			b.x1 -= player.speedX;
			b.y1 -= player.speedY;
		}

		for(int i = 0 ; i < enemyBullets.size() ; i++) {
			Bullet b = enemyBullets.get(i);
			b.x1 -= player.speedX;
			b.y1 -= player.speedY;
		}

	}

	void generateEnemies() {
		if(generateEnemies) {
			round++;
			for(int x = 0 ; x < 3+(round-1)*3 ; x++) {
				Enemy e = new Enemy(border.width, border.height, border.x1, border.y1);
				enemies.add(e);
			}

			generateEnemies = false;
		}
	}


	class DrawingPanel extends JPanel {

		DrawingPanel(){
			this.setPreferredSize(new Dimension(PANW, PANH));
			this.setBackground(new Color(210,105,30));
			generateEnemies();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			healthBar = health;

			g.drawLine((int)(player.x1+player.width/2),(int)(player.y1+player.height/2), mx, my);

			player.paint(g);

			for (int i = 0 ; i < bullets.size() ; i++) {
				Bullet b = bullets.get(i);
				b.paint(g);
			}

			g.setFont(new Font ("TimesRoman", Font.BOLD, 24));
			g.drawString("Round: " + round, PANW-(30+24*5), 30);
			g.drawString("Health: " + health, 30, 30);

			if(healthBar<=0) {
				healthBar=0;
			}

			g.setColor(Color.white);
			g2.setStroke(new BasicStroke(4));
			border.paint(g);


			g.setColor(new Color(225-healthBar*2,25+healthBar*2,25));
			g.fillRect(180, 10, (int)(healthBar*1.5), 23);

			g.setColor(Color.orange);
			for(int i = 0 ; i < enemies.size() ; i++) {
				Enemy e = enemies.get(i);
				e.paint(g);
			}

			g.setColor(Color.red);
			for(int x = 0 ; x < enemyBullets.size() ; x++) {
				Bullet b = enemyBullets.get(x);
				b.paint(g);
			}

			//check for game over
			//only for actual game
			//			if(health <= 0) {
			//				timer.stop();
			//				g.setColor(Color.black);
			//				g.setFont(new Font ("TimesRoman", Font.BOLD, 100));
			//				g.drawString("Game Over!", PANW/5, PANH/2);
			//			}
			checkBullets();
			checkEnemies();
			
		}
	}

	void enemyMovement() {
		double angle;
		for(int i = 0 ; i < enemies.size() ; i++) {
			Enemy e = enemies.get(i);
			angle = getEnemyAngle(e.x1, e.y1);
			setVelocity(e.x1, e.y1, angle, e);
			e.x1 -= e.v1;
			e.y1 -= e.v2;

		}
	}

	void checkBullets(){
		for(int i = 0 ; i < enemyBullets.size() ; i++) {
			Bullet b = enemyBullets.get(i);

			if(b.x1>= player.x1 && b.x1 <= player.x1 + player.width && 
					b.y1 >= player.y1 && b.y1 <= player.y1 + player.height	) {
				health -= 5;
				enemyBullets.remove(i);
			}

			if(b.x1 + Bullet.width >= border.x1+border.width || b.x1 <= border.x1 
					|| b.y1 + Bullet.height  > border.y1+border.height || b.y1 < border.y1) {
				enemyBullets.remove(i);

			}
		}
		
		for(int x = 0 ; x < bullets.size() ; x++) {
			Bullet b = bullets.get(x);
			if(b.x1 + Bullet.width>= border.x1+border.width || b.x1 <= border.x1 
					|| b.y1 + Bullet.height > border.y1+border.height || b.y1 < border.y1) {
				bullets.remove(x);

			}
			
		}
	}

	void checkEnemies() {
		for (int i = 0 ; i < bullets.size() ; i++) {
			Bullet b = bullets.get(i);
			for(int x = 0 ; x < enemies.size() ; x++) {
				Enemy e = enemies.get(x);
				if(b.x1 > e.x1 && b.x1 < e.x1 + Enemy.width && 
						b.y1 > e.y1 && b.y1 < e.y1 + Enemy.height	) {
					enemies.remove(x);
					bullets.remove(i);
				}

			}
		}

		for (int i = 0 ; i < enemies.size() ; i++) {
			Enemy e = enemies.get(i);

			e.getCenterX();
			e.getCenterY();

			double n = Math.hypot(player.centerX-e.centerX, player.centerY-e.centerY);

			if(n < player.width/2 + Enemy.width/2) {
				health -= 10;
				enemies.remove(i);
			}

		}

	}


double getEnemyAngle(double x, double y) {
	double angle = 0;

	if(x < player.x1+player.width/2 && y < player.y1+player.height/2) { // upper left
		angle = Math.atan2(player.y1-y, player.x1-x);
	}

	if (x > player.x1+player.width/2 && y > player.y1+player.height/2){ // lower right
		angle = Math.atan2(y-(player.y1+player.height/2 + Bullet.width/2), x-(player.x1+player.width/2 + Bullet.width/2));
	}

	if(x < player.x1+player.width/2 && y > player.y1+player.height/2) { // lower left
		angle = Math.atan2(y - (player.y1+player.height/2 + Bullet.width/2), player.x1 - x);
	}

	if(x > player.x1+player.width/2 && y < player.y1+player.height/2) { // upper right
		angle = Math.atan2(player.y1 - y, x - (player.x1+player.width/2 + Bullet.width/2));
	}

	return angle;
}


double getAngle(double x, double y) {

	double angle = 0;

	if(x < player.x1+player.width/2 && y < player.y1+player.height/2) { // upper left
		angle = Math.atan2((player.y1+player.height/2 + Bullet.width/2)-y, (player.x1+player.width/2 + Bullet.width/2)-x);
	}

	if (x > player.x1+player.width/2 && y > player.y1+player.height/2){ // lower right
		angle = Math.atan2(y-(player.y1+player.height/2 + Bullet.width/2), x-(player.x1+player.width/2 + Bullet.width/2));
	}

	if(x < player.x1+player.width/2 && y > player.y1+player.height/2) { // lower left
		angle = Math.atan2(y - (player.y1+player.height/2 + Bullet.width/2), (player.x1+player.width/2 + Bullet.width/2) - x);
	}

	if(x > player.x1+player.width/2 && y < player.y1+player.height/2) { // upper right
		angle = Math.atan2((player.y1+player.height/2 + Bullet.width/2) - y, x - (player.x1+player.width/2 + Bullet.width/2));
	}

	return angle;

}

void setVelocity(double x, double y, double angle, Enemy b) { //Set Enemy Velocity
	if(x < player.x1+player.width/2 && y < player.y1+player.height/2) { // upper left
		b.v1 = -Math.cos(angle)*2;
		b.v2 = -Math.sin(angle)*2;
	}

	if (x > player.x1+player.width/2 && y > player.y1+player.height/2){ // lower right
		b.v1 = Math.cos(angle)*2;
		b.v2 = Math.sin(angle)*2;
	}

	if(x < player.x1+player.width/2 && y > player.y1+player.height/2) { // lower left
		b.v1 = -Math.cos(angle)*2;
		b.v2 = Math.sin(angle)*2;
	}

	if(x > player.x1+player.width/2 && y < player.y1+player.height/2) { // upper right
		b.v1 = Math.cos(angle)*2;
		b.v2 = -Math.sin(angle)*2;
	}


	for(int i = 0 ; i < enemies.size() ; i++) {
		Enemy e1 = enemies.get(i);
		e1.getCenterX();
		e1.getCenterY();
		for(int p = 0 ; p < enemies.size(); p++) {
			Enemy e2 = enemies.get(p);
			e2.getCenterX();
			e2.getCenterY();

			if(p!=i) {
				//					System.out.println(Math.hypot(e1.centerX - e2.centerX, e1.centerY - e2.centerY));
				if(Math.hypot(e1.centerX - e2.centerX, e1.centerY - e2.centerY) < Enemy.width + 5) {

					//						e1.v1 = e2.v1;
					//						e1.v2 = e2.v2;

					if(e1.v1 > 0 && e2.v1 > 0) {
						if(e1.v1 > e2.v1) {
							e1.v1 = 0;
						}
						else {
							e2.v1 = 0;
						}
					}
					else if(e1.v1 < 0 && e2.v1 < 0) {
						if(e1.v1 < e2.v1) {
							e1.v1 = 0;
						}
						else {
							e2.v1 = 0;
						}	
					}
					else if(e1.v1 < 0 && e2.v1 > 0) {
						e1.v1 = e2.v1;
					}
					else if(e1.v1 > 0 && e2.v1 < 0) {
						e2.v1 = e1.v1;
					}

					if(e1.v2 > 0 && e2.v2 > 0) {
						if(e1.v2 > e2.v2) {
							e1.v2 = 0;
						}
						else {
							e2.v2 = 0;
						}
					}
					else if(e1.v2 < 0 && e2.v2 < 0) {
						if(e1.v2 < e2.v2) {
							e1.v2 = 0;
						}
						else {
							e2.v2 = 0;
						}	
					}
					else if(e1.v2 < 0 && e2.v2 > 0) {
						e1.v2 = e2.v2;
					}
					else if(e1.v2 > 0 && e2.v2 < 0) {
						e2.v2 = e1.v2;
					}

				}


			}
		}

	}
}



void setVelocity(double x, double y, double angle, Bullet b) { // set Bullet Velocity
	if(x < player.x1+player.width/2 && y < player.y1+player.height/2) { // upper left
		b.v1 = -Math.cos(angle)*5;
		b.v2 = -Math.sin(angle)*5;
	}

	if (x > player.x1+player.width/2 && y > player.y1+player.height/2){ // lower right
		b.v1 = Math.cos(angle)*5;
		b.v2 = Math.sin(angle)*5;
	}

	if(x < player.x1+player.width/2 && y > player.y1+player.height/2) { // lower left
		b.v1 = -Math.cos(angle)*5;
		b.v2 = Math.sin(angle)*5;
	}

	if(x > player.x1+player.width/2 && y < player.y1+player.height/2) { // upper right
		b.v1 = Math.cos(angle)*5;
		b.v2 = -Math.sin(angle)*5;
	}
}


/* ACTIONLISTNER*/
@Override
public void mouseDragged(MouseEvent e) {
	// TODO Auto-generated method stub

}


@Override
public void mouseMoved(MouseEvent e) {
	mx = e.getX();
	my = e.getY();
	panelGame.repaint();
}


@Override
public void mouseClicked(MouseEvent e) {
	double x = e.getX();
	double y = e.getY();

	double angle = getAngle(x, y);

	Bullet b = new Bullet(player.x1+player.width/2,player.y1+player.height/2);

	setVelocity(x,y,angle,b);


	bullets.add(b);

}


@Override
public void mousePressed(MouseEvent e) {	}


@Override
public void mouseReleased(MouseEvent e) {	}


@Override
public void mouseEntered(MouseEvent e) {	}


@Override
public void mouseExited(MouseEvent e) {	}


@Override
public void keyTyped(KeyEvent e) {	}



@Override
public void keyPressed(KeyEvent e) {

	int key = e.getKeyChar();
	if(key == 'w') {
		player.speedY = -5;
	}
	if(key == 'a') {
		player.speedX = -5;
	}
	if(key == 's') {
		player.speedY = 5;	
	}
	if(key == 'd') {
		player.speedX = 5;	
	}



}


@Override
public void keyReleased(KeyEvent e) {
	int key = e.getKeyChar();

	if(key == 'w') {
		player.speedY = 0;
	}
	if(key == 'a') {
		player.speedX = 0;
	}
	if(key == 's') {
		player.speedY = 0;
	}
	if(key == 'd') {
		player.speedX = 0;
	}


}



}
