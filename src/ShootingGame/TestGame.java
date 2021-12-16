package ShootingGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
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



public class TestGame implements MouseListener, MouseMotionListener, KeyListener{

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestGame(); 
			}
		});
	}

	static final int PANW = 500;
	static final int PANH = 300;

	static ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	Player player = new Player(250,150, 40, 40);


	DrawingPanel panelGame = new DrawingPanel();

	int my, mx;

	JFrame frame;

	Timer timer;
	TestGame(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panelGame.addMouseListener(this);
		panelGame.addMouseMotionListener(this);
		panelGame.addKeyListener(this);
		frame.addKeyListener(this);
		frame.add(panelGame);

		//		frame.setSize(PANW, PANH);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		

		timer = new Timer(10, new TimerAL());
		timer.start();
	}


	private class TimerAL implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			shooting();
			moving();
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

	void moving() {
		player.x1 += player.speedX;
		player.y1 += player.speedY;
	}
	
	class DrawingPanel extends JPanel {

		DrawingPanel(){
			this.setPreferredSize(new Dimension(PANW, PANH));
			this.setBackground(Color.white);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g.drawLine((int)(player.x1+player.width/2),(int)(player.y1+player.height/2), mx, my);
			player.paint(g);

			for (int i = 0 ; i < bullets.size() ; i++) {
				Bullet b = bullets.get(i);
				b.paint(g);
			}

		}
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
	
	void setVelocity(double x, double y, double angle, Bullet b) {
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
		//Math.atan2(rectangle.y1-y, rectangle.x1-x)
		
		Bullet b = new Bullet(player.x1+player.width/2,player.y1+player.height/2);

		setVelocity(x,y,angle,b);
	
		
		bullets.add(b);


		panelGame.repaint();
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

		panelGame.repaint();


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

		panelGame.repaint();

	}



}
