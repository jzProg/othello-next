package com.jzprog.othellonext.src.model;

import org.springframework.stereotype.Component;

@Component
public class StateDTO {
	
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

	@Override
	public String toString() {
		return "StateDTO [moveX=" + moveX + ", moveY=" + moveY + ", playerColor=" + playerColor + "]";
	}  
	
	
}
