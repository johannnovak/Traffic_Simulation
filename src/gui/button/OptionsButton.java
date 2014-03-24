package gui.button;

import gui.frame.DescriptionPanel;
import gui.frame.ImagePanel;
import gui.frame.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import code.Difficulty;
/**
 * This button sets create the options panel and update it considering the button pressed in the options panel.<br>
 * It can set the difficulty and display information on the chosen difficulty.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class OptionsButton extends JButton implements ActionListener{
	private static final long serialVersionUID = 1L;

	private MainFrame mf;
	private ImagePanel optionsPan;
	private JPanel jpan;
	private DescriptionPanel descDifficulty;
	private ButtonGroup difficultySelection;
	private Difficulty temp;
	private NoobButton noobB;
	private EasyButton easyB;
	private MediumButton mediumB;
	private HardButton hardB;
	private HardCoreButton hardCoreB;
	private CreditsButton creditsB;
	private ReturnButton returnMenuOB;
	public OptionsButton(String s, MainFrame mainf, JPanel pan)
	{
		super(s);
		mf = mainf;
		jpan = pan;
	}
	/**
	 * Create the options panel which allows the user to chose between difficulties.
	 */
	public void createOptionsPan()
	{
		optionsPan = new ImagePanel("./images/bg/options_image_feu_desc_voit.png");
		descDifficulty = new DescriptionPanel();
		difficultySelection = new ButtonGroup();
		noobB = new NoobButton("NOOB");
		easyB = new EasyButton("EASY");
		mediumB = new MediumButton("MEDIUM");
		hardB = new HardButton("HARD");
		hardCoreB = new HardCoreButton("HARDCORE	");
		creditsB = new CreditsButton("Crédits");
		returnMenuOB = new ReturnButton("<html>Retourner au<br><center>menu</center></html>");	
		
		optionsPan.setLayout(null);
		descDifficulty.setLocation(mf.getWidth()/2-mf.getWidth()/6, mf.getHeight()*2/5);
		descDifficulty.setSize(mf.getWidth()/3, mf.getHeight()/3);
		descDifficulty.setBounds(mf.getWidth()/2-mf.getWidth()/6, mf.getHeight()*2/5,mf.getWidth()/3, mf.getHeight()/3);
		noobB.setBounds(mf.getWidth()/2-mf.getWidth()/16-mf.getWidth()/6-mf.getWidth()/6, mf.getHeight()/5, mf.getWidth()/8, mf.getHeight()/10);
		easyB.setBounds(mf.getWidth()/2-mf.getWidth()/16-mf.getWidth()/6, mf.getHeight()/5, mf.getWidth()/8, mf.getHeight()/10);
		mediumB.setBounds(mf.getWidth()/2-mf.getWidth()/16, mf.getHeight()/5, mf.getWidth()/8, mf.getHeight()/10);
		mediumB.setSelected(true);
		hardB.setBounds(mf.getWidth()/2+mf.getWidth()/16+mf.getWidth()/6-mf.getWidth()/8, mf.getHeight()/5, mf.getWidth()/8, mf.getHeight()/10);
		hardCoreB.setBounds(mf.getWidth()/2+mf.getWidth()/16+mf.getWidth()/6+mf.getWidth()/6-mf.getWidth()/8, mf.getHeight()/5, mf.getWidth()/8, mf.getHeight()/10);
		creditsB.setBounds(mf.getWidth()/2-mf.getWidth()/16, mf.getHeight()*4/5, mf.getWidth()/8, mf.getHeight()/10);
		returnMenuOB.setBounds(mf.getWidth()*4/5,mf.getHeight()*5/7,mf.getWidth()/6,mf.getHeight()/10);
		
		noobB.addActionListener(noobB);
		noobB.addMouseListener(noobB);
		easyB.addActionListener(easyB);
		easyB.addMouseListener(easyB);
		mediumB.addActionListener(mediumB);
		mediumB.addMouseListener(mediumB);
		hardB.addActionListener(hardB);
		hardB.addMouseListener(hardB);
		hardCoreB.addActionListener(hardCoreB);
		hardCoreB.addMouseListener(hardCoreB);
		creditsB.addActionListener(creditsB);
		returnMenuOB.addActionListener(returnMenuOB);
		
		difficultySelection.add(noobB);
		difficultySelection.add(easyB);
		difficultySelection.add(mediumB);
		difficultySelection.add(hardB);
		difficultySelection.add(hardCoreB);
		optionsPan.add(descDifficulty);
		optionsPan.add(noobB);
		optionsPan.add(easyB);
		optionsPan.add(mediumB);
		optionsPan.add(hardB);
		optionsPan.add(hardCoreB);
		optionsPan.add(creditsB);
		optionsPan.add(returnMenuOB);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		createOptionsPan();
		mf.getContentPane().setVisible(false);
		mf.setContentPane(optionsPan);
	}
	/**
	 * Set the difficulty to NOOB and update the information displayed.
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public class NoobButton extends JToggleButton implements MouseListener, ActionListener{
		private static final long serialVersionUID = 1L;
		public NoobButton(String s)
		{
			super(s);
		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			mf.setDifficulty(Difficulty.NOOB);
			descDifficulty.changeText(Difficulty.NOOB);
			temp = null;
		}
		@Override
		public void mouseClicked(MouseEvent e){}
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			temp = mf.getDifficulty();
			descDifficulty.changeText(Difficulty.NOOB);
		}
		@Override
		public void mouseExited(MouseEvent e) 
		{
			if(temp != null)
			{
				descDifficulty.changeText(temp);
				temp = null;
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}	
	
	}
	/**
	 * Set the difficulty to EASY and update the information displayed.
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public class EasyButton extends JToggleButton implements MouseListener, ActionListener{
		private static final long serialVersionUID = 1L;
		public EasyButton(String s)
		{
			super(s);

		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			mf.setDifficulty(Difficulty.EASY);
			descDifficulty.changeText(Difficulty.EASY);
			temp = null;
		}
		@Override
		public void mouseClicked(MouseEvent e){}
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			temp = mf.getDifficulty();
			descDifficulty.changeText(Difficulty.EASY);
		}
		@Override
		public void mouseExited(MouseEvent e) 
		{
			if(temp != null)
			{
				descDifficulty.changeText(temp);
				temp = null;
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}	
	}
	/**
	 * Set the difficulty to MEDIUM and update the information displayed.
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public class MediumButton extends JToggleButton implements MouseListener, ActionListener{	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public MediumButton(String s)
		{
			super(s);
		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			mf.setDifficulty(Difficulty.MEDIUM);
			descDifficulty.changeText(Difficulty.MEDIUM);
			temp = null;
		}
		@Override
		public void mouseClicked(MouseEvent e){}
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			temp = mf.getDifficulty();
			descDifficulty.changeText(Difficulty.MEDIUM);
		}
		@Override
		public void mouseExited(MouseEvent e) 
		{
			if(temp != null)
			{
				descDifficulty.changeText(temp);
				temp = null;
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}	
	}
	/**
	 * Set the difficulty to HARD and update the information displayed.
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public class HardButton extends JToggleButton implements MouseListener, ActionListener{

		private static final long serialVersionUID = 1L;
		public HardButton(String s)
		{
			super(s);
		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			mf.setDifficulty(Difficulty.HARD);
			descDifficulty.changeText(Difficulty.HARD);
			temp = null;
		}
		@Override
		public void mouseClicked(MouseEvent e){}
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			temp = mf.getDifficulty();
			descDifficulty.changeText(Difficulty.HARD);
		}
		@Override
		public void mouseExited(MouseEvent e) 
		{
			if(temp != null)
			{
				descDifficulty.changeText(temp);
				temp = null;
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}	
	}
	/**
	 * Set the difficulty to HARDCORE and update the information displayed.
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public class HardCoreButton extends JToggleButton implements MouseListener, ActionListener{
		private static final long serialVersionUID = 1L;
		public HardCoreButton(String s)
		{
			super(s);

		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			mf.setDifficulty(Difficulty.HARDCORE);
			descDifficulty.changeText(Difficulty.HARDCORE);
			temp = null;
		}
		@Override
		public void mouseClicked(MouseEvent e){}
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			temp = mf.getDifficulty();
			descDifficulty.changeText(Difficulty.HARDCORE);
		}
		@Override
		public void mouseExited(MouseEvent e) 
		{
			if(temp != null)
			{
				descDifficulty.changeText(temp);
				temp = null;
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}	
	}
	/**
	 * Return to the main menu button.
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public class ReturnButton extends JButton implements ActionListener{
		private static final long serialVersionUID = 1L;
		public ReturnButton(String s)
		{
			super(s);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			mf.getContentPane().setVisible(false);
			jpan.setVisible(true);
			mf.setContentPane(jpan);
		}
	}
	/**
	 * Display a dialog with credits.
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public class CreditsButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;
		public CreditsButton(String title) {
			this.setText(title);
		}		
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			JOptionPane.showMessageDialog(this,
				    "\"Simulation de trafic de trains de véhicules\" est un jeu proposé par\n\nNOVAK Johann et SCHULZ Quentin GI01\n\nen tant que projet final en Automne 2013 de LO43 (Introduction à la POO)", "Crédits", JOptionPane.PLAIN_MESSAGE);	
		}
	}
}