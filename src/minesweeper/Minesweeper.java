package minesweeper;

public class Minesweeper {

	//METHODES
	private static void parseArguments () {
	
		int numberOfTiles = 0;
		double mineProbability = -1.0;
		
		new MinesweeperFrame (numberOfTiles, mineProbability);
	
	}
	
	/**
	 * Main program
	 */
	public static void main (String[] args) {
		
		parseArguments ();
	
	}

}