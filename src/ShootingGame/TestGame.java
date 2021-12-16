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

	Player rectangle = new Player(250,150, 40, 40);


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
//		frame.addKeyListener(this);
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
			b.x1 -= b.v1;
			b.y1 -= b.v2;
		}
	}

	void moving() {
		rectangle.x1 += rectangle.speedX;
		rectangle.y1 += rectangle.speedY;
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

			g.drawLine((int)(rectangle.x1+rectangle.width/2),(int)(rectangle.y1+rectangle.height/2), mx, my);
			rectangle.paint(g);

			for (int i = 0 ; i < bullets.size() ; i++) {
				Bullet b = bullets.get(i);
				b.paint(g);
			}

		}
	}
	
	double getAngle(double x, double y) {
		
		double angle = 0;
		
		if(x < rectangle.x1 && y < rectangle.y1) {
			angle = Math.atan2(rectangle.y1-y, rectangle.x1-x);
		}
		
		if (x > rectangle.x1 && y < rectangle.y1){
			angle = Math.atan2(rectangle.y1-y, x-rectangle.x1);
		}
		
		if(x < rectangle.x1 && y > rectangle.y1) {
			angle = Math.atan2(y - rectangle.y1, rectangle.x1-x);
		}
		
		if(x > rectangle.x1 && y < rectangle.y1) {
			angle = Math.atan2(rectangle.y1 - y, x - rectangle.x1);
		}
		
		return angle;
		
	}
	
	void setVelocity() {

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
		
		Bullet b = new Bullet(rectangle.x1+rectangle.width/2,rectangle.y1+rectangle.height/2);

		b.v1 = Math.cos(angle)*5;
		b.v2 = Math.sin(angle)*5;
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
			rectangle.speedY = -5;
		}
		if(key == 'a') {
			rectangle.speedX = -5;
		}
		if(key == 's') {
			rectangle.speedY = 5;	
		}
		if(key == 'd') {
			rectangle.speedX = 5;	
		}

		panelGame.repaint();


	}


	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyChar();
		
		if(key == 'w') {
			rectangle.speedY = 0;
		}
		if(key == 'a') {
			rectangle.speedX = 0;
		}
		if(key == 's') {
			rectangle.speedY = 0;
		}
		if(key == 'd') {
			rectangle.speedX = 0;
		}

		panelGame.repaint();

	}



}
