package hack;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

public class WavePanel extends javax.swing.JPanel{
	public WavePanel(){
		super();
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		File file = new File("src//res//waves.gif");
		ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
		Image aImage = imageIcon.getImage();
		g.drawImage(aImage, 0, 0, this);
		System.out.println("was here");
		
	}

}
