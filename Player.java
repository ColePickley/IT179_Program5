/*
 * Created on: October 26, 2025
 * 
 * ULID: ctpickl
 * Class: IT 179
 */
package ilstu.edu;

import ilstu.edu.Game.Card;

/*
 * Stores Player data
 * 
 * @author Cole Pickley
 */
public class Player {
	private Card[] hand;
	private String name;
	
	/**
	 * Creates a new Player object with the inputed name
	 * 
	 * @param name
	 */
	public Player(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the Player's name
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the Player's hand
	 * @return
	 */
	public Card[] getHand() {
		return this.hand;
	}
	
	/**
	 * Sets the Player's hand
	 * 
	 * @param hand
	 */
	public void setHand(Card[] hand) {
		this.hand = hand;
	}
}
