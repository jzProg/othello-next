package com.jzprog.othellonext.src.services;

import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.ScopedProxyMode;
import com.jzprog.othellonext.src.model.Action;
import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GameService {
    
	private GameState gameState;
	private TileStates[][] board;
	private double utility; //value depending on the winner(1:win for black,0:win for white,0.5:draw,-1:non terminal state)
	private Action currentMove;
	private TileStates player;
	
	public int init() {
		//todo validate -> always
		this.utility = -1;
		initBoard();
		gameState = new InitialState();
		play();
		return new Random().nextInt();
	}
	
	public GameState getState() {
		return gameState;
	}
	
	public void setState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public void play() {
		gameState.makeMove(this);
	}
	
	public void nextState() {
		gameState.next(this);
	}
	
	public TileStates[][] getBoard() {
		return board;
	}
	
	public double getUtility() {
		return utility;
	}
	
	public void setUtility(double utility) {
		this.utility = utility;
	}
	
	public Action getCurrentMove() {
		return currentMove;
	}

	public void setCurrentMove(Action currentMove) {
		//todo validate -> only when player turn
		this.currentMove = currentMove;
	}
	
	public TileStates getPlayer() {
		return player;
	}

	public void setPlayer(TileStates player) {
		//todo validate -> only when initial
		this.player = player;
	}

	private void initBoard() {
		board = new TileStates[8][8];
		for (int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				board[i][j] = TileStates.EMPTY;
			 }
		}
		board[3][3] = TileStates.WHITE;
		board[3][4] = TileStates.BLACK;
		board[4][3] = TileStates.BLACK;
		board[4][4] = TileStates.WHITE;
	}
}
