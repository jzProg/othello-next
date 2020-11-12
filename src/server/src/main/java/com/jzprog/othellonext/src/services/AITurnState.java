package com.jzprog.othellonext.src.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jzprog.othellonext.src.model.Action;

@Component
public class AITurnState implements GameState {
	
	@Autowired
	private MinMaxSearch minMax;

	@Override
	public void makeMove(GameService game) {
		Action AIMove = minMax.makeDecision(game.getInfo());
		game.setCurrentMove(AIMove);
		game.putDisc();
	}

	@Override
	public void next(GameService game) {
		GameState nextState = game.isTerminal() ?  new EndState() : new PlayerTurnState();
		game.setGameState(nextState);	
	}
}
