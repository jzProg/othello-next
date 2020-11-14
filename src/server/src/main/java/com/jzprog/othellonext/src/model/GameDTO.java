package com.jzprog.othellonext.src.model;

import org.springframework.stereotype.Component;

@Component
public class GameDTO {
	private int gameId;
	private String gameMessage;
	private int moveX;
	private int moveY;
	private String playerColor;
	
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

	@Override
	public String toString() {
		return "GameDTO [gameId=" + gameId + ", gameMessage=" + gameMessage + ", moveX=" + moveX + ", moveY=" + moveY
				+ ", playerColor=" + playerColor + "]";
	}	
}
