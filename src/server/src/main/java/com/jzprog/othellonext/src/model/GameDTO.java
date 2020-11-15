package com.jzprog.othellonext.src.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;

@Component
public class GameDTO {
	private int gameId;
	private String gameMessage;
	private int moveX;
	private int moveY;
	private String playerColor;
	private String playerToMove;
	private TileStates[][] board;
	private List<Action> availableMoves;
	private List<Integer> score;
	
	public int getMoveX() {
		return moveX;
	}
	
	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}
	
	public int getMoveY() {
		return moveY;
	}
	
	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}

	public String getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(String playerColor) {
		this.playerColor = playerColor;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameMessage() {
		return gameMessage;
	}

	public void setGameMessage(String gameMessage) {
		this.gameMessage = gameMessage;
	}

	public String getPlayerToMove() {
		return playerToMove;
	}

	public void setPlayerToMove(String playerToMove) {
		this.playerToMove = playerToMove;
	}

	public TileStates[][] getBoard() {
		return board;
	}

	public void setBoard(TileStates[][] board) {
		this.board = board;
	}

	public List<Action> getAvailableMoves() {
		return availableMoves;
	}

	public void setAvailableMoves(List<Action> availableMoves) {
		this.availableMoves = availableMoves;
	}

	public List<Integer> getScore() {
		return score;
	}

	public void setScore(List<Integer> score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "GameDTO [gameId=" + gameId + ", gameMessage=" + gameMessage + ", moveX=" + moveX + ", moveY=" + moveY
				+ ", playerColor=" + playerColor + ", playerToMove=" + playerToMove + ", board="
				+ Arrays.toString(board) + ", availableMoves=" + availableMoves + ", score=" + score + "]";
	}
}
