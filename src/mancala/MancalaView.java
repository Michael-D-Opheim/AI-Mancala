package mancala;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The front-end/view for the mancala program. It displays an interactive
 * mancala board GUI for users to play on
 * 
 * @author Michael Opheim
 * @version 05/09/2023
 */

//TODO: comments
public class MancalaView extends Application {

	/** A reference to the model of the GUI */
	private MancalaModel model;

	/** The layout of the scene for the GUI */
	private BorderPane root;

	private Button gameButtonAI;

	private Button gameButtonHuman;

	@Override
	/**
	 * Creates the mancala GUI
	 * 
	 * @param primaryStage The window where the GUI will be displayed
	 * @throws Exception if an error occurs
	 */
	public void start(Stage primaryStage) throws Exception {

		try {

			// Instantiate the GUI layout
			root = new BorderPane();
			Scene scene = new Scene(root, 940, 470);
			primaryStage.setScene(scene);

			// Instantiate the back-end of the GUI
			model = new MancalaModel();

			// Initiate the mancala board
			fillBoard();

			// Create the mancala board image backdrop for the GUI
			root.setStyle(" -fx-background-color: #ab6937; -fx-background-position: center, "
					+ "center;-fx-background-repeat: no-repeat; "
					+ "-fx-background-size: 900 250; -fx-background-image: "
					+ "url(\"https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/8a1b6d9c1b1f0d4.png\");");

			// Create an HBox for buttons
			HBox hBox = new HBox();
			hBox.setSpacing(10);
			hBox.setPadding(new Insets(10, 0, 10, 0));

			// Create a button that allows the user to view the rules of mancala
			Button rules = new Button("Rules");
			rules.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Rules");
				alert.setContentText(getRules());
				alert.showAndWait();
			});
			rules.setPrefSize(100, 40);
			rules.setStyle(" -fx-background-color: #7d4d29; -fx-text-fill: black;");
			hBox.getChildren().add(rules);

			// Create a button that allows the user to start a new game
			Button startNewGame = new Button("Start New Game");
			startNewGame.setOnAction((event) -> {
				model.initializeBoard();
				fillBoard();
			});
			startNewGame.setPrefSize(150, 40);
			startNewGame.setStyle(" -fx-background-color: #7d4d29; -fx-text-fill: black; ");
			hBox.getChildren().add(startNewGame);

