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
import com.jzprog.othellonext.src.utils.SystemMessages.GameLevels;
import com.jzprog.othellonext.src.utils.SystemMessages.MoveResults;
import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GameService {
	
	@Autowired
	private ValidationStrategy validation;
	
	@Autowired
	private MinMaxSearch minMax;
    
	private GameState gameState;
	private StateInfo gameInfo;
	private Action currentMove;
	private TileStates player; // the color of User player
	private ValidationResponse completed;
	private int gameId = -1;
	private MoveResults result;

	public int init() {
		gameState = new InitialState();
		setCompleted(validation.provideValidation(SystemMessages.ValidationTypes.RESET_GAME_VALIDATION, gameState)); // validate -> always
        if (isCompleted().isSuccess()) play();
		return gameId;
	}
	
	@LogMethodInfo
	public void play() {
		gameState.makeMove(this);
		checkResult();
	}
	
	@LogMethodInfo
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
		setCompleted(validation.provideValidation(SystemMessages.ValidationTypes.MOVE_VALIDITY, gameState, currentMove, getInfo().clone(), getPlayer())); // validate -> only when player turn
		if (isCompleted().isSuccess()) this.currentMove = currentMove;
		else if (isCompleted().getErrorMessage().equals(SystemMessages.NO_AVAILABLE_MOVE_ERROR)){
			checkResult();
			getInfo().setNextPlayer();
			getInfo().calculateAvailableMoves();
		} else if (isCompleted().getErrorMessage().equals(SystemMessages.NON_EMPTY_MOVE_ERROR)) {
			getInfo().analyzeUtility();
		}
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
	
	public void setDifficulty(GameLevels difficulty) {
		if (difficulty == null) {
			difficulty = GameLevels.EASY;
		}
		minMax.updateDepth(difficulty.getDepth());
	}
	
	public MoveResults getResult() {
		return result;
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
	public boolean checkNextPlayerAvailability() {
		if (!getInfo().canPlay(getInfo().getPlayerToMove())) {
			getInfo().setNextPlayer();
			getInfo().calculateAvailableMoves();
			return false;
		}
		return true;
	}
	
	@LogMethodInfo
	public void putDisc() {
		Action move = getCurrentMove();
		gameInfo.putDisc(move.getX(), move.getY());	
	}
	
	public void makeDecision() {
		setCurrentMove(minMax.makeDecision(getInfo().clone()));
	}

	@LogMethodInfo
	private double determineWinner(TileStates player) {
		double utility = getUtility();
		System.out.print("initial:"+utility);
		if (player.equals(TileStates.WHITE)) utility = 1 - utility;
		System.out.print("final:"+utility);
		return utility;
	}
	
	@LogMethodInfo
	public void checkResult() {
	  if (isTerminal()){
		  String winner = determineWinner(TileStates.BLACK) == 1 ? TileStates.BLACK.name() : determineWinner(TileStates.WHITE) == 1 ? TileStates.WHITE.name() : "";
		 if (winner.equals("")) { // draw
			 this.result = MoveResults.DRAW; 
		 } else {
			 if (winner.equals(player.name())) this.result = MoveResults.WINNING_MOVE;
			 else this.result = MoveResults.LOSING_MOVE;
		 }
	  }
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
}
