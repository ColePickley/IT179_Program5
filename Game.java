/*
 * Created on: October 26, 2025
 * 
 * ULID: ctpickl
 * Class: IT 179
 */
package ilstu.edu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
 * Stores game data and executes game functions
 * 
 * @author Cole Pickley
 */
public class Game {
	private Card startCard;
	private Stack<Card> stack;
	private Stack<Card> floor;
	private Queue<Player> players;
	
	/**
	 * Creates a new Game object
	 * Initializes players using the playerNames parameter
	 * Initializes startCard
	 * Creates an empty stack for the 'stack' and 'floor' objects
	 * 
	 * @param playerNames
	 */
	public Game(String[] playerNames) {
		players = new LinkedList<>();
		for (String name: playerNames)
			players.offer(new Player(name));
		this.startCard = new Card("Spades", 1);
		this.stack = new Stack<>();
		this.floor = new Stack<>();
	}
	
	/**
	 * Creates the deck
	 * Shuffles the deck
	 * Deals the Player's hands
	 */
	public void startGame() {
		this.createDeck();
		this.fillStack();
		this.dealHands();
	}
	
	/**
	 * Returns the inputed hand as a String
	 * 
	 * @param hand
	 * @return
	 */
	public String getHandAsString(Card[] hand) {
		String output = "";
		for (int i = 0; i < 4; i++)
			output += hand[i].toString();
		return output;
	}
	
	/**
	 * Moves the Player at the front of the Queue to the back
	 */
	public void nextPlayer() {
		players.offer(players.poll());
	}
	
	/**
	 * Returns a Player object if that Player has won the game
	 * 
	 * @return
	 */
	public Player getWinner() {
		for (Player player: players) {
			int sum = 0;
			Card[] hand = player.getHand();
			boolean sameSuit = true;
			for (int i = 0; i < 3; i++)
				if (!hand[i].suit.equals(hand[i + 1].suit))
					sameSuit = false;
			if (sameSuit)
				return player;
			for (int i = 0; i < 4; i++)
				sum += hand[i].value;
			if (sum == 23)
				return player;
		}
		return null;
	}
	
	/**
	 * Prints the Card at the top of the floor and each Player object's hand
	 */
	public void printGame() {
		System.out.println("Turn: " + players.peek().getName());
		if (floor.isEmpty())
			System.out.println("Floor is empty");
		else
			System.out.println("Floor: " + floor.peek());
		System.out.println();
		Player start = players.peek();
		System.out.println(start.getName() + "'s hand:");
		Card[] hand = start.getHand();
		System.out.println(this.getHandAsString(hand));
		players.offer(players.poll());
		while (players.peek() != start) {
			hand = players.peek().getHand();
			System.out.println(players.peek().getName() + "'s hand:");
			System.out.println(this.getHandAsString(hand));
			players.offer(players.poll());
		}
	}
	
	/**
	 * Returns the Player object at the front of the Queue
	 * 
	 * @return
	 */
	public Player getCurrentPlayer() {
		return players.peek();
	}
	
	/**
	 * Draws a Card from either the floor or the stack and puts it in the current Player's hand
	 * Returns a String containing the Card drawn and where it was drawn from
	 * 
	 * @return
	 */
	public String draw() {
		Card[] hand = players.peek().getHand();
		String drawLocation = "";
		if (!floor.isEmpty()) {
			int[] sums = this.getSums(hand);
			for (int i = 0; i < 4; i++) {
				if (floor.peek().value + sums[i] == 23) {
					hand[4] = floor.pop();
					drawLocation = " from the floor";
					break;
				}
			}
		}
		if (hand[4] == null) {
			hand[4] = stack.pop();
			drawLocation = " from the stack";
		}
		return hand[4].toString() + drawLocation;
	}
	
	/**
	 * Discards the correct Card from the current Player's hand
	 * Returns a String representing the Card that's been discarded
	 * Calls sumTest() and suitsTest()
	 * 
	 * @return
	 */
	public String discard() {
		Card[] hand = players.peek().getHand();
		Card discardCard = this.sumTest(hand);
		if (discardCard == null)
			discardCard = this.suitsTest(hand);
		int i = this.getCardIndex(discardCard, hand);
		for (int j = i; j < 4; j++)
			hand[j] = hand[j + 1];
		hand[4] = null;
		floor.push(discardCard);
		return discardCard.toString();
	}
	
	/**
	 * Tests if any combination of 4 Card object's values equals 23
	 * If a combo equals 23, it returns the Card not used in that combo
	 * If no combo equals 23, it returns null
	 * 
	 * @param hand
	 * @return
	 */
	private Card sumTest(Card[] hand) {
		if (hand[0].value + hand[1].value + hand[2].value + hand[3].value == 23)
			return hand[4];
		else if (hand[0].value + hand[1].value + hand[2].value + hand[4].value == 23)
			return hand[3];
		else if (hand[0].value + hand[1].value + hand[3].value + hand[4].value == 23)
			return hand[2];
		else if (hand[0].value + hand[2].value + hand[3].value + hand[4].value == 23)
			return hand[1];
		else if (hand[1].value + hand[2].value + hand[3].value + hand[4].value == 23)
			return hand[0];
		return null;
	}
	
