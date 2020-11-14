package com.jzprog.othellonext.src.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.ScopedProxyMode;

import com.jzprog.othellonext.src.advices.LogMethodInfo;
import com.jzprog.othellonext.src.model.Action;
import com.jzprog.othellonext.src.model.StateInfo;
import com.jzprog.othellonext.src.model.ValidationResponse;
import com.jzprog.othellonext.src.services.validation.ValidationStrategy;
import com.jzprog.othellonext.src.utils.SystemMessages;
import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GameService {
	
	@Autowired
	ValidationStrategy validation;
    
	private GameState gameState;
	private StateInfo gameInfo;
	private Action currentMove;
	private TileStates player; // the color of User player
	private ValidationResponse completed;
	private int gameId = -1;
	
	public int init() {
		gameState = new InitialState();
		setCompleted(validation.provideValidation(SystemMessages.ValidationTypes.RESET_GAME_VALIDATION, gameState)); // validate -> always
        if (isCompleted().isSuccess()) play();
		return gameId;
	}
	
	public void play() {
		gameState.makeMove(this);
	}
	
	public void nextState() {
		gameState.next(this);
	}
	
	public TileStates[][] getBoard() {
		return gameInfo.getOthelloBoard();
	}
	
	public double getUtility() {
		return gameInfo.getUtility();
	}
	
	public Action getCurrentMove() {
		return currentMove;
	}

	@LogMethodInfo
	public void setCurrentMove(Action currentMove) {
		setCompleted(validation.provideValidation(SystemMessages.ValidationTypes.MOVE_VALIDITY, gameState, currentMove, getInfo().clone())); // validate -> only when player turn
		if (isCompleted().isSuccess()) this.currentMove = currentMove;
	}
	
	public TileStates getPlayer() {
		return player;
	}

	@LogMethodInfo
	public void setPlayer(TileStates player) {
		setCompleted(validation.provideValidation(SystemMessages.ValidationTypes.SELECT_VALIDATION, gameState)); // validate -> only when initial
		if (isCompleted().isSuccess()) this.player = player;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public ValidationResponse isCompleted() {
		return completed;
	}

	public void setCompleted(ValidationResponse completed) {
		this.completed = completed;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setBoard(TileStates[][] board) {
	  gameInfo.setOthelloBoard(board);
	}
	
	public boolean isTerminal() {
		return gameInfo.getUtility() != -1;
	}
	
	public StateInfo getInfo() {
		return gameInfo;
	}
	
	@LogMethodInfo
	public void putDisc() {
		Action move = getCurrentMove();
		gameInfo.putDisc(move.getX(), move.getY());
	}

	protected void initBoard() {
		TileStates[][] board = new TileStates[8][8];
		for (int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				board[i][j] = TileStates.EMPTY;
			 }
		}
		board[3][3] = TileStates.WHITE;
		board[3][4] = TileStates.BLACK;
		board[4][3] = TileStates.BLACK;
		board[4][4] = TileStates.WHITE;
		gameInfo = new StateInfo(board, TileStates.BLACK, -1);
	}

	@Override
	public String toString() {
		return "GameService [validation=" + validation + ", gameState=" + gameState + ", gameInfo=" + gameInfo
				+ ", currentMove=" + currentMove + ", player=" + player + ", completed=" + completed + ", gameId="
				+ gameId + "]";
	}
}
