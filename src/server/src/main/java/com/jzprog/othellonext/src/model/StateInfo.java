package com.jzprog.othellonext.src.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.jzprog.othellonext.src.advices.LogMethodInfo;
import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;

@Component
public class StateInfo implements Cloneable {
	
	private TileStates playerToMove; // player who is about to move
	private TileStates[][] othelloBoard;
	private double utility; //value depending on the winner(1:win for black,0:win for white,0.5:draw,-1:non terminal state)
	private static final int[] DIRECTION_X = { -1,  0,  1, -1, 1, -1, 0, 1 };
	private static final int[] DIRECTION_Y = { -1, -1, -1,  0, 0,  1, 1, 1 };
	private static final int[] CORNERS_X = { 0, 7, 7,  0 };
	private static final int[] CORNERS_Y = { 0, 0, 7,  7 };
	private List<Action> availableMoves;
	private Map<TileStates, Integer> score;
	
	public StateInfo(){}
	
	public StateInfo(TileStates[][] board, TileStates playerToMove, double utility) {
		othelloBoard = board;
		this.utility = utility;
		this.playerToMove = playerToMove;
		availableMoves = calculateAvailableMoves();
		score = new HashMap<>();
		score.put(TileStates.BLACK, 2);
		score.put(TileStates.WHITE, 2);
	}
	
	public TileStates[][] getOthelloBoard() {
		return othelloBoard;
	}

	public void setOthelloBoard(TileStates[][] othelloBoard) {
		this.othelloBoard = othelloBoard;
	}

	public void setPlayerToMove(TileStates playerToMove) {
		this.playerToMove = playerToMove;
	}
	
	public boolean isEmptySquare(Action action){
	  return isEmptySquare(action.getX(), action.getY());
	}

	//checks if the square in r,c position is empty
	public boolean isEmptySquare(int r,int c){
	  return othelloBoard[r][c].equals(TileStates.EMPTY);
	}
	
	public List<Action> getAvailableMoves() {
		return availableMoves;
	}

	public Map<TileStates, Integer> getScore() {
		return score;
	}

	private void updateScore(TileStates player, int valueToIncrease) {
		Integer oldValue = score.get(player);
		score.put(player, oldValue + valueToIncrease);
	}

	//returns the value of the square in r,c position
	public TileStates getSquareValue(int r, int c) {
	  return othelloBoard[r][c];
	}
	
	public List<Action> calculateAvailableMoves() {
	   List<Action> moves = new ArrayList<Action>();
	   for(int j = 0; j < 8; j++) {
		for(int i = 0; i < 8; i++) {
		  Action squareToEvaluated = new Action(i, j, playerToMove);
		  if (isEmptySquare(squareToEvaluated) && isValidMove(squareToEvaluated)) {
			 moves.add(squareToEvaluated);
		  }
		 }
	    }
		return  moves;
	}
	
	private boolean isValidMove(Action move) {
	  for (int d = 0; d < DIRECTION_X.length; d++) {
		  boolean foundDifferent = false;
		  int moveX = move.getX();
		  int moveY= move.getY();
		  for(int i = 0; i < 64; i++) {
			  moveX += DIRECTION_X[d];
	          moveY += DIRECTION_Y[d];
	          if (isOutOfBounds(moveX, moveY)) break;
	          TileStates color = getSquareValue(moveX, moveY);    
	          if (isEmptySquare(moveX, moveY)) break;
	          else if (!color.equals(move.getPlayerToMove())) foundDifferent = true;
		      else if (foundDifferent) return true;
		      else break;         
		  }
	  }
	  return false;
	}
	
	public boolean isOutOfBounds(int r,int c) {
		return (r < 0 || r >= 8) || (c < 0 || c >= 8);
	}
	
	public TileStates getOpponentColor(TileStates currentPlayer) {
		return currentPlayer.equals(TileStates.BLACK) ? TileStates.WHITE : TileStates.BLACK;
	}

	public void setSquareValue(int x, int y, String player) {
		TileStates color = TileStates.valueOf(player.toUpperCase());
		if (!isEmptySquare(x, y)) { // it's a flip
			updateScore(getOpponentColor(color), -1); // decrease opponent
		}
		othelloBoard[x][y] = color;
		updateScore(color, 1);	// increase score	 
	}
	
	public double getUtility() {
		return utility;
	}
	
	public String getPlayerToMove() {
		return playerToMove.name();
	}
	
	//creates a copy of the state
	public StateInfo clone(){
	  StateInfo cp = null;
	  try {
		cp = (StateInfo) super.clone();
		cp.othelloBoard = new TileStates[8][8];
		for(int i = 0 ; i < cp.othelloBoard.length; i++) {
		  for(int j = 0; j < cp.othelloBoard[i].length; j++) {
		    cp.othelloBoard[i][j] = othelloBoard[i][j];
		   }
		 }	
		cp.playerToMove = playerToMove;
		List<Action> clonedAvailableMoves = new ArrayList<>();
		for(Action move : availableMoves) {
			clonedAvailableMoves.add(move);
		}
		cp.availableMoves = clonedAvailableMoves;
		Map<TileStates, Integer> clonedScoreMap = new HashMap<>();
		clonedScoreMap.putAll(score);
		cp.score = clonedScoreMap;
	   } catch (CloneNotSupportedException e){
		   e.printStackTrace();
	   }
	   return cp;
	}

