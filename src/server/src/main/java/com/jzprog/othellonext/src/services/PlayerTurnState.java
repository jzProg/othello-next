package com.jzprog.othellonext.src.services;

import static com.jzprog.othellonext.src.utils.SystemMessages.TileStates.BLACK;
import static com.jzprog.othellonext.src.utils.SystemMessages.TileStates.WHITE;

import org.springframework.stereotype.Component;

@Component
public class PlayerTurnState implements GameState {

	@Override
	public void makeMove(GameService game) {
		game.putDisc(game.getPlayer().equals(BLACK) ? WHITE : BLACK);
	}

	@Override
	public void next(GameService game) {
		GameState nextState = game.isTerminal() ?  new EndState() : new AITurnState();
		game.setGameState(nextState);	
	}

}
