package com.jzprog.othellonext.src.services;

import org.springframework.stereotype.Component;

@Component
public class AITurnState implements GameState {

	@Override
	public void makeMove(GameService game) {
		game.putDisc();
	}

	@Override
	public void next(GameService game) {
		boolean hasNormalTransition = game.checkNextPlayerAvailability();
		GameState nextState = hasNormalTransition ?  new PlayerTurnState() : new AITurnState();
		if (game.isTerminal()) {
			nextState = new EndState();
		}
		game.setGameState(nextState);	
	}
}
