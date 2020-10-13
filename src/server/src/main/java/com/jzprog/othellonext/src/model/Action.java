package com.jzprog.othellonext.src.model;

import org.springframework.stereotype.Component;

//represents a 2D point
@Component
public class Action {
	
	private int x; // x coordinate for row
	private int y; // y coordinate for column
	
	public Action(){}
	
	public Action(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	@Override
	public String toString() {
		return "Action [x=" + x + ", y=" + y + "]";
	}	
}