	//returns the number of discs the given player has played
	public int getNumberOfDiscs(String player) {
		return getScore().get(TileStates.valueOf(player));
	}
		
	//returns the number of corners the given player has captured
	public int getNumberOfCorners(String player) {
		int num = 0;
		for (int i = 0; i < CORNERS_X.length; i++) {
			int x = CORNERS_X[i];
			int y = CORNERS_Y[i];
			if (getSquareValue(x, y).equals(TileStates.valueOf(player))) {
				num++;
			}
		}
		return num;
	}
	
	//returns the number of discs close to corners the given player has played
	public int getNumberOfDiscsCloseToCorners(String player){
		int num = 0;
		for (int i = 0; i < CORNERS_X.length; i++) {
			int x = CORNERS_X[i];
			int y = CORNERS_Y[i];
			if (isEmptySquare(x, y)) {
				for (int d = 0; d < DIRECTION_X.length; d++) {
					  int moveX = x;
					  int moveY= y;
					  for(int j = 0; j < 64; j++) {
						  moveX += DIRECTION_X[d];
				          moveY += DIRECTION_Y[d];
				          if (isOutOfBounds(moveX, moveY)) break;
				          String color = getSquareValue(moveX, moveY).name();    
				          if (color.equals(player)) {
				        	  num++;
				          } 			        
					  }
				  }
			}
		}
		return num;
	}
	
	public void putDisc(Action action){
	    putDisc(action.getX(),action.getY());
	}	
	
	// puts a disc in the square in r,c position + calculate available moves for next player
	@LogMethodInfo
	public void putDisc(int r,int c){
		setSquareValue(r, c, playerToMove.name()); // play move
		applyFlips(r, c);
		analyzeUtility(); // update utility
		setNextPlayer();  // update player to move next
		availableMoves = calculateAvailableMoves(); // calculate next player's available moves
	}
	
	// checks for flips in all directions and applies them
	private void applyFlips(int r,int c) {
		List<Action> flipCandidates = new ArrayList<>();
		for (int d = 0; d < DIRECTION_X.length; d++) {
			  int moveX = r;
			  int moveY= c;
			  for(int i = 0; i < 64; i++) {
				  moveX += DIRECTION_X[d];
		          moveY += DIRECTION_Y[d];
		          if (isOutOfBounds(moveX, moveY)) break;
		          if (isEmptySquare(moveX, moveY)) break;
		          TileStates color = getSquareValue(moveX, moveY);
		          if (!color.equals(TileStates.valueOf(getPlayerToMove()))) {
		        	  Action flipCand = new Action(moveX, moveY);
		        	  flipCand.setPlayerToMove(TileStates.valueOf(getPlayerToMove()));
		        	  flipCandidates.add(flipCand);
		          } 
		          else { //equals
		        	  flip(flipCandidates);
		        	  break;
		          }
			  }
		  }
	}
	
	// apply flip to a list of candidates
	private void flip(List<Action> flipCandidates) {
		for (Action a : flipCandidates) {
		  flip(a.getX(), a.getY());
		}
	}
	
	// flips the disc in r,c position
	private void flip(int r,int c){
		setSquareValue(r, c, othelloBoard[r][c].equals(TileStates.BLACK) ? TileStates.WHITE.name() : TileStates.BLACK.name());
	}
	
	// configures player to move next
	private void setNextPlayer() {
		setPlayerToMove(getOpponentColor(playerToMove)); // next player's turn					
	}
	
	// return if the given move is valid for current player
	public boolean validateMove(Action move) {
		return availableMoves.stream().filter(o -> o.getX() == move.getX() && o.getY() == move.getY()).findFirst().isPresent();
	}
	
	// updates the utility of the state
	public void analyzeUtility(){
	  int player1Num = getNumberOfDiscs(TileStates.BLACK.name());
	  int player2Num = getNumberOfDiscs(TileStates.WHITE.name());
	  if ((player1Num + player2Num == 64) || (!canPlay(TileStates.BLACK.name())) && !canPlay(TileStates.WHITE.name())){
		if (player1Num > player2Num){
			utility = 1;
		} else if (player1Num < player2Num){
			utility = 0;
		} else {
			utility = 0.5;
		}
	  }	
	}
	
	//checks if the given player has moves to play
	public boolean canPlay(String player) {
		return !availableMoves.isEmpty();

	}

	@Override
	public String toString() {
		return "StateInfo [playerToMove=" + playerToMove + ", othelloBoard=" + Arrays.toString(othelloBoard)
				+ ", utility=" + utility + "]";
	}

}
