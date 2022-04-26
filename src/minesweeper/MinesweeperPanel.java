package minesweeper;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
// import javax.swing.JButton;
import javax.swing.JLabel;
// import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MinesweeperPanel extends JPanel {
	
	/*ATTRIBUTES*/
	private MinesweeperFrame frame;
	private JPanel gridPanel;
	private JLabel mineLable;
	private JLabel statusLabel;
	private int mineCount;
	private String status;
	private Tile [] tiles;
	
	/*METHODES */
	
	public MinesweeperPanel (MinesweeperFrame parent) {
	
		frame = parent;
		tiles = null;
		gridPanel = null;
		
		mineLable = new JLabel ("Mines: "+ Integer.toString (mineCount));
		statusLabel = new JLabel ("Nay metti!", SwingConstants.CENTER);
		
		setLayout (new BorderLayout ());
		
		add (mineLable, BorderLayout.SOUTH);
		add (statusLabel, BorderLayout.NORTH);
		
		reset ();
	
	}
	
	/**
	//  Reset du jeu
	 */
	public void reset () {
	
		int numberOfTiles = 0;
	
		if (tiles != null) {
		
			for (int i = 0; i < numberOfTiles; i++) {
		
				gridPanel.remove (tiles[i]);
		
			}
		
		}
		
		if (gridPanel != null) {
		
			remove (gridPanel);
		
		}
		
		numberOfTiles = (int)Math.pow (frame.getGame ().getSquareLength (), 2.0);
		gridPanel = new JPanel (new GridLayout (frame.getGame ().getSquareLength (), frame.getGame ().getSquareLength ()));
		
		tiles = new Tile [numberOfTiles];
		
		for (int i = 0; i < numberOfTiles; i++) {
		
			tiles[i] = new Tile (i, this);
			tiles[i].addMouseListener (new MouseHandler ());
			gridPanel.add (tiles[i]);
		
		}
		
		mineCount = getGame ().getEstimatedNumberOfMines ();
		
		status = "Rang ngay meuytou";
		
		add (gridPanel, BorderLayout.CENTER);
		
		repaint ();
	
	}
	
	/**
	 * Draw le jeu
	 */
	public void paintComponent (Graphics g) {
	
		super.paintComponent (g);
		
		mineLable.setText ("Nombre de mines: "+ Integer.toString (mineCount));
		statusLabel.setText (status);

	}
	
	/**
	 * Returns the instance of the MinesweeperGame.
	 * @return Returns game.
	 */
	public MinesweeperGame getGame () {
	
		return frame.getGame ();
	
	}
	
	/**
	 * Handles button presses for the game.
	 */
	private class MouseHandler extends MouseAdapter {
	
		/**
		* Handles button presses for the game.
		* @param e the MouseEvent to be handled.
		*/
		public void mousePressed (MouseEvent e) {
		
			if (e.getButton () == MouseEvent.BUTTON3) {
			
				getGame ().flagTile (((Tile)(e.getSource ())).getTileIndex ());
			
			} else if (e.getButton () == MouseEvent.BUTTON1) {
			
				getGame ().exploreTile (((Tile)(e.getSource ())).getTileIndex ());
			
			}
			
			switch (getGame ().getGameState ()) {
		
			case JEU_EN_COURS:
			
				mineCount = getGame ().getEstimatedNumberOfMines ();
				break;
			
			case PERDU:
			
				mineCount = getGame ().getNumberOfMines ();
				status = "Vous avez perdu en " + Float.toString (getGame ().getFinalTime ()) + " secondes!";
				break;
			
			case GAGNE:
		
				mineCount = getGame ().getNumberOfMines ();
				status = "Vous avez gagné en " + Float.toString (getGame ().getFinalTime ()) + " secondes!";
				break;
				
			}
			
			repaint ();
		
		}
	
	}
	
}