package mancala;

/**
 * The deterministic AI agent for mancala
 * 
 * @author Michael Opheim
 * @version 05/09/2023
 */
//TODO: comment, check
public class ArtificialIntelligenceAgent {

	/** A copy of the game in its current state for simulations */
	private MancalaModel aiModel;

	/**
	 * A method that picks the optimal move for a turn
	 * 
	 * @param model A reference to the model
	 * @return the most optimal move for the AI to take
	 */
	public int optimalMove(MancalaModel model, String recursiveIndent) {

		int optimalMove = 0;

		// Store the state of the current game so we can run simulations on it without
		// altering our actual game
		aiModel = new MancalaModel();
		aiModel.setBoard(model.getBoard(), 1);

		// Run simulations on the game to find an optimal move for our AI MAX player
		optimalMove = findOptimalMove(aiModel.getBoard(), recursiveIndent).getHole();

		// Return the most optimal move once we have found it via our simulations
		return optimalMove;
	}

	/**
	 * A method that does the work to find the most optimal move for the AI MAX
	 * player
	 * 
	 * @param board        A simulated board of some state
	 * @param currentDepth The depth of MINIMAX tree we are analyzing
	 * @return The most optimal move for the AI player
	 */
	private BestMoveBoardValue findOptimalMove(int[][] board, String recursiveIndent) {

		// Create an object to the most optimal move for the AI players
		BestMoveBoardValue optimalMove = new BestMoveBoardValue(-1, 0);

		int value = -1;

		for (int hole = 0; hole < 6; hole++) {

			// Make sure that the hole contains stones
			if (board[1][hole] != 0) {

				MancalaModel localCopy = new MancalaModel();
				localCopy.setBoard(aiModel.getBoard(), 1);
				
				System.out.println(recursiveIndent + "hole:" + hole);

				localCopy.moveStones(hole);

				if (localCopy.getCurrentPlayer() == 1) {
					int holeToCheck = optimalMove(localCopy, recursiveIndent + "   ");
					localCopy.moveStones(holeToCheck);
				}

				System.out.println(recursiveIndent + "P2 Store:" + localCopy.getP2Store() + "\n");

				if (value < localCopy.getP2Store()) {
					value = localCopy.getP2Store();
					optimalMove.setValue(value);
					optimalMove.setHole(hole);
				}
			}
		}
		
		System.out.println(recursiveIndent + "Most optimal move: " + optimalMove.getHole());

		return optimalMove;
	}

	/**
	 * A class to store the best move possible for the AI MAX player
	 * 
	 * @author Michael Opheim
	 * @version 05/09/2023
	 */
	private class BestMoveBoardValue {

		/**
		 * The value that the most optimal move will provide the AI MAX player, assuming
		 * the MIN player makes the moves that are most optimal for them as well
		 */
		private int value;

		/**
		 * The number of the hole that will provide the most optimal move for the AI MAX
		 * player
		 */
		private int hole;

		/**
		 * Constructor
		 * 
		 * @param value The possible point gain for the most optimal move
		 * @param hole  The number of the hole that will provide the most optimal move
		 *              for the AI MAX player
		 */
		public BestMoveBoardValue(int value, int hole) {
			this.value = value;
			this.hole = hole;
		}

		/**
		 * The setter for the value of the most optimal move
		 * 
		 * @param value The possible point gain for the most optimal move
		 */
		public void setValue(int value) {
			this.value = value;
		}

		/**
		 * The getter the value of the most optimal move
		 * 
		 * @return the possible point gain for the most optimal move
		 */
		public int getValue() {
			return this.value;
		}

		/**
		 * The setter for the hole that will provide the AI MAX player with the most
		 * optimal move
		 * 
		 * @param hole The number of the hole that will provide the most optimal move
		 *             for the AI MAX player
		 */
		public void setHole(int hole) {
			this.hole = hole;
		}

		/**
		 * The getter for the hole that will provide the AI MAX player with the most
		 * optimal move
		 * 
		 * @return the number of the hole that will provide the most optimal move for
		 *         the AI MAX player
		 */
		public int getHole() {
			return this.hole;
		}
	}
}