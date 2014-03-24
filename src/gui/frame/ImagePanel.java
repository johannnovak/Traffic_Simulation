package gui.frame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This class represents the background of the MainFrame.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private transient BufferedImage img;
	/**
	 * Create a new panel background whose path is passed by parameter.
	 * @param path The path for the background for the panel.
	 */
	public ImagePanel(String path)
	{
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(img, 0, 0,img.getWidth(),img.getHeight(), this);
	}
}
