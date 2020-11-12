package com.jzprog.othellonext.src.services;

import org.springframework.stereotype.Component;

@Component
public class PlayerTurnState implements GameState {

	@Override
	public void makeMove(GameService game) {
		game.putDisc();
	}

	@Override
	public void next(GameService game) {
		GameState nextState = game.isTerminal() ?  new EndState() : new AITurnState();
		game.setGameState(nextState);	
	}
}
