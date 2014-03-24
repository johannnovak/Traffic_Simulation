package gui.frame;

import gui.button.OptionsButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import code.Difficulty;
import code.SceneManager;

/**
 * This class represents the first Frame displayed on game start. It displays for options: start a new game, load a save, edit settings and quit the game.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class MainFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private ImagePanel menuPan;
	private JLabel menuTitleLab;
	private JButton playB;
	private JButton loadB;
	private OptionsButton optionsB;
	private QuitButton quitB;
	private Difficulty difficulty;
	private JFileChooser jc;	
	/**
	 * Create a new MainFrame (the starting window) who is centered on the screen, not resizable, whose size is 1000*600px and whose title is "Menu principal".
	 */
	public MainFrame()
	{
		this.setName("Menu principal");
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		createMenuPan();
					
		this.setVisible(true);
	}
	/**
	 * Return the chosen difficulty for the game (MEDIUM by default).
	 * @return the chosen difficulty for the game (MEDIUM by default).
	 */
	public Difficulty getDifficulty()
	{
		return this.difficulty;
	}
	/**
	 * Create the Menu.
	 */
	private void createMenuPan()
	{
		menuPan = new ImagePanel("./images/bg/fond_menu_blanc.png");
		menuTitleLab = new JLabel("   Simulation de trafic de trains de véhicules");
		playB = new JButton("Jouer !");
		loadB = new JButton("Charger une partie");
		optionsB = new OptionsButton("Options", this, menuPan);
		quitB = new QuitButton("Quitter");

		menuPan.setLayout(null);
		
		difficulty = Difficulty.MEDIUM;
		menuTitleLab.setBounds(this.getWidth()/2-this.getWidth()/6, this.getHeight()/12, this.getWidth()/3, this.getHeight()/10);
		playB.setBounds(this.getWidth()/2-this.getWidth()/6, this.getHeight()*5/12, this.getWidth()/3, this.getHeight()/10);
		loadB.setBounds(this.getWidth()/2-this.getWidth()/6, this.getHeight()*7/12, this.getWidth()/3, this.getHeight()/10);
		optionsB.setBounds(this.getWidth()/2-this.getWidth()/6, this.getHeight()*9/12, this.getWidth()/3, this.getHeight()/10);
		quitB.setBounds(this.getWidth()*4/5,this.getHeight()*5/7,this.getWidth()/6,this.getHeight()/10);
		
		playB.addActionListener(this);
		optionsB.addActionListener(optionsB);
		loadB.addActionListener(this);
		quitB.addActionListener(quitB);
				
		menuPan.add(playB);
		menuPan.add(loadB);
		menuPan.add(optionsB);
		menuPan.add(quitB);
		
		this.setContentPane(menuPan);
	}
	/**
	 * Ask for an XML file of the map to load.
	 * @return The XML file of the map to load.
	 */
	private File createPlayPan()
	{
		//Default directory is /map/
		File f = new File("./map/");
		//The file's extension has to be ".xml"
		FileFilter ff = new FileFilter() {
			@Override
			public boolean accept(File pathname) 
			{
				if(pathname.getPath().endsWith(".xml"))
					return true;
				return false;
			}
		};
		jc = new JFileChooser();
		jc.setDialogTitle("Choisissez une carte");
		jc.setFileFilter(new FileNameExtensionFilter("Fichiers de type eXtensible Markup Language (.XML)", "xml"));
		jc.setCurrentDirectory(f);
		//Select a random .xml file in the default directory
		jc.setSelectedFile(f.listFiles(ff)[(int)(Math.random()*f.listFiles(ff).length)]);
		//Be sure that the user clicked on OK
		if(jc.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)
			return null;
		return jc.getSelectedFile();
	}
	/**
	 * Ask for a ".save" file of the save to load.
	 * @return ".save" file of the save to load.
	 */
	private File createLoadPan()
	{
		//Default directory is /save/
		File f = new File("./save/");
		//The file's extension has to be ".save"
		FileFilter ff = new FileFilter() {
			@Override
			public boolean accept(File pathname) 
			{
				if(pathname.getPath().endsWith(".save"))
					return true;
				return false;
			}
		};
		jc = new JFileChooser();
		jc.setDialogTitle("Choisissez une sauvegarde");
		jc.setFileFilter(new FileNameExtensionFilter("Fichiers de type .save", "save"));
		jc.setCurrentDirectory(f);
		//Select a random .save file in the default directory
		jc.setSelectedFile(f.listFiles(ff)[(int)(Math.random()*f.listFiles(ff).length)]);
		//Be sure that the user clicked on OK
		if(jc.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)
			return null;
		return jc.getSelectedFile();
	}
	/**
	 * Click on any of menu's buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		File file=null;
		//Click on play button
		if(e.getSource() == playB)
		{
			if ((file = createPlayPan())!=null)
			{
				//Set the menu invisible.
				menuPan.setVisible(false);
				try {
					//Creation of a new game from the file
					new GameFrame(new SceneManager(file, difficulty, false));					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				//Delete the menu frame.
				this.dispose();
			}
		}
		//Click on load button
		else if(e.getSource() == loadB)
		{
			if ((file = createLoadPan())!=null)
			{
				//Set the menu invisible
				menuPan.setVisible(false);
				try {
					//Creation of a new game from the file
					new GameFrame(new SceneManager(file, difficulty, true), file);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				//Delete the menu frame
				this.dispose();
			}
		}
	}
	/**
	 * Set the chosen difficulty for the game.
	 * @param difficulty The chosen difficulty for the game.
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	/**
	 * This class represents the button for exiting the game.
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public class QuitButton extends JButton implements ActionListener{

		private static final long serialVersionUID = 1L;
	
		public QuitButton(String s)
		{
			super(s);
		}
		/**
		 * Exit the game.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);		
		}
	}
}