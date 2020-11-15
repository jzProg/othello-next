package com.jzprog.othellonext.src.services;

import org.springframework.stereotype.Component;

@Component
public class AITurnState implements GameState {

	@Override
	public void makeMove(GameService game) {
		game.putDisc();
		game.checkNextPlayerAvailability();
	}

	@Override
	public void next(GameService game) {
		GameState nextState = game.isTerminal() ?  new EndState() : new PlayerTurnState();
		game.setGameState(nextState);	
	}
}
