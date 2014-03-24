package gui.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import code.Car;
import code.CrossRoad;
import code.Road;

/**
 * This class represents what will be displayed in the game window. It is strongly linked to a camera object and receive at each
 * update of the SceneManager the whole list of actors in the game.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class GamePanel extends JPanel implements MouseMotionListener, MouseListener{

	private static final long serialVersionUID = 1L;
	//List of all actors in the game.
	private List<Displayable> list;
	//Number of crashed vehicles and vehicles which reached their destination or a dead-end.
	private int deaths=0, reached=0;
	private Camera camera;
	//Set the height of the little map displayed on lower right corner of the window.
	private final float CAMERA_HEIGHT = 200.f;
	private GameFrame frame;
	/**
	 * Create a new GamePanel with actors to be displayed, the GameFrame to which it is attached and the camera used to find coordinates of the map to display.
	 * @param disp The set of all actors in the game at this moment.
	 * @param frame The GameFrame to which it is attached.
	 * @param camera The camera used to find coordinates of the map to display.
	 */
	public GamePanel(ArrayList<Displayable> disp, GameFrame frame, Camera camera)
	{
		list = disp;
		this.setLayout(null);
		this.frame = frame;
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.camera = camera;
		this.setVisible(true);
	}
	/**
	 * Update of the display.
	 * @param disps The set of all actors to be displayed in the game at this moment.
	 * @param deaths Number of crashed vehicles.
	 * @param reached Number of vehicles which reached their destination or a dead-end.
	 */
	public void update(List<Displayable> disps, int deaths, int reached){
		list = new ArrayList<>(disps);
		this.reached = reached;
		this.deaths = deaths;
		this.repaint();
	}
	public synchronized void paintComponent(Graphics g)
	{
		BufferedImage img = null;
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		AffineTransform at = new AffineTransform();
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform save = g2d.getTransform();
		if (list==null)
			return;
		for(Displayable disp : list)
		{
			if (disp.getClass()!=Road.class)
			{
				try{
					at = new AffineTransform();
					//Translate the origin to the coordinates of the camera.
					at.translate(-camera.x, -camera.y);
					img = disp.getImage();
					/*On tourne le repère de l'angle de l'image*/
					//Rotate the representative image of the actor of its angle.
					at.rotate(disp.getAngle());
					AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
					g2d.drawImage(img,op, (int)disp.getCoordinates()[0], (int)disp.getCoordinates()[1]);
					//Reset the origin
					g2d.setTransform(save);
				}
				catch(NullPointerException ce){}
			}
			else
			{
				//If it is a Road, fill in its polygon in grey considering the camera.
				at = new AffineTransform();
				at.translate(-camera.x, -camera.y);
				g2d.setColor(Color.GRAY);
				g2d.setTransform(at);
				g2d.fillPolygon(disp);
				g2d.setTransform(save);
			}
		}
		//Creation of the miniature of the map in the lower right corner of the window.
		float ratio = this.getWidth()/((float)this.getHeight());
		//Paint over the existing map.
		g2d.setColor(Color.WHITE);
		g2d.fillRect((int)(this.getWidth()-CAMERA_HEIGHT*ratio), (int)(this.getHeight()-CAMERA_HEIGHT), (int)(CAMERA_HEIGHT*ratio), (int)(CAMERA_HEIGHT));
		for (Displayable disp : list)
		{
			//Display only crossroads and roads.
			if (disp.getClass()==CrossRoad.class)
			{
				//The crossroad is a red point if it is final, blue point if a source and black if none of them.
				if (((CrossRoad)disp).isFinal())
					g2d.setColor(Color.RED);
				else if (((CrossRoad)disp).isSource())
					g2d.setColor(Color.BLUE);
				else
					g2d.setColor(Color.BLACK);
				g2d.fillOval((int)(CAMERA_HEIGHT*ratio/camera.getxMax()*disp.getCoordinates()[0]+this.getWidth()-CAMERA_HEIGHT*ratio), (int)(CAMERA_HEIGHT/camera.getxMax()*disp.getCoordinates()[1] + this.getHeight()-CAMERA_HEIGHT), 10, 10);
			}
			else if (disp.getClass()==Road.class)
				g2d.drawLine((int)(CAMERA_HEIGHT*ratio/camera.getxMax()*((Road)disp).getStartingPoint().x+this.getWidth()-CAMERA_HEIGHT*ratio), (int)(CAMERA_HEIGHT/camera.getxMax()*((Road)disp).getStartingPoint().y + this.getHeight()-CAMERA_HEIGHT), (int)(CAMERA_HEIGHT*ratio/camera.getxMax()*((Road)disp).getEndingPoint().x+this.getWidth()-CAMERA_HEIGHT*ratio), (int)(CAMERA_HEIGHT/camera.getxMax()*((Road)disp).getEndingPoint().y + this.getHeight()-CAMERA_HEIGHT));
			g2d.setColor(Color.BLACK);
		}
		//Draw a circumscribing rectangle for the miniature of the map.
		g2d.setColor(Color.BLACK);
		g2d.drawRect((int)(this.getWidth()-CAMERA_HEIGHT*ratio), (int)(this.getHeight()-CAMERA_HEIGHT), (int)(CAMERA_HEIGHT*ratio), (int)(CAMERA_HEIGHT));
		//Print counters of crashed vehicles and vehicles which reached their destination or a dead-end.
		g2d.drawString("Deaths :"+deaths+" Reached :"+reached, this.getWidth()-CAMERA_HEIGHT*ratio, this.getHeight()-205);
		//Draw a circumscribing rectangle of the map currently shown in the miniature of the whole map.
		g2d.setColor(Color.PINK);
		g2d.drawRect((int)(CAMERA_HEIGHT*ratio/camera.getxMax()*camera.x+this.getWidth()-CAMERA_HEIGHT*ratio), (int)(CAMERA_HEIGHT/camera.getxMax()*camera.y + this.getHeight()-CAMERA_HEIGHT), (int)(CAMERA_HEIGHT*ratio/camera.getxMax()*camera.width), (int)(CAMERA_HEIGHT/camera.getxMax()*camera.height));
	}
	/**
	 * Return the camera.
	 * @return the camera.
	 */
	public Camera getCamera()
	{
		return camera;
	}
	/**
	 * Set the X coordinate of the camera.
	 * @param x The X coordinate to be set on the camera.
	 */
	public void setCameraX(int x){
		camera.setX(x);
	}
	/**
	 * Set the X coordinate of the camera.
	 * @param x The X coordinate to be set on the camera.
	 */
	public void setCameraY(int y){
		camera.setY(y);
	}
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {
		//Update the camera if the cursor is on border of the window.
		int VERTICAL_MARGIN = (int) (this.getHeight()*0.02), HORIZONTAL_MARGIN = (int) (this.getWidth()*0.02);
		if (e.getX()<=HORIZONTAL_MARGIN)
			camera.setX(camera.x-20);
		else if (e.getX()>=this.getWidth()-HORIZONTAL_MARGIN)
			camera.setX(camera.x+20);
		if (e.getY()<=VERTICAL_MARGIN)
			camera.setY(camera.y-20);
		else if (e.getY()>=this.getHeight()-4*VERTICAL_MARGIN)
			camera.setY(camera.y+20);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent e) {
		Car c;
		float ratio = ((float)this.getWidth())/this.getHeight();
		//If the click is in the miniature of the miniature map, we update the camera.
		//TODO Currently a bugged version
		if (e.getX()>=this.getWidth()-(200*ratio) && e.getY()>=this.getHeight()-200)
		{
			float x = ((e.getX()-camera.width+200.f*ratio)*camera.getxMax()/200.f/ratio)+50, y=((e.getY()-camera.height+200)*camera.getyMax()/200.f)+300;
			camera.setX((int)x);
			camera.setY((int)y);
			return;
		}
		//Retrieve the clicked vehicle (if there exists) or perform action on click (switch traffic lights).
		Point pt =new Point(e.getX()+camera.x, e.getY()+camera.y); 
		if ((c=frame.getScene().clickOn(pt))!=null)
		{
			//A vehicle is clicked, pause the game and display possibilities for the user.
			frame.getScene().pause();
			Object[] options;
			if (c.getTrain().hasOnlyOneCar())
			{
				options = new Object[2];
				options[0]= "Accélérer";
				options[1] = "Ralentir";
			}
			else
			{
				options = new Object[3];
				options[0]= "Accélérer";
				options[1] = "Ralentir";
				options[2] = "Quitter le train";
			}
			//Display next intermediate and its destination.
			int choice = JOptionPane.showOptionDialog(this, "Prochaine étape de ce véhicule : "+c.getIntermediate().getName()+"\nDestination :"+c.getDestination().getName()+"\nQue voulez-vous faire ?","MENU", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,options, options[0]);
			switch(choice)
			{
				case 0: c.getTrain().accelerate();break;
				case 1: c.getTrain().decelerate();break;
				case 2: c.forceQuit();break;
			}
			frame.getScene().unpause();
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
