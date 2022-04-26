package minesweeper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class MinesweeperGame {

	//ATTRIBUTES
	private double mineProbability;
	private long randomSeed;
	private int [] gameGrid;
	private ArrayList<Integer> flags;
	private boolean [] explored;
	private int numberOfMines;
	private int squareLength;
	private int numberOfTilesExplored;
	private long startTime;
	private long stopTime;
	
	public enum GameState { JEU_EN_COURS, PERDU, GAGNE }
	public enum Difficulte { FACILE, INTERMEDIAIRE, EXPERT }
	private GameState gameState;
	
	//Attributions d'une valeur suivant l'état du carreau
	public static final int MINE = -1;
	public static final int FLAGGED = -2;
	public static final int UNEXPLORED = -3;
	
	private static final int FACILE_NUMBER_OF_TILES = 9;
	private static final double FACILE_MINE_PROBABILITY = 0.123456789;
	
	private static final int INTERMEDIAIRE_NUMBER_OF_TILES = 16;
	private static final double INTERMEDIAIRE_MINE_PROBABILITY = 0.142;
	
	private static final int EXPERT_NUMBER_OF_TILES = 30;
	private static final double EXPERT_MINE_PROBABILITY = 0.0928;
	
	private static final String FILE_EXTENSION = ".save";
	
	//METHODES	
	public MinesweeperGame (int numberOfTiles, double mineProbability) {
	
		reset (numberOfTiles, mineProbability);
	
	}
	
	/*
	 * Permet de récupérer l'état du jeu
	 */
	public GameState getGameState () {
	
		return gameState;
	
	}
	
	/**
	*Récupérer le nombre de mines
	 */
	public int getNumberOfMines () {
	
		return numberOfMines;
	
	}
	
	/**
	 * Récupérer le seed(input) utilisé pour générer les carreaux
	 */
	public long getRandomSeed () {
	
		return randomSeed;
	
	}
	
	/*
	 * Récupérer la taille de l'espace de jeu
	 */
	public int getSquareLength () {
	
		return squareLength;
	
	}
	
	/*
	 * Nombre de flags
	 */
	public int getNumberOfFlags () {
	
		return flags.size ();
	
	}
	
	/*
	 * Nombres de mines moins nombre de flags
	 */
	public int getEstimatedNumberOfMines () {
	
		return numberOfMines - flags.size ();
	
	}
	
	/*
	 * Récupérer le nombre de mines adjacentes à un carreau 
	 * MINE si le carreau est une mine ou est flagé
	 * FLAGGED si le carreau est flagé
	 * UNEXPLORED si le carreau n'a pas été ouvert
	 */
	public int getStateOf (int position) {
	
		//vérification
		if (position < 0 || position >= gameGrid.length) {
		
			System.out.println ("Tile Invalid");
			assert (false);
		
		}
	
		if (explored[position]) {
		
			return gameGrid[position];
		
		}
	
		for (int i = 0; i < flags.size (); i++) {
			
			if (flags.get (i) == position) {
				
				return FLAGGED;
				
			}
			
		}
	
		return UNEXPLORED;
	
	}
	
	/*
	 * Récupérer le temps écoulé
	 */
	public float getGameTime () {

		return (System.currentTimeMillis () - startTime) / 1000.0f;

	}	
	
	/*
	 *Timer
	 */
	public float getFinalTime () {

		return (stopTime - startTime) / 1000.0f;

	}	
	
	/*
	 * retourne l'extension du jeu
	 */
	public static String getFileExtension () {
	
		return FILE_EXTENSION;
	
	}
	
	
	/**
	 //Vérifier si le carreau sélectionné est une mine ou pas et mise à jour de gameState
	 */
	public boolean exploreTile (int position) {
	
		//Vérification de la position fournie
		if (position < 0 || position >= gameGrid.length) {
		
			System.out.println ("Tile Invalid");
			assert (false);
		
		}
		
		if (explored[position]) {
		
			return false;
		
		}
		
		explored[position] = true;
		
		if (gameGrid[position] != MINE) {
		
			numberOfTilesExplored++;
		
			if (numberOfMines + numberOfTilesExplored == gameGrid.length) {
			
				gameState = GameState.GAGNE;
				stopTime = System.currentTimeMillis ();
				revealAll ();
			
			} else if (gameGrid[position] == 0) {
			
				exploreAdjacent (position);
			
			}
		
			return false;
		
		}

		gameState = GameState.PERDU;
		stopTime = System.currentTimeMillis ();
		revealAll ();
		return true;
	
	}
	
	/**
	 Flag des carreaux
	 */
	public void flagTile (int position) {
	
		// Vérifier si la valide fournie est invalide
		if (position < 0 || position >= gameGrid.length) {
		
			System.out.println ("Tile Invalid");
			assert (false);
		
		}

		if (explored[position]) {
		
			return;
		
		}
		
		for (int i = 0; i < flags.size (); i++) {
			
			if (flags.get (i) == position) {
				
				flags.remove (i);
				return;
				
			}
			
		}
		
		if (flags.size () < numberOfMines) {
		
			flags.add (position);
		
		}
	
	}
	
	/**
	 * Choix du niveau de difficulté
	 */
	public void newGame (Difficulte difficulty) {
	
		switch (difficulty) {
		
		case FACILE:
			reset (FACILE_NUMBER_OF_TILES, FACILE_MINE_PROBABILITY);
			break;
			
		case INTERMEDIAIRE:
			reset (INTERMEDIAIRE_NUMBER_OF_TILES, INTERMEDIAIRE_MINE_PROBABILITY);
			break;
			
		case EXPERT:
			reset (EXPERT_NUMBER_OF_TILES, EXPERT_MINE_PROBABILITY);
			break;
		
		}
	
	}
	
	/*
	 * Recharge partie
	 */
	public boolean load (File saveFile) {
	
		String filename = saveFile.getName ();
		FileInputStream inStream = null;
		BufferedInputStream bufferedInStream = null;
		ObjectInputStream objectStream = null;
		
		if (filename.indexOf (".") == -1) {
		
			saveFile = new File (filename + "." + FILE_EXTENSION);
			filename = saveFile.getName ();
		
		}
		
		if (saveFile.exists () && saveFile.canRead () && (filename.substring (filename.indexOf ("."))).equals ("." + FILE_EXTENSION)) {
		
			try {
			
				inStream = new FileInputStream (saveFile);
				bufferedInStream = new BufferedInputStream (inStream);
				objectStream = new ObjectInputStream (bufferedInStream);
			
				mineProbability = (Double) (objectStream.readObject ());
				randomSeed = (Long) (objectStream.readObject ());
				numberOfMines = (Integer) (objectStream.readObject ());
				squareLength = (Integer) (objectStream.readObject ());
				numberOfTilesExplored = (Integer) (objectStream.readObject ());
				startTime = System.currentTimeMillis () - (Long) (objectStream.readObject ());
				stopTime = (Long) (objectStream.readObject ());
				gameState = (GameState) (objectStream.readObject ());
				flags = castObjectToArrayList (objectStream.readObject ());
				gameGrid = (int []) (objectStream.readObject ());
				explored = (boolean []) (objectStream.readObject ());
			
			} catch (Exception e) {
			
				return false;
			
			}
		
			return true;
		
		}
		
		return false;
	
	}
	
	/**
	 * Sauvegarde du jeu
	 */
	public boolean save (File saveFile) {
	
		String filename = saveFile.getName ();
		FileOutputStream outStream = null;
		BufferedOutputStream bufferedOutStream = null;
		ObjectOutputStream objectStream = null;
	
	
		if (filename.indexOf (".") == -1) {
		
			saveFile = new File (filename + "." + FILE_EXTENSION);
			filename = saveFile.getName ();
		
		}
	
		if (filename.substring (filename.indexOf (".")).equals ("." + FILE_EXTENSION)) {
		
			try {
		
				outStream = new FileOutputStream (saveFile);
				bufferedOutStream = new BufferedOutputStream (outStream);
				objectStream = new ObjectOutputStream (bufferedOutStream);
			
				objectStream.writeObject (new Double (mineProbability));
				objectStream.writeObject (new Long (randomSeed));
				objectStream.writeObject (new Integer (numberOfMines));
				objectStream.writeObject (new Integer (squareLength));
				objectStream.writeObject (new Integer (numberOfTilesExplored));
				objectStream.writeObject (new Long (System.currentTimeMillis () - startTime));
				objectStream.writeObject (new Long (stopTime));
				objectStream.writeObject (gameState);
				objectStream.writeObject (flags);
				objectStream.writeObject (gameGrid);
				objectStream.writeObject (explored);
				
				bufferedOutStream.flush ();
			
			} catch (Exception e) {
			
				return false;
			
			}
		
			return true;
		
		}
		
		return false;
	
	}
	
	/**
	 * reset du jeu / nouvelle partie
	 */
	public void reset (int numberOfTiles, double mineProbability) {
	
		numberOfMines = 0;
		numberOfTilesExplored = 0;
		gameState = GameState.JEU_EN_COURS;
		stopTime = -1;
		startTime = 0;
	
		if (numberOfTiles < 1) {
		
			numberOfTiles = INTERMEDIAIRE_NUMBER_OF_TILES * INTERMEDIAIRE_NUMBER_OF_TILES;
		
		} else {
		
			numberOfTiles = numberOfTiles * numberOfTiles;
		
		}
		
		if (mineProbability < 0.0) {
		
			this.mineProbability = INTERMEDIAIRE_MINE_PROBABILITY;
		
		} else {
		
			this.mineProbability = mineProbability;
		
		}
		
		
		
		squareLength = (int)Math.sqrt (numberOfTiles);
		
		gameGrid = new int [numberOfTiles];
		explored = new boolean [numberOfTiles];
		propogateGameGrid ();
		
		flags = new ArrayList<Integer> (numberOfMines);
		
		startTime = System.currentTimeMillis ();
	
	}
	
	/*
	 * Remplissage du jeu avec les mines
	 */
	private void propogateGameGrid () {
	
		Random rand = new Random (randomSeed);
	
		for (int i = 0; i < gameGrid.length; i++) {
		
			if (rand.nextDouble () <= mineProbability) {
			
				gameGrid[i] = MINE;
				numberOfMines++;
				
				updateAdjacent (i);
			
			}
		
		}
	
	}
	
	/**
	 * MàJ du compte des mines
	 */
	private void updateAdjacent (int minePosition) {
	
		int adjacentIndex = 0;
	
		//Vérifier la position fournie
		if (minePosition < 0 || minePosition >= gameGrid.length || gameGrid[minePosition] != MINE) {
		
			return;
		
		}
	
		// Ajustement du compte des mines
		for (int j = -1; j <= 1; j++) {
			
			for (int k = -1; k <= 1; k++) {
				
				adjacentIndex = minePosition + (j * squareLength);
				
				// Eviter de compter le premier et le dernier carreau comme adjacent quand la mine est premier ou dernier element
				if ((adjacentIndex % squareLength == 0 && k == -1) || ((adjacentIndex + 1) % squareLength == 0 && k == 1)) {
					
					continue;
					
				}
				
				adjacentIndex += k;
				
				
				if (adjacentIndex >= 0 && adjacentIndex < gameGrid.length && gameGrid[adjacentIndex] != MINE) {
					
					gameGrid[adjacentIndex]++;
					
				}
				
			}
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Integer> castObjectToArrayList (Object obj) {
	
		if (obj instanceof ArrayList) {

			return (ArrayList<Integer>)(obj);
			
		}
		
		return null;
	
	}
	
	/*
		* Voir toutes les cases adjacentes
	*/
	private void exploreAdjacent (int position) {
	
		int adjacentIndex = 0;
	
		//Est-ce que la position fournie est invalide
		if (position < 0 || position >= gameGrid.length) {
		
			return;
		
		}
	
		// Ajustement du compte des cases adjacents
		for (int j = -1; j <= 1; j++) {
			
			for (int k = -1; k <= 1; k++) {
				
				adjacentIndex = position + (j * squareLength);
				
				// Eviter de compter les premier et dernier comme adjacent quand une mine est premier / dernier élément
				if ((adjacentIndex % squareLength == 0 && k == -1) || ((adjacentIndex + 1) % squareLength == 0 && k == 1)) {
					
					continue;
					
				}
				
				adjacentIndex += k;
				
				// Ces conditions doivent être vérifiées à deux reprises lorsque k=1
				if (adjacentIndex >= 0 && adjacentIndex < gameGrid.length) {
					
					exploreTile (adjacentIndex);
					
				}
				
			}
			
		}
	
	}
	
	/**
	 * Montrer le contenu de chaque case lorsque le jeu termine
	 */
	private void revealAll () {
	
		for (int i = 0; i < explored.length; i++) {
		
			explored[i] = true;
		
		}
	
	}
	
}