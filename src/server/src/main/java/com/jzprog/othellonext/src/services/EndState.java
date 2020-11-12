package com.jzprog.othellonext.src.services;

import org.springframework.stereotype.Component;

@Component
public class EndState implements GameState {

	@Override
	public void makeMove(GameService game) {
		// TODO show post game modal
		
	}

	@Override
	public void next(GameService game) {
		GameState nextState = new InitialState();
		game.setGameState(nextState);
	}
}
