package mancala;

/**
 * The back-end/model of the mancala program; it handles configuring and
 * adjusting the board after each turn, following the rules of mancala
 * 
 * @author Michael Opheim
 * @version 05/15/2023
 */
public class MancalaModel {

	/** The mancala board */
	private int[][] board;

	/** Player 1's store */
	private int p1Store;

	/** Player 2's store */
	private int p2Store;

	/** A boolean that tracks whether a two-player or AI game is being played */
	private boolean isHumanGame;

	/**
	 * An attribute to keeps track of the current player (note: player 2 corresponds
	 * to row one of the board array and player 1 corresponds to row zero of the
	 * array)
	 */
	private int row = 0;

	/**
	 * Constructor
	 */
	public MancalaModel() {

		// Initialize the mancala board
		board = new int[2][6];
		initializeBoard();
		row = 0;
		isHumanGame = false;
	}

	/**
	 * A method that makes a copy of the current state of the game
	 * 
	 * @param currentBoard The current state of the board
	 * @param player       The current player
	 * @param p1Store      The state of player 1's store
	 * @param p2Store      The state of player 2's store
	 */
	public void copy(int[][] currentBoard, int player, int p1Store, int p2Store) {

		// Copy the board
		for (int row = 0; row < currentBoard.length; row++) {
			for (int col = 0; col < currentBoard[row].length; col++) {
				board[row][col] = currentBoard[row][col];
			}
		}

		// Save the current player and the stores of the players
		row = player;
		this.p1Store = p1Store;
		this.p2Store = p2Store;

	}

	/**
	 * The getter for the mancala board
	 * 
	 * @return board The current state of the mancala board
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * The getter for the current player
	 * 
	 * @return the current player (based on which row is in play)
	 */
	public int getCurrentPlayer() {
		return row;
	}

	/**
	 * The getter for player 1's store
	 * 
	 * @return the current state of player 1's store
	 */
	public int getP1Store() {
		return p1Store;
	}

	/**
	 * The getter for player 2's store
	 * 
	 * @return the current state of player 2's store
	 */
	public int getP2Store() {
		return p2Store;
	}

	/**
	 * The setter for establishing the game mode
	 * 
	 * @param gameType The type of game to be played (an AI game or a two-player
	 *                 game)
	 */
	public void setIsHumanGame(boolean gameType) {
		isHumanGame = gameType;
	}

	/**
	 * The getter for the game mode of a particular game
	 * 
	 * @return the current game mode of a particular game
	 */
	public boolean getIsHumanGame() {
		return isHumanGame;
	}

