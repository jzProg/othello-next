package com.jzprog.othellonext.src.services;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class InitialState implements GameState {
	
	@Override
	public void makeMove(GameService game) {
		game.setUtility(-1);
		game.initBoard();
		game.setGameId(Math.abs(new Random().nextInt()));
	}

	@Override
	public void next(GameService game) {
		// TODO player turn or AI turn
		
	}

}