			gameButtonAI = new Button("Play with AI");
			gameButtonAI.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("AI Game");
				alert.setContentText("starting new AI game");
				alert.showAndWait();
				hBox.getChildren().remove(gameButtonAI);
				hBox.getChildren().add(gameButtonHuman);
				model.setIsHumanGame(false);
				model.initializeBoard();
				fillBoard();
			});
			gameButtonAI.setPrefSize(100, 40);
			gameButtonAI.setStyle(" -fx-background-color: #7d4d29; -fx-text-fill: black;");

			gameButtonHuman = new Button("2 Player Mode");
			gameButtonHuman.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("2 Player Mode");
				alert.setContentText("starting new 2 player game");
				alert.showAndWait();
				hBox.getChildren().remove(gameButtonHuman);
				hBox.getChildren().add(gameButtonAI);
				model.setIsHumanGame(true);
				model.initializeBoard();
				fillBoard();
			});
			gameButtonHuman.setPrefSize(100, 40);
			gameButtonHuman.setStyle(" -fx-background-color: #7d4d29; -fx-text-fill: black;");
			hBox.getChildren().add(gameButtonHuman);

			// Format the HBox
			hBox.setAlignment(Pos.CENTER);
			root.setBottom(hBox);

			// Show the GUI window
			primaryStage.show();

			// Catch any exceptions that might be produced from the GUI
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Have AI make a turn
	private void runAI() {
		if (!model.getIsHumanGame()) {
//			try {
////				Thread.sleep(400); //TODO: move this
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			ArtificialIntelligenceAgent aiAgent = new ArtificialIntelligenceAgent();
			int aiMove = aiAgent.optimalMove(model);
			model.moveStones(aiMove);

			// Recreate the board to reflect the changes made from stones being moved
			fillBoard();

			// If the move caused the game to end...
			if (model.isWinningState()) {

				// Add remaining stones to the other player's total
				model.addRemainingStonesToPlayersStore();

				// And alert the players that the game is over, stating who won or whether there
				// was a tie
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Game Over");
				alert.setContentText(model.displayWinner());
				alert.showAndWait();
			}

		}
	}

	/**
	 * A method that instantiates and sets-up the mancala board of the GUI for user
	 * interactivity
	 */
	public void fillBoard() {

		// Create a GridPane object to evenly format the mancala board
		GridPane grid = new GridPane();

		// Create the mancala board using position values from the model
		int board[][] = model.getBoard();

		// Create a counter, which will give every position on the board a unique
		// identifier
		int counter = 0;

		// Loop to create the individual board positions and buttons
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 6; col++) {

				// Create and format buttons for each position
				Button button = new Button(board[row][col] + "");
				button.setStyle(" -fx-background-color: #7d4d29; -fx-text-fill: black;  -fx-background-radius: 150;\n"
						+ "    -fx-pref-width: 70;\n" + "    -fx-pref-height: 70;\n");
				button.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
				if (row == 0) {
					GridPane.setMargin(button, new Insets(0, 30, 17, 17));
				} else {
					GridPane.setMargin(button, new Insets(30, 30, 0, 17));
				}

				// Make the row, column, and counter values final to keep the compiler happy
				final int finalRow = row;
				final int finalCol = col;
				final int finalCounter = counter;

				// Set up the event handling for the buttons
				button.setOnAction((event) -> {

					// If the current player clicked on the wrong side of the board somewhere, alert
					// them
					// Since the AI will not choose an invalid move this is only for the human
					// players
					if (!model.isValidInput(model.getCurrentPlayer(), finalCounter)) {
						String side = (finalRow == 0) ? "bottom" : "top";
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Try Again");
						alert.setContentText("Player " + (model.getCurrentPlayer() + 1) + ", "
								+ "You can only select from the " + side + " side of the board.");
						alert.showAndWait();

						// Otherwise, if the current player clicked on a position with no stones, alert
						// them of that
					} else if (board[finalRow][finalCol] == 0) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Try Again");
						alert.setContentText("You can not select holes that are zero.");
						alert.showAndWait();

						// Else, the current player made a valid move, so move the stones accordingly
					} else {
						model.moveStones(finalCounter);

						// Recreate the board to reflect the changes made from stones being moved
						fillBoard();

						// If the move caused the game to end...
						if (model.isWinningState()) {

							// Add remaining stones to the other player's total
							model.addRemainingStonesToPlayersStore();

							// And alert the players that the game is over, stating who won or whether there
							// was a tie
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("Game Over");
							alert.setContentText(model.displayWinner());
							alert.showAndWait();
						}

						if (!model.getIsHumanGame()) {
							while (model.getCurrentPlayer() == 1) {
								runAI();
							}
						}
					}
				});

				// Add button to the mancala board
				button.setPrefSize(100, 40);
				grid.add(button, col, row);
				counter++;
			}
		}

		// Create a VBox to store the mancala board display
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10, 0, 10, 0));
		vBox.setSpacing(50);

		// Add some text to distinguish player 1's side of the board from player 2's
		Text p1SideText = new Text("Player 1 Side");
		p1SideText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		vBox.getChildren().add(p1SideText);

		// Add the mancala board to the display
		vBox.getChildren().add(grid);

		// Add some text to distinguish player 2's side of the board from player 1's
		Text p2 = new Text("Player 2 Side");
		if (!model.getIsHumanGame()) {
			p2 = new Text("AI Side");
		}
		p2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		vBox.getChildren().add(p2);

		// Format the VBox
		root.setCenter(vBox);

		// Add the finishing touches to the display, starting with player scores
		Text player1 = new Text("     P1:     \n      " + model.getP1Store() + "        \n         ");
		player1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		BorderPane.setMargin(player1, new Insets(95, 12, 20, 12));
		root.setLeft(player1);

		Text player2 = new Text("P2:  \n " + model.getP2Store() + "       \n            ");
		if (!model.getIsHumanGame()) {
			player2 = new Text("AI:  \n " + model.getP2Store() + "       \n            ");
		}

		player2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		BorderPane.setMargin(player2, new Insets(95, 12, 20, 12));
		root.setRight(player2);

		// Also add a header to the display that notifies users whose turn it is
		Text playerTurn = new Text(displayUserPrompt(model.getCurrentPlayer()));
		playerTurn.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		BorderPane.setAlignment(playerTurn, Pos.CENTER);
		BorderPane.setMargin(playerTurn, new Insets(12, 12, 12, 12));
		root.setTop(playerTurn);
	}

	/**
	 * A method that displays a prompt to notify the players whose turn it is
	 * 
	 * @param playerTurn The current player (the player whose turn it is)
	 * @return A prompt stating which player's turn it is
	 */
	private String displayUserPrompt(int playerTurn) {
		if (!model.getIsHumanGame() && playerTurn == 1) {
			return "AI Turn";
		} else if (playerTurn == 1) {
			return "Player 2 Turn";
		} else {
			return "Player 1 Turn";
		}
	}

	/**
	 * A method that gets the rules for mancala
	 * 
	 * @return the rules for mancala
	 */
	private String getRules() {
		return "The game of mancala is played on a board with two rows of six holes each,"
				+ " with a larger hole on each end called the \"store.\"\n"
				+ " Each player starts with six stones in each of their six holes.\n\n"
				+ "The first player picks up all the stones in one of their holes and distributes them,"
				+ " one by one, in a counterclockwise direction, placing one stone in each hole,"
				+ " including their own store, but skipping the opponent's store."
				+ "\n If the last stone lands in the player's store, they get to take another turn."
				+ "\n If the last stone lands in an empty hole on their side, the player captures all the stones"
				+ " in the opponent's hole opposite to it and places them in their store.\n\n"
				+ "After the first player completes their turn, the next player takes their turn and repeats the same process.\n\n"
				+ "Player 1's side consists of slots 0 to 5 and Player 2's side consists of 6 to 11.\n"
				+ "Each turn player 1 will enter a number 0 to 5 and player 2 will enter a number 6 to 11.\n\n"
				+ "The game ends when one player's six holes are empty. The player with the most stones in their store wins.\n\n"
				+ "Learn More:\n"
				+ "\"Mancala Rules.\" Masters Traditional Games. https://www.mastersofgames.com/rules/mancala-rules.htm\n"
				+ "\"Mancala.\" Wikipedia. https://en.wikipedia.org/wiki/Mancala\n\n";
	}

	/**
	 * Launches GUI
	 * 
	 * @param args All provided arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}