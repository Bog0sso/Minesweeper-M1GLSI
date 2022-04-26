package minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MinesweeperFrame extends JFrame implements ActionListener {

	// ATTRIBUTES
	private MinesweeperGame game;
	private MinesweeperPanel panel;
	
	private JMenuBar menuBar;
	private JMenuItem newMenuItem;
	private JMenuItem loadMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem quitMenuItem;
	private JMenuItem viewStatItem;

	private JFileChooser fileBrowser;
	
	private static final String NEW_ACTION = "new";
	private static final String SAVE_ACTION = "save";
	private static final String LOAD_ACTION = "load";
	private static final String QUIT_ACTION = "quit";
	private static final String VIEW_STAT_ACTION = "view_stat";
	
	// METHODS

	public MinesweeperFrame (int numberOfTiles, double mineProbability) {
	
		setTitle ("Minesweeper");
		setSize (800, 800);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		
		game = new MinesweeperGame (numberOfTiles, mineProbability);
		
		fileBrowser = new JFileChooser ();
		FileNameExtensionFilter filter = new FileNameExtensionFilter ("Minesweeper Game (.save)", game.getFileExtension ());
		fileBrowser.setFileFilter (filter);
		fileBrowser.setFileSelectionMode (JFileChooser.FILES_ONLY );
		
		buildMenuBar ();
		
		panel = new MinesweeperPanel (this);
		add (panel);
		setVisible (true);
	
	}
	
	/**
	 * Retourne une instance du jeu
	 */
	public MinesweeperGame getGame () {
	
		return game;
	
	}
	
	public void actionPerformed (ActionEvent e) {
	
        switch (e.getActionCommand ()) {
		
		case NEW_ACTION:
			newGame ();
			break;
			
		case LOAD_ACTION:
			loadGame ();
			break;
			
		case SAVE_ACTION:
			saveGame ();
			break;
			
		case QUIT_ACTION:
			System.exit (0);
			break;

		case VIEW_STAT_ACTION:
			viewStats();
			break;
		
		}
		
		panel.reset ();
		invalidate ();
		validate ();
		repaint ();
		
	}
	
	/**
	 * Fonction de recharge d'une partie précédemment enregistrée
	 */
	private void loadGame () {
	
		int fileChooserReturnValue = fileBrowser.showOpenDialog (this);
		File saveFile = null;
		String filename = null;
		
		if (fileChooserReturnValue == JFileChooser.APPROVE_OPTION) {
			
			saveFile = fileBrowser.getSelectedFile ();
			filename = saveFile.getName ();
			
			if (filename.indexOf (".") == -1) {
		
				saveFile = new File (filename + "." + MinesweeperGame.getFileExtension ());
				filename = saveFile.getName ();
			
			}
			
			if (!game.load (saveFile)) {
			
				JOptionPane.showMessageDialog (this,"La partie n'a pas pu être chargée.");
			
			}
			
		}
	
	}

	/*
		*Affichage des statistiques sur le jeu
	*/
	private void viewStats () {
	
		//Not implemented feature
	}
	
	/**
	 * Sauvegarde de la partie
	 */
	private void saveGame () {
	
		int fileChooserReturnValue = fileBrowser.showSaveDialog (this);
		File saveFile = null;
		String filename = null;
		
		if (fileChooserReturnValue == JFileChooser.APPROVE_OPTION) {

			saveFile = fileBrowser.getSelectedFile ();
			filename = saveFile.getName ();
			
			if (filename.indexOf (".") == -1) {
		
				saveFile = new File (filename + "." + MinesweeperGame.getFileExtension ());
				filename = saveFile.getName ();
			
			}
		
			if (!game.save (saveFile)) {
				
				JOptionPane.showMessageDialog (this,"Impossible de sauvergarder le fichier.");
				
			}
			
		}
	
	}
	
	/**
	 * Demander le niveau de difficulté au joueur et commencer une nouvelle partie
	 */
	private void newGame () {
	
		String difficulty = (String) (JOptionPane.showInputDialog (this, "Difficulte:", "Nouvelle partie", JOptionPane.QUESTION_MESSAGE, 
																   null, new String [] {"Facile", "Intermediaire", "Expert"}, "Intermediaire"));
		
		if (difficulty != null) {
		
			if (difficulty.equals ("Facile")) {
			
				game.newGame (MinesweeperGame.Difficulte.FACILE);
			
			} else if (difficulty.equals ("Expert")) {
			
				game.newGame (MinesweeperGame.Difficulte.EXPERT);
			
			} else {
			
				game.newGame (MinesweeperGame.Difficulte.INTERMEDIAIRE);
				
			}
			
		} else {
		
			return;
		
		}
	
	}
	
	/**
	 * Affichage du menu et de ces onglets.
	 */
	private void buildMenuBar () {
	
		// Menu
		menuBar = new JMenuBar ();
		
		//Nouvelle partie
		newMenuItem = new JMenuItem ("Nouvelle partie");
		newMenuItem.setActionCommand (NEW_ACTION);
		newMenuItem.addActionListener (this);
		menuBar.add (newMenuItem);
		
		//Charger partie
		loadMenuItem = new JMenuItem ("Charger partie");
		loadMenuItem.setActionCommand (LOAD_ACTION);
		loadMenuItem.addActionListener (this);
		menuBar.add (loadMenuItem);
		
		//Sauvegarder partie
		saveMenuItem = new JMenuItem ("Sauvegarder");
		saveMenuItem.setActionCommand (SAVE_ACTION);
		saveMenuItem.addActionListener (this);
		menuBar.add (saveMenuItem);
		
		// Quit
		quitMenuItem = new JMenuItem ("Quitter");
		quitMenuItem.setActionCommand (QUIT_ACTION);
		quitMenuItem.addActionListener (this);
		menuBar.add (quitMenuItem);
		
		// Statistiques
		// quitMenuItem = new JMenuItem ("Statistiques");
		// quitMenuItem.setActionCommand (VIEW_STAT_ACTION);
		// quitMenuItem.addActionListener (this);
		// menuBar.add (viewStatItem);
		
		// Afficher  le menu
		setJMenuBar (menuBar);
	
	}

}