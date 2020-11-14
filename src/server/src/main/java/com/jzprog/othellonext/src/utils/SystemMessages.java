package com.jzprog.othellonext.src.utils;

import org.springframework.stereotype.Component;

@Component
public class SystemMessages {
	
	public static final String INIT_ENDPOINT = "/startNewGame";
	public static final String SELECT_TURN_ENDPOINT = "/choose";
	public static final String PLAY_MOVE_ENDPOINT = "/play";
	
	public static final String INIT_GAME_SUCCESS = "A new Game initialized!";
	public static final String SELECT_TURN_INVALID_ERROR = "You cannot choose color when game in progress...";
	public static final String INVALID_MOVE_ERROR = "Invalid Move or on other player's turn...";
	public static final String NON_EMPTY_MOVE_ERROR = "Invalid Move!Try Again!";
	public static final String NO_AVAILABLE_MOVE_ERROR = "%s cannot play! Next Player's Turn!";
	public static final String PLAY_FIRST_MESSAGE = "You play First!";
	public static final String PLAY_AI_MESSAGE = "AI player's turn!";


	public enum ValidationTypes {
		RESET_GAME_VALIDATION,
		MOVE_VALIDITY,
		SELECT_VALIDATION
	}
	
    public enum TileStates {
		EMPTY,
		BLACK,
		WHITE
	}
    
    public enum MoveResults {
		INVALID_MOVE("Invalid Move!Try Again!"),
		VALID_MOVE("You played (%s,%s)"),
		WINNING_MOVE("You won the game!"),
		LOSING_MOVE("You lost the game...");
    	
    	private String text;
    	
    	MoveResults(String text) {
    		this.text = text;
    	}
    	
    	public String getText() {
    		return text;
    	}
	}
}
