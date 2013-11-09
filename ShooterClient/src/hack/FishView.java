package hack;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

//GUI

public class FishView extends JFrame implements Runnable{
	protected static final int WIDTH = 903;
	protected static final int HEIGHT = 478;
	int bullets = 5;
	Graphics graphics;
	JPanel canvas;
	BufferedImage image;
	public FishView(){
		Thread viewthread = new Thread(this, "View Thread");
		viewthread.start();
	}
	//helper to create GUI
	public void run(){
		setLayout(new BorderLayout());
		//background label
		canvas = new JPanel();
		canvas.setDoubleBuffered(true);
		add(canvas);
		//JFrame stuff
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		drawBackground();
		dipTarget();
	}
<<<<<<< HEAD
	//need this so that super isn't called
=======
	
	
	
	
	
	//casts line animation
	// -1 < xpos < 5; -1 < ypos < 3 for (x,y) grid
	public void castLine(int xpos, int ypos){
		Point initialpoint = new Point(WIDTH/6 + (xpos * WIDTH/6), (ypos * HEIGHT/4) - HEIGHT/4 - HEIGHT/4);
		Graphics2D graphics = (Graphics2D) background.getGraphics();
	    final BasicStroke dashed =
	        new BasicStroke(5.0f);
	    graphics.setStroke(dashed);
		for(int i = 0; i < 20; i++){
			graphics.drawArc((int) initialpoint.getX(), (int) initialpoint.getY(), 
				WIDTH/9, HEIGHT, 0, i * 10);
			try {
				Thread.sleep(20L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
>>>>>>> 7636eb780e20f366841c60dbf618a5b7fbe79fc8
	public void paint(Graphics g){
	}
	
	public void drawBackground(){
		if (graphics == null){
			graphics = canvas.getGraphics();
		}
		try {
			File file = new File("src//res//duck-hunt.jpg");
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		graphics.drawImage(image, 0, 0, this);
		graphics.setColor(Color.BLACK);
		for(int i = 0; i < bullets; i++){
			graphics.fillOval(60 + i*30, 10, 10, 10);
		}
		update(graphics);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
	
	public void drawTarget(int x, int y){
		int diameter = WIDTH/50;
		graphics.setColor(Color.RED);
		graphics.fillOval(x, y, 3*diameter, 3*diameter);
		graphics.setColor(Color.WHITE);
		graphics.fillOval(x + diameter/2, y + diameter/2, 2*diameter, 2*diameter);
		graphics.setColor(Color.RED);
		graphics.fillOval(x + diameter, y + diameter , diameter, diameter);
		update(graphics);
	}
	
	public void drawCrosshair(int x, int y){
	
	}
	
	public void dipTarget(){
		Random rand;
		rand = new Random();
		int xv = rand.nextInt(10) + 6;
		int yvar = rand.nextInt(4) + 2;
		int xinit = rand.nextInt(500);
		int yinit = rand.nextInt(34) + 50;
		for(int i = xinit; i < WIDTH; i+= xv){
			//sinusoidal movement
			drawTarget(i, yinit + (int)(Math.sin(i/(yvar))*10));
			drawBackground();
		}
		graphics.dispose();
	}
<<<<<<< HEAD
}
=======
	
	
	
}
>>>>>>> 7636eb780e20f366841c60dbf618a5b7fbe79fc8