	/**
	 * Called when sumTest() returns null
	 * Tests for Card objects that share the least suits with other Card objects in a hand
	 * Returns the Card with the highest value
	 * Calls valueTest()
	 * If more than one Card does not share a suit with another Card in the hand, calls sameValueTest()
	 * 
	 * @param hand
	 * @return
	 */
	private Card suitsTest(Card[] hand) {
		ArrayList<Card> singleSuits = new ArrayList<>();
		ArrayList<Card> doubleSuits = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			int sharedSuits = 0;
			for (int j = 0; j < 5; j++) {
				if (hand[i].suit.equals(hand[j].suit))
					sharedSuits++;
			}
			switch (sharedSuits) {
				case 1:
					singleSuits.add(hand[i]);
					break;
				case 2:
					doubleSuits.add(hand[i]);
					break;
			}
		}
		Card discardCard = this.valueTest(singleSuits, doubleSuits);
		if (singleSuits.size() > 1)
			discardCard = this.sameValueTest(discardCard, singleSuits);
		return discardCard;
	}
	
	/**
	 * Returns the Card with the greatest value from the correct set of Card objects
	 * Calls getGreatestValueCard()
	 * 
	 * @param singleSuits
	 * @param doubleSuits
	 * @return
	 */
	private Card valueTest(ArrayList<Card> singleSuits, ArrayList<Card> doubleSuits) {
		Card discardCard;
		if (singleSuits.size() == 0)
			discardCard = this.getGreatestValueCard(doubleSuits);
		else if (singleSuits.size() == 1)
			discardCard = singleSuits.get(0);
		else
			discardCard = this.getGreatestValueCard(singleSuits);
		return discardCard;
	}
	
	/**
	 * Returns the Card with the greatest value from a select set of Card objects
	 * 
	 * @param cards
	 * @return
	 */
	private Card getGreatestValueCard(ArrayList<Card> cards) {
		Card greatestValueCard;
		if (cards.get(0).value > cards.get(1).value)
			greatestValueCard = cards.get(0);
		else
			greatestValueCard = cards.get(1);
		if (cards.size() > 2 && greatestValueCard.value < cards.get(2).value)
			greatestValueCard = cards.get(2);
		return greatestValueCard;
	}
	
	/**
	 * Tests to see if there are multiple Card objects with the highest value that can be discarded
	 * If so, it returns one of those Card objects at random
	 * If not, it returns the initial discardCard parameter
	 * 
	 * @param discardCard
	 * @param cards
	 * @return
	 */
	private Card sameValueTest(Card discardCard, ArrayList<Card> cards) {
		ArrayList<Card> sameValueCards = new ArrayList<>();
		for (int i = 0; i < cards.size(); i++) {
			if (discardCard != cards.get(i) && discardCard.value == cards.get(i).value) {
				sameValueCards.add(cards.get(i));
			}
		}
		sameValueCards.add(discardCard);
		
		if (sameValueCards.size() > 1) {
			int i = (int) (Math.random() * sameValueCards.size());
			return sameValueCards.get(i);
		}
		return discardCard;
	}
	
	/**
	 * Returns the index of a Card within a hand
	 * 
	 * @param card
	 * @param hand
	 * @return
	 */
	private int getCardIndex(Card card, Card[] hand) {
		for (int i = 0; i < 5; i++)
			if (hand[i] == card)
				return i;
		return -1;
	}
	
	/**
	 * Sets the initial hand for each Player object at the start of the game
	 */
	private void dealHands() {
		for (Player player: players) {
			Card[] hand = new Card[5];
			for (int i = 0; i < 4; i++)
				hand[i] = stack.pop();
			player.setHand(hand);
		}
	}
	
	/**
	 * Returns an array of all 3-Card sums within a 4-Card hand
	 * 
	 * @param hand
	 * @return
	 */
	private int[] getSums(Card[] hand) {
		int[] sums = new int[4];
		sums[0] = hand[0].value + hand[1].value + hand[2].value;
		sums[1] = hand[0].value + hand[1].value + hand[3].value;
		sums[2] = hand[0].value + hand[2].value + hand[3].value;
		sums[3] = hand[1].value + hand[2].value + hand[3].value;
		return sums;
	}
	
	/**
	 * Creates a single linked list of Card objects ordered as they would be in a sorted deck
	 */
	private void createDeck() {
		Card temp = this.startCard;
		for (int i = 2; i <= 10; i++) {
			temp.next = new Card("Spades", i);
			temp = temp.next;
		}
		for (int i = 1; i <= 10; i++) {
			temp.next = new Card("Diamonds", i);
			temp = temp.next;
		}
		for (int i = 1; i <= 10; i++) {
			temp.next = new Card("Clubs", i);
			temp = temp.next;
		}
		for (int i = 1; i <= 10; i++) {
			temp.next = new Card("Hearts", i);
			temp = temp.next;
		}
	}
	
	/**
	 * Shuffles the deck into the stack
	 */
	private void fillStack() {
		Card temp = this.startCard;
		Card prev = null;
		int randomInt;
		while (startCard != null) {
			randomInt = (int) (Math.random() * 40);
			for (int i = 0; i <= randomInt; i++) {
				prev = temp;
				if (temp.next == null)
					temp = this.startCard;
				else
					temp = temp.next;
			}
			this.stack.push(temp);
			if (temp == this.startCard && temp.next == null)
				this.startCard = null;
			else if (temp.next == null)
				prev.next = null;
			else if (temp == this.startCard)
				this.startCard = temp.next;
			else
				prev.next = temp.next;
			temp = startCard;
		}
	}
	
	/*
	 * Stores the card value and suit
	 * References the next card in the deck
	 * 
	 * @author Cole Pickley
	 */
	public static class Card {
		private String suit;
		private int value;
		private Card next;
		
		/**
		 * Creates a new Card object with a set suit and value
		 * 
		 * @param suit
		 * @param value
		 */
		public Card(String suit, int value) {
			this.suit = suit;
			this.value = value;
		}
		
		/**
		 * Returns a String representing the Card
		 * 
		 * @return
		 */
		public String toString() {
			if (this.value == 1)
				return "|Ace of " + this.suit + "|";
			return "|" + this.value + " of " + this.suit + "|";
		}
	}
}