	/**
	 * A method that initialzies the mancala game board - at the start, each slot on
	 * the board contains six stones
	 */
	public void initializeBoard() {

		p1Store = 0;
		p2Store = 0;

		// Fill each slot in the board with six stones
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				board[row][column] = 6;
			}
		}
	}

	/**
	 * A method that determines whether a player's input is valid
	 * 
	 * @param playerTurn The current player's turn
	 * @param userInput  The number the user entered
	 * @return a boolean signifying whether a player's input is valid (returning
	 *         true if it is, and false otherwise)
	 */
	public boolean isValidInput(int playerTurn, int userInput) {
		if ((playerTurn == 1 && userInput >= 6 && userInput <= 11)
				|| (playerTurn == 0 && userInput >= 0 && userInput <= 5)) {
			return true;
		}
		return false;
	}

	/**
	 * A method that adds up all of the stones on each player's side of the board to
	 * determine if the game is over. It also helps determine the final score for
	 * any particular game
	 * 
	 * @return an array where position 0 contains player 1's row sum and position 1
	 *         is player 2's row sum
	 */
	private int[] rowSums() {
		int p1RowSum = 0;
		int p2RowSum = 0;

		// Add up the values, checking to see if the game is over
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				if (row == 0) {
					p1RowSum += board[row][column];
				} else {
					p2RowSum += board[row][column];
				}
			}
		}

		// Return the values of the rows
		int[] rowSums = new int[2];
		rowSums[0] = p1RowSum;
		rowSums[1] = p2RowSum;
		return rowSums;
	}

	/**
	 * A method that checks if a player is in a winning state (i.e., when one
	 * player's row is empty)
	 * 
	 * The player with the most stones in their store wins
	 * 
	 * @return a boolean stating whether a player has won (it returns true if a
	 *         player has won, and false otherwise)
	 */
	public boolean isWinningState() {
		int[] rowSums = rowSums();

		// If somebody has won, return true
		if (rowSums[0] == 0 || rowSums[1] == 0) {
			return true;
		}

		// Else, return false since nobody has won
		return false;
	}

	/**
	 * A method that tells the players who won once the game is over
	 * 
	 * @return a message stating who won the game or if the game was a tie
	 */
	public String displayWinner() {

		// Check the stores of each player to find who has more 'stones', as whoever
		// does will be the winner
		if (p1Store > p2Store) {
			return "Player 1 wins!";
		} else if (p2Store > p1Store && !isHumanGame) {
			return "AI wins!";
		} else if (p2Store > p1Store) {
			return "Player 2 wins!";
		} else {
			return "The game was a tie!";
		}
	}

	/**
	 * A method that adds any remaining stones to a player's store once the game has
	 * ended
	 */
	public void addRemainingStonesToPlayersStore() {

		// Add any remaining stones to the players' stores
		int[] rowSums = rowSums();
		if (rowSums[0] == 0) {
			p2Store += rowSums[1];
		} else if (rowSums[1] == 0) {
			p1Store += rowSums[0];
		}

		// Empty the board after the stones have been moved
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col] = 0;
			}
		}
	}

	/**
	 * Due to the way that the mancala board is set up and how the game is played
	 * (stones move in a counterclockwise direction), this method helps convert
	 * player move selections into positions in the mancala board's 2D array
	 * 
	 * @param userSelection The hole the player selected (holes 0 to 11)
	 * @return the corresponding position of the player's selection in the 2D array
	 */
	private int playerSelectionToArray(int userSelection) {

		return userSelection % 6;
	}

	/**
	 * A method that moves all the stones from their respective hole and distributes
	 * them one by one in a counterclockwise direction, placing one stone in each
	 * sequential hole (including the current player's store but skipping the
	 * opponent's store)
	 * 
	 * @param playerSelection The hole the player selected (holes 0 to 11)
	 */
	public void moveStones(int playerSelection) {

		// Intialize the player's hole selection and the stones in that position
		int arrayPosition = playerSelectionToArray(playerSelection);
		int stones = board[row][arrayPosition];

		// A boolean that will tell us if the last stone landed in the current player's
		// store
		boolean inCurrentPlayersStore = false;

		// Get the player
		int originalPlayer = row;

		// Set the position that the player chose to -1 (it will initially become zero
		// in the loop below)
		board[row][arrayPosition] = -1;

		// While we still have stones to distribute
		while (stones >= 0) {

			// Add them to the holes sequentially
			board[row][arrayPosition] += 1;

			// If we get to the end of player 2's row...
			if (arrayPosition == 5 && row == 1) {

				// If it is player 2's turn, add the stone to their store
				if (originalPlayer == row && stones >= 1) {
					p2Store += 1;
					stones--;

					// Note whether the stone is the last stone because then player 2 will get
					// another turn
					if (stones == 0) {
						inCurrentPlayersStore = true;
					}
				}

				// Move counterclockwise to the next position on the board
				row = 0;
				arrayPosition = 5;

				// If we get to the end of player 1's row...
			} else if (arrayPosition == 0 && row == 0) {

				// If it is player 1's turn, add the stone to their store
				if (originalPlayer == row && stones >= 1) {
					p1Store += 1;
					stones--;

					// Note whether the stone is the last stone because then player 1 will get
					// another turn
					if (stones == 0) {
						inCurrentPlayersStore = true;
					}
				}

				// Move counterclockwise to the next position on the board
				row = 1;
				arrayPosition = 0;

				// Otherwise, continue moving counterclockwise to distribute stones to holes
			} else if (row == 0) {
				arrayPosition--;
			} else if (row == 1) {
				arrayPosition++;
			}

			stones--;

			// Check to see if the last stone landed in an empty hole for captures
			if (stones == 0) {
				isStoneInEmptyHole(arrayPosition, originalPlayer);
			}
		}

		// If the last stone ended up in the current player's store, give them another
		// turn
		if (inCurrentPlayersStore) {
			inCurrentPlayersStore = false;
			row = originalPlayer;

			// Otherwise, let the other player have their turn
		} else {
			row = (originalPlayer == 0) ? 1 : 0;
		}
	}

	/**
	 * A method that determines if a player's last stone landed in an empty hole
	 * 
	 * If the last stone lands in an empty hole on their side, the player captures
	 * all the stones in the opponent's hole opposite to it, and places them in
	 * their store
	 * 
	 * @param arrayPosition  The final array postion where the last stone landed
	 * @param originalPlayer The current player
	 */
	private void isStoneInEmptyHole(int arrayPosition, int originalPlayer) {
		if (board[row][arrayPosition] == 0 && row == originalPlayer) {
			if (originalPlayer == 0) {
				p1Store += board[1][arrayPosition];
				board[1][arrayPosition] = 0;
			} else {
				p2Store += board[0][arrayPosition];
				board[0][arrayPosition] = 0;
			}
		}
	}
}