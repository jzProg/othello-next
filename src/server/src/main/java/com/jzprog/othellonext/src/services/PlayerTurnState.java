package com.jzprog.othellonext.src.services;

import org.springframework.stereotype.Component;

import com.jzprog.othellonext.src.advices.LogMethodInfo;

@Component
public class PlayerTurnState implements GameState {

	@Override
	@LogMethodInfo
	public void makeMove(GameService game) {
		game.putDisc();
	}

	@Override
	public void next(GameService game) {
		GameState nextState = game.isTerminal() ?  new EndState() : new AITurnState();
		game.setGameState(nextState);	
	}
}
