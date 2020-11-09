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
		INVALID_MOVE,
		VALID_MOVE,
		WINNING_MOVE,
		LOSING_MOVE
	}
}
