package gui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import code.Difficulty;
/**
 * This class provides the description panel. It allows the user to see differences between difficulties.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class DescriptionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private String noobDesc;
	private String easyDesc;
	private String mediumDesc;
	private String hardDesc;
	private String hardCoreDesc;
	private String noobCharac;
	private String easyCharac;
	private String mediumCharac;
	private String hardCharac;
	private String hardCoreCharac;
	private String noobTitle;
	private String easyTitle;
	private String mediumTitle;
	private String hardTitle;
	private String hardCoreTitle;
	private JTextArea title;
	private JTextArea charac;
	private JTextArea desc;
	/**
	 * Creation of the DescriptionPanel. The difficulty shown by default is MEDIUM.
	 */
	public DescriptionPanel()
	{
		super();
		noobTitle = "                   Difficulté NOOB\n";
		easyTitle = "                    Difficulté EASY\n";
		mediumTitle = "                 Difficulté MEDIUM\n";
		hardTitle = "                   Difficulté HARD\n";
		hardCoreTitle = "              Difficulté HARDCORE\n";
		noobCharac = "Vitesse des véhicules :\ttrès lente\n" +
					 "Débit de véhicules :\ttrès faible\n" +
					 "Nombre de feux tricolore :\tsur chaque route";
		easyCharac = "Vitesse des véhicules :\tlente\n" +
				     "Débit de véhicules :\tfaible\n" +
				 	 "Nombre de feux tricolore :\tbeaucoup";
		mediumCharac = "Vitesse des véhicules :\tnormale\n" +
					   "Débit de véhicules :\tnormal\n" +
				 	   "Nombre de feux tricolore :\tnormal";
		hardCharac = "Vitesse des véhicules :\trapide\n" +
					 "Débit de véhicules :\trelevé\n" +
				 	 "Nombre de feux tricolore :\tpeu";
		hardCoreCharac = "Vitesse des véhicules :\ttrès rapide\n" +
						 "Débit de véhicules :\tinsane\n" +
				 		 "Nombre de feux tricolore :\t1";
		noobDesc = "Mode de jeu le plus facile. Il permet aux novices de découvrir la simulation" +
					" et de s'amuser avec la vie de pauvres gens.";
		easyDesc = "Mode de jeu facile. Ce n'est pas aussi facile que le niveau NOOB, mais laisse" +
				" une plus grand marge de manoeuvres avec peu de véhicules à vitesse faible, " +
				"beaucoup de feux tricolores et un débit relativement bas.";
		mediumDesc = "Mode de jeu normal. A prendre si vous êtes un utilisatur lambda et que vous découvrez " +
					"le jeu sans vouloir vous ennuyez devant ! Les véhicules roulent à vitesse normale, il y" +
					" a des feux tricolores en quantité suffisante et le débit de véhicule est moyen.";
		hardDesc = "Mode de jeu difficile. Si vous connaissez le jeu et voulez une difficulté un peu plus relevée " +
				"c'est le mode idéal. Les véhicules sont plus rapides et en plus grand nombre avec moins de feux tricolores.";
		hardCoreDesc = "Mode de jeu le plus difficile. Si vous aimez les challenges alors ce mode est fait pour vous." +
						" Les véhicules ont le moteur d'une Ferrari, de plus ils se sont passés le message pour tous " +
						"rouler en même temps et bizarrement, il n'y a plus de feux tricolores. Novices s'abstenir.";
		
		title = new JTextArea(mediumTitle);
		title.setFont(new Font("Nimbus Sans L", Font.BOLD,18));
		charac = new JTextArea(mediumCharac);
		desc = new JTextArea(mediumDesc);
		desc.setLineWrap(true);
		desc.setWrapStyleWord(true);
		desc.setEditable(false);
		title.setEditable(false);
		charac.setEditable(false);
		
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createEtchedBorder());
		
		this.setLayout(new BorderLayout());
		this.add(title,BorderLayout.NORTH);
		this.add(desc,BorderLayout.CENTER);
		this.add(charac,BorderLayout.PAGE_END);
	}
	/**
	 * Change the displayed text depending on the difficulty passed by parameter.
	 * @param diff The difficulty which affects the displayed text in the DescriptionPanel.
	 */
	public void changeText(Difficulty diff)
	{
		switch(diff)
		{
			case NOOB:
				title.setText(noobTitle);
				charac.setText(noobCharac);
				desc.setText(noobDesc);
				break;
			case EASY:
				title.setText(easyTitle);
				charac.setText(easyCharac);
				desc.setText(easyDesc);
				break;
			case MEDIUM:
				title.setText(mediumTitle);
				charac.setText(mediumCharac);
				desc.setText(mediumDesc);
				break;
			case HARD:
				title.setText(hardTitle);
				charac.setText(hardCharac);
				desc.setText(hardDesc);
				break;
			case HARDCORE:
				title.setText(hardCoreTitle);
				charac.setText(hardCoreCharac);
				desc.setText(hardCoreDesc);
				break;
		}
	}
}
