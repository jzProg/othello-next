package com.jzprog.othellonext.src.model;

import org.springframework.stereotype.Component;

import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;

//represents a 2D point
@Component
public class Action {
	
	private int x; // x coordinate for row
	private int y; // y coordinate for column
	private TileStates playerToMove;
	
	public Action(){}
	
	public Action(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public Action(int x,int y, TileStates player){
		this.x = x;
		this.y = y;
		playerToMove = player;
	}
	
	public TileStates getPlayerToMove() {
		return playerToMove;
	}

	public void setPlayerToMove(TileStates playerToMove) {
		this.playerToMove = playerToMove;
	}

	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	@Override
	public String toString() {
		return "Action [x=" + x + ", y=" + y + " player=" + playerToMove + "]";
	}	
}
