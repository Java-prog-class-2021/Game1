package ShootingGame;

import java.awt.BasicStroke;
import java.awt.Image;
import javax.swing.ImageIcon;
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
import java.io.InputStream;
import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Rectangle;



public class ShootingGame implements MouseListener, MouseMotionListener, KeyListener{

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ShootingGame(); 
			}
		});
	}

	//variables
	static final int PANW = 900;
	static final int PANH = 600;

	//speed for player movement
	final double SPEED = 5;

	//boolean to generate enemies if true
	boolean generateEnemies = true;

	//ArrayList
	static ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	static ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	static ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();

	//Player declaration
	Player player = new Player(PANW/2,PANH/2, 40, 40);

	//Border declaration
	Border border = new Border(-PANW, -PANH, 3*PANW, 3*PANH);

	//Player health
	int health = 100;
	
	//Player healthBar to draw the value
	int healthBar;
	
	//Enemy shooting rate
	int ENEMYSHOOTINGRATE = 100;

	int round = 0;

	DrawingPanel panelGame = new DrawingPanel();

	//variable for mouse index
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
			playerShooting();
			enemyMovement();
			generateEnemies();

			//if enemies are eliminated, set generateEnemies to true to respawn 
			if (enemies.size()==0) {
				generateEnemies = true;
			}

			//the rate for enemy shooting
			//enemy shoot per 100
			if(time%ENEMYSHOOTINGRATE == 0) {
				generateEnemyBullets();
			}

			enemyShooting();


			panelGame.repaint();
		}
	}//end for Timer Class

	//method for player's bullets movement
	void playerShooting() {
		for (int i = 0 ; i < bullets.size() ; i++) {
			Bullet b = bullets.get(i);
			b.x1 += b.v1;
			b.y1 += b.v2;
		}

	}//end for playerShooting method

	//method for generating enemy bullets
	void generateEnemyBullets() {
		double angle;

		//create bullets and their properties(angle, speed)
		for(int i = 0 ; i < enemies.size() ; i++) {
			Enemy e = enemies.get(i);

			e.getCenterX();
			e.getCenterY();

			//check if the enemies are close enough to shoot
			if(Math.hypot(e.centerX - player.centerX, player.centerY - e.centerY) < PANW/2) {			
				angle = getEnemyAngle(e.x1 + Enemy.width/2, e.y1 + Enemy.height/2);

				Bullet b = new Bullet();
				b.enemyBullet(e.x1 + Enemy.width/2, e.y1 + Enemy.height/2);
				setBulletsVelocity(e.x1 + Enemy.width/2, e.y1 + Enemy.height/2, angle, b);

				enemyBullets.add(b);
			}

		}
	}//end for generateEnemyBullets method

	//enemy bullets movement
	void enemyShooting () {
		for(int i = 0 ; i < enemyBullets.size() ; i++) {
			Bullet b = enemyBullets.get(i);
			b.x1 -= b.v1;
			b.y1 -= b.v2;
		}
	}//end for enemyShooting method

	//method for player's movement where player is located in the center
	//therefore this method codes for the movement for objects not the player
	void moving() {

		//if player reaches the border
		//player's speed change to 0
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

		//border moving
		border.x1 -= player.speedX;
		border.y1 -= player.speedY;

		//enemies moving
		for(int i = 0 ; i < enemies.size() ; i++) {
			Enemy e = enemies.get(i);
			e.x1 -= player.speedX;
			e.y1 -= player.speedY;
		}

		//player's bullets moving
		for(int i = 0 ; i < bullets.size() ; i++) {
			Bullet b = bullets.get(i);
			b.x1 -= player.speedX;
			b.y1 -= player.speedY;
		}

		//enemies' bullets moving
		for(int i = 0 ; i < enemyBullets.size() ; i++) {
			Bullet b = enemyBullets.get(i);
			b.x1 -= player.speedX;
			b.y1 -= player.speedY;
		}

	}//end of moving method

	void generateEnemies() {
		if(generateEnemies) {
			round++;
			//as the number of rounds increases, the enemy size will also increase by 2
			for(int x = 0 ; x < 2+(round-1)*2 ; x++) {
				Enemy e = new Enemy();
				e.Enemy1(border.width, border.height, border.x1, border.y1);
				enemies.add(e);
			}

			if(round>=3) {
				for(int x = 0 ; x < 1+(round-3)*2 ; x++) {
					Enemy e = new Enemy();
					e.Enemy2(border.width, border.height, border.x1, border.y1);
					enemies.add(e);
				}
			}

			//set the boolean for generateEnemies to false
			//therefore it will not generate enemies again unless this boolean is turned on again
			generateEnemies = false;
		}
	}//end for generateEnemies method

	//Drawing Panel
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

			//draw player
			player.paint(g);

			for (int i = 0 ; i < bullets.size() ; i++) {
				Bullet b = bullets.get(i);
				b.paint(g);
			}

			//draw information of rounds and health
			g.setFont(new Font ("TimesRoman", Font.BOLD, 24));
			g.drawString("Round: " + round, PANW-(30+24*5), 30);
			g.drawString("Health: " + health, 30, 30);

			//make sure healthBar does not go under 0 since healthBar is used to draw the value
			if(healthBar<=0) {
				healthBar=0;
			}

			//the border of class is white
			g.setColor(Color.white);
			g2.setStroke(new BasicStroke(4));
			border.paint(g);

			//draw healthBar
			g.setColor(new Color(225-healthBar*2,25+healthBar*2,25));
			g.fillRect(180, 10, (int)(healthBar*1.5), 23);

			//draw Enemies
			g.setColor(Color.orange);
			for(int i = 0 ; i < enemies.size() ; i++) {
				Enemy e = enemies.get(i);
				//e.paint(g);

				e.paintImg(g);
			}

			//draw enemies' bullets
			g.setColor(Color.red);
			for(int x = 0 ; x < enemyBullets.size() ; x++) {
				Bullet b = enemyBullets.get(x);
				b.paint(g);
			}

			//check for game over
			//only for actual game
									if(health <= 0) {
										timer.stop();
										g.setColor(Color.black);
										g.setFont(new Font ("TimesRoman", Font.BOLD, 100));
										g.drawString("Game Over!", PANW/5, PANH/2);
									}
			checkBorderBullets();
			damage();
			eliminatedEnemies();

		}
	}//end for drawing panel

	//method for enemy movement
	void enemyMovement() {
		double angle;
		for(int i = 0 ; i < enemies.size() ; i++) {
			Enemy e = enemies.get(i);
			angle = getEnemyAngle(e.x1, e.y1);
			setEnemyVelocity(e.x1, e.y1, angle, e);
			e.x1 -= e.speedX;
			e.y1 -= e.speedY;

		}
	}//end for enemyMovement method

	//method to make sure bullets do not cross the border
	void checkBorderBullets(){
		
		//enemy bullets
		for(int i = 0 ; i < enemyBullets.size() ; i++) {
			Bullet b = enemyBullets.get(i);

			if(b.x1 + Bullet.width >= border.x1+border.width || b.x1 <= border.x1 
					|| b.y1 + Bullet.height  > border.y1+border.height || b.y1 < border.y1) {
				enemyBullets.remove(i);

			}
		}

		//player bullets
		for(int x = 0 ; x < bullets.size() ; x++) {
			Bullet b = bullets.get(x);
			if(b.x1 + Bullet.width>= border.x1+border.width || b.x1 <= border.x1 
					|| b.y1 + Bullet.height > border.y1+border.height || b.y1 < border.y1) {
				bullets.remove(x);

			}

		}
	}//end for checkBorderBullets method

	//method to take out player's health
	void damage() {
		//when enemies' bullets hit player
		//5 health taken out
		for(int i = 0 ; i < enemyBullets.size() ; i++) {
			Bullet b = enemyBullets.get(i);

			if(b.x1>= player.x1 && b.x1 <= player.x1 + player.width && 
					b.y1 >= player.y1 && b.y1 <= player.y1 + player.height	) {
				health -= 5;
				enemyBullets.remove(i);
			}

		}

		//when enemies hit the player
		//10 health taken out
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

	}//end for damage method

	//method for checking whether player's bullets hit enemies
	void eliminatedEnemies() {
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


	}//end for eliminatedEnemies method

	//method to get angle from enemy to player
	//this method is used to set the velocity of enemies and their bullets
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
	}//end for getEnemyAngle method


	//method to get angle for player bullets
	//this is used to set velocity for player's bullets
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

	}//end for getAngle method

	//method to set enemy movement speed
	void setEnemyVelocity(double x, double y, double angle, Enemy e) { //Set Enemy Velocity
		if(x < player.x1+player.width/2 && y < player.y1+player.height/2) { // if the player is located on its upper left
			e.speedX = -Math.cos(angle)*e.v1per;
			e.speedY = -Math.sin(angle)*e.v2per;
		}

		if (x > player.x1+player.width/2 && y > player.y1+player.height/2){ // if the player is located on its lower right
			e.speedX = Math.cos(angle)*e.v1per;
			e.speedY = Math.sin(angle)*e.v2per;
		}

		if(x < player.x1+player.width/2 && y > player.y1+player.height/2) { // if the player is located on its lower left
			e.speedX = -Math.cos(angle)*e.v1per;
			e.speedY = Math.sin(angle)*e.v2per;
		}

		if(x > player.x1+player.width/2 && y < player.y1+player.height/2) { // if the player is located on its upper right
			e.speedX = Math.cos(angle)*e.v1per;
			e.speedY = -Math.sin(angle)*e.v2per;
		}


		//the rest of the part is the prevention of enemies' overlapping
		for(int i = 0 ; i < enemies.size() ; i++) {
			Enemy e1 = enemies.get(i);
			
			Rectangle r1 = new Rectangle((int)e1.x1, (int)e1.y1, (int)Enemy.width, (int)Enemy.height);
			for(int p = 0 ; p < enemies.size(); p++) {
				Enemy e2 = enemies.get(p);
				Rectangle r2 = new Rectangle((int)e2.x1, (int)e2.y1, (int)Enemy.width, (int)Enemy.height);
				

				//prevent from comparing with itself
				if(p!=i) {
					//					System.out.println(Math.hypot(e1.centerX - e2.centerX, e1.centerY - e2.centerY));
					//if the distance between two enemies is smaller than its width plus 5
					if(r1.intersects(r2)) {

						//						e1.v1 = e2.v1;
						//						e1.v2 = e2.v2;

						/***************************************************************
						 * I made this method considering three cases(in terms of 
						 * vertical and horizontal speeds)
						 * 
						 * Case 1: If they face the same direction (both enemies speedX
						 * and speedY are positive)
						 * 		The faster one's velocity(X/Y) will be reduced to 0 to prevent 
						 * Overlapping with the next one
						 * 
						 * Case 2: If they face the same direction (both enemies speedX
						 * and speedY are negative)
						 * 		The one with a greater negative velocity will be reduced 
						 * To 0 to prevent overlapping with the next one
						 * 
						 * Case 3: If the player located in the middle of two enemies
						 * 		Since they may run in the opposite direction, make their
						 * speedX or speedY be the same to prevent overlapping 
						 **************************************************************/
						
						if(e1.speedX > 0 && e2.speedX > 0) {
							if(e1.speedX > e2.speedX) {
								e1.speedX = 0;
							}
							else {
								e2.speedX = 0;
							}
						}
						else if(e1.speedX < 0 && e2.speedX < 0) {
							if(e1.speedX < e2.speedX) {
								e1.speedX = 0;
							}
							else {
								e2.speedX = 0;
							}	
						}
						else if(e1.speedX < 0 && e2.speedX > 0) {
							e1.speedX = e2.speedX;
						}
						else if(e1.speedX > 0 && e2.speedX < 0) {
							e2.speedX = e1.speedX;
						}

						if(e1.speedY > 0 && e2.speedY > 0) {
							if(e1.speedY > e2.speedY) {
								e1.speedY = 0;
							}
							else {
								e2.speedY = 0;
							}
						}
						else if(e1.speedY < 0 && e2.speedY < 0) {
							if(e1.speedY < e2.speedY) {
								e1.speedY = 0;
							}
							else {
								e2.speedY = 0;
							}	
						}
						else if(e1.speedY < 0 && e2.speedY > 0) {
							e1.speedY = e2.speedY;
						}
						else if(e1.speedY > 0 && e2.speedY < 0) {
							e2.speedY = e1.speedY;
						}

					}


				}
			}

		}
	}//end for setEnemyVelocity method

	//method to set bullets velocity (both player's and enemies')
	void setBulletsVelocity(double x, double y, double angle, Bullet b) { // set Bullet Velocity
		if(x < player.x1+player.width/2 && y < player.y1+player.height/2) { // upper left
			b.v1 = -Math.cos(angle)*b.v1per;
			b.v2 = -Math.sin(angle)*b.v2per;
		}

		if (x > player.x1+player.width/2 && y > player.y1+player.height/2){ // lower right
			b.v1 = Math.cos(angle)*b.v1per;
			b.v2 = Math.sin(angle)*b.v2per;
		}

		if(x < player.x1+player.width/2 && y > player.y1+player.height/2) { // lower left
			b.v1 = -Math.cos(angle)*b.v1per;
			b.v2 = Math.sin(angle)*b.v2per;
		}

		if(x > player.x1+player.width/2 && y < player.y1+player.height/2) { // upper right
			b.v1 = Math.cos(angle)*b.v1per;
			b.v2 = -Math.sin(angle)*b.v2per;
		}
	}//end for setBulletsVelocity method


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

		setBulletsVelocity(x,y,angle,b);

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
			player.speedY = -SPEED;
		}
		if(key == 'a') {
			player.speedX = -SPEED;
		}
		if(key == 's') {
			player.speedY = SPEED;	
		}
		if(key == 'd') {
			player.speedX = SPEED;	
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
