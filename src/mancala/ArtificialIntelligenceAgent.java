package mancala;

/**
 * The AI agent for mancala games
 * 
 * @author Michael Opheim
 * @version 05/15/2023
 */
public class ArtificialIntelligenceAgent {

	/**
	 * A method that allows the AI agent to pick the optimal move for a turn
	 * 
	 * @param model           A reference to the model
	 * @param recursiveIndent a String that indents console output based on
	 *                        recursion for easier reading of the AI's logic
	 * @return the most optimal move for the AI to take
	 */
	public int optimalMove(MancalaModel model, String recursiveIndent) {

		// Create a variable to store the AI agent's optimal move
		int optimalMove = 0;

		// Store the state of the current game so we can run simulations on it without
		// altering our actual game
		MancalaModel aiModel = new MancalaModel();
		aiModel.copy(model.getBoard(), 1, model.getP1Store(), model.getP2Store());

		// Run simulations on the game to find an optimal move for our AI MAX player
		optimalMove = findOptimalMove(aiModel, recursiveIndent).getHole();

		// Return the most optimal move once we have found it via our simulations
		return optimalMove;
	}

	/**
	 * A method that does the work to find the most optimal move for the AI player
	 * 
	 * @param board           A simulated board of some state
	 * @param recursiveIndent a String that indents console output based on
	 *                        recursion for easier reading of the AI's logic
	 * @return The most optimal move for the AI player
	 */
	private BestMoveBoardValue findOptimalMove(MancalaModel aiModel, String recursiveIndent) {

		// Create an object to the most optimal move for the AI player
		BestMoveBoardValue optimalMove = new BestMoveBoardValue(-1, 0);

		// Create a variable to save values during potential move analyses
		int value = -1;

		for (int hole = 0; hole < 6; hole++) {

			// Make sure that the hole contains stones
			if (aiModel.getBoard()[1][hole] != 0) {

				// Create a copy of the state of the game for simulated analyses of moves
				MancalaModel localCopy = new MancalaModel();
				localCopy.copy(aiModel.getBoard(), 1, aiModel.getP1Store(), aiModel.getP2Store());

				// Move stones from a particular hole
				localCopy.moveStones(hole);

				// If the move made causes the AI agent to get another turn, analyse possible
				// subsequent turns
				while (localCopy.getCurrentPlayer() == 1) {

					// Recursively call the tail-recursive method to start analyzing moves again
					// from this state onwards
					int holeToCheck = optimalMove(localCopy, recursiveIndent + "   ");

					// Move stones according to the findings of the recursive analysis
					localCopy.moveStones(holeToCheck);
				}

				System.out.println(
						recursiveIndent + "Hole: " + hole + " - Resulting Total Score: " + localCopy.getP2Store() + "\n");

				// If the potential value is higher for a pocket than our current highest saved
				// potential value, save it
				if (value < localCopy.getP2Store()) {
					value = localCopy.getP2Store();
					optimalMove.setValue(value);
					optimalMove.setHole(hole);
				}
			} else {
				System.out.println(recursiveIndent + "Hole " + hole + " is empty\n");
			}
		}

		System.out.println(
				recursiveIndent + "Most optimal move: " + optimalMove.getHole() + " Value: " + optimalMove.getValue());

		return optimalMove; // Return the most optimal move for the AI agent
	}

	/**
	 * A class to store the best move possible for the AI MAX player
	 * 
	 * @author Michael Opheim
	 * @version 05/15/2023
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