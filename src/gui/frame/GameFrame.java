package gui.frame;

import interfaces.GameObserver;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import code.SceneManager;
/**
 * This frame represents the window game.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class GameFrame extends JFrame implements GameObserver,KeyListener{

	private static final long serialVersionUID = 1L;
	
	private SceneManager scene;
	private GamePanel gamePanel;
	/**
	 * Creation of a new window game whose scene manager is passed by parameter.
	 * @param sc The scene manager used to communicate between MVC components.
	 */
	public GameFrame(SceneManager sc)
	{
		super("Simulation");
		scene = sc;
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setLocationRelativeTo(null);
		gamePanel= new GamePanel(null, this, new Camera(this.getWidth(), this.getHeight(), (int)scene.getCircumscribingRectangle().x, (int)scene.getCircumscribingRectangle().y));
		this.addKeyListener(this);
		sc.addObserver(this);
		this.setContentPane(gamePanel);
		this.setVisible(true);
	}
	/**
	 * Creation of a new window game whose scene manager is passed by parameter and who is starting from a save..
	 * @param sc The scene manager used to communicate between MVC components.
	 * @param f The file where the save is stored.
	 */
	public GameFrame(SceneManager sc, File f)
	{
		super("Simulation");
		scene = sc;
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setLocationRelativeTo(null);
		
		this.addKeyListener(this);
		sc.addObserver(this);
		gamePanel = new GamePanel(null, this, new Camera(this.getWidth(), this.getHeight(),(int)scene.getCircumscribingRectangle().x, (int)scene.getCircumscribingRectangle().y));
		this.setContentPane(gamePanel);
		this.setVisible(true);
		//Pause the game in order to display the load screen.
		scene.pause();
		JOptionPane.showMessageDialog(this, ("Vous venez de charger "+f.getName()+". Bon jeu !") , "Chargement de partie", JOptionPane.PLAIN_MESSAGE);
		scene.unpause();
	}
	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyReleased(KeyEvent e){
		//Move the camera on pressed keys (arrows).
		if (e.getKeyCode() == 37)
			gamePanel.setCameraX(gamePanel.getCamera().x-20);
		else if (e.getKeyCode() == 39)
			gamePanel.setCameraX(gamePanel.getCamera().x+20);
		else if (e.getKeyCode() == 38)
			gamePanel.setCameraY(gamePanel.getCamera().y-20);
		else if (e.getKeyCode() == 40)
			gamePanel.setCameraY(gamePanel.getCamera().y+20);
	}


	@Override
	public void keyTyped(KeyEvent e) 
	{
		//Display a menu and pause the game if the pressed key is ESCAPE.
		if(e.getKeyChar() == KeyEvent.VK_ESCAPE)
		{
			int choice, endchoice;
			Object[] options = {"Sauver la partie en cours","Retourner au menu principal", "Quitter la simulation","Retourner jouer"};
			scene.pause();
			do{
				endchoice = 0;
				choice = JOptionPane.showOptionDialog(this, "Que voulez-vous faire ?","MENU", JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[3]);
				switch(choice)
				{
					case 0:
						//Save the current game.
						scene.save();
						break;
					case 1:
						//Exit the game without saving and return to the main menu.
						endchoice = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment retourner au menu principal ?", "",JOptionPane.YES_NO_OPTION);
						switch(endchoice)
						{
							case 0: 
								new MainFrame();
								scene.setStop();
								this.dispose();
								break;
						}
						break;
					case 2:
						//Exit the game without saving.
						endchoice = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment quitter ?", "",JOptionPane.YES_NO_OPTION);
						switch(endchoice)
						{
							case 0: 
								System.exit(0);
								break;
						}
						break;
					case 3:
						//Unpause the game.
						scene.unpause();				
						break;
				}
			}while(choice == 0 || (choice == 1 && endchoice == 1) || (choice == 2 && endchoice == 1));
			//Unpause the game.
			scene.unpause();
		}
	}
	@Override
	public void update() {
		//Update the map displayed
		if (gamePanel!=null)
			gamePanel.update(scene.getDisplayables(), scene.getNbOfDeaths(), scene.getNbOfReached());
	}
	/**
	 * Return the scene manager.
	 * @return the scene manager.
	 */
	public SceneManager getScene() {
		return scene;
	}
}