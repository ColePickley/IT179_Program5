/*
 * Created on: October 26, 2025
 * 
 * ULID: ctpickl
 * Class: IT 179
 */
package ilstu.edu;

import java.util.Scanner;

/*
 * Runs and controls the flow of the program
 * 
 * @author Cole Pickley
 */
public class Main {
	
	private static Game g;
	private static int numPlayers;
	private static Scanner scan;
	private static String[] playerNames;
	
	/**
	 * Runs the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		scan = new Scanner(System.in);
		playerNames = null;
		printInfo();
		setNumPlayers();
		setPlayerNames();
		g = new Game(playerNames);
		System.out.println("Press any key and enter to shuffle the deck and deal which will start the game:");
		scan.nextLine();
		System.out.println();
		g.startGame();
		runGame();
	}
	
	/**
	 * Takes a user input and sets the number of players
	 */
	private static void setNumPlayers() {
		boolean inputValid = false;
		System.out.println("Please enter the number of players:");
		while (!inputValid) {
			if (scan.hasNextInt())
				numPlayers = scan.nextInt();
			scan.nextLine();
			if (numPlayers > 1 && numPlayers < 6)
				inputValid = true;
			else
				System.out.println("You can only have 2-5 players. Please enter the number of players again:");
		}
	}
	
	/**
	 * Takes user input to set player names
	 */
	private static void setPlayerNames() {
		playerNames = new String[numPlayers];
		boolean inputValid;
		for (int i = 0; i < numPlayers; i++) {
			inputValid = false;
			while (!inputValid) {
				System.out.println("Player " + (i + 1) + ", please enter your name below:");
				playerNames[i] = scan.nextLine();
				boolean sharedName = false;
				for (int j = 0; j < i; j++) {
					if (playerNames[i].equals(playerNames[j])) {
						System.out.println("You must use a different name for every player.");
						sharedName = true;
					}
				}
				if (!sharedName)
					inputValid = true;
			}
		}
	}
	
	/**
	 * Controls the flow of the game and calls game functions
	 */
	private static void runGame() {
		boolean gameWon = false;
		Player winner = g.getWinner();
		if (winner != null)
			gameWon = true;
		while (!gameWon) {
			g.printGame();
			System.out.println();
			System.out.println("Press any key and enter to exectute your turn.");
			scan.nextLine();
			System.out.print(g.getCurrentPlayer().getName() + " drew the " + g.draw());
			System.out.println(" and discarded the " + g.discard() + ".");
			System.out.println();
			winner = g.getWinner();
			if (winner != null)
				gameWon = true;
			else
				g.nextPlayer();
		}
		System.out.println(winner.getName() + " won the game!");
		System.out.print("Winning hand: " + g.getHandAsString(winner.getHand()));
		scan.close();
	}
	
	/**
	 * Prints all the necessary information for the user to play the game
	 */
	private static void printInfo() {
		System.out.println("Rules:");
		System.out.println("- The game can only be played with 2-5 people.");
		System.out.println("- The game is to be played with only the cards Ace-10.");
		System.out.println("- To win, you must have four cards of either the same suit or that add up to 23.");
		System.out.println("- You can only draw from the floor if that card will allow you to win the game.");
		System.out.println();
		System.out.println("Instructions:");
		System.out.println("1. Shuffle the deck (post-shuffle, we will refer to the deck as the stack).");
		System.out.println("2. Deal 4 cards to each player from the stack.");
		System.out.println("3. The first player takes a card from the stack.");
		System.out.println("4. The first player picks one card to discard based on the following criteria:");
		System.out.println("   i.   If there's only one card of a suit, discard that card.");
		System.out.println("   ii.  If there's multiple cards that don't share a suit with another card in");
		System.out.println("        your hand, discard the card with the higher value.");
		System.out.println("   iii. If there's multiple cards that don't share a suit with another card in");
		System.out.println("        your hand and thier values are the same, pick any one of those cards at");
		System.out.println("        random to discard.");
		System.out.println("5. When the card is discarded, it should be placed face up in a new pile we will");
		System.out.println("   refer to as the floor.");
		System.out.println("6. Repeat steps 3-5 for each player until the game is won. After the first turn,");
		System.out.println("   the players will also have the option to draw from the floor instead of the");
		System.out.println("   stack.");
		System.out.println();
		System.out.println("Note:");
		System.out.println("All moves are automated within this game simulation.");
		System.out.println();
	}

}
