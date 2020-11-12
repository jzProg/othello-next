package com.jzprog.othellonext.src.services;

import java.util.Random;
import org.springframework.stereotype.Component;
import com.jzprog.othellonext.src.utils.SystemMessages;

@Component
public class InitialState implements GameState {
		
	@Override
	public void makeMove(GameService game) {
		game.initBoard();
		game.setGameId(Math.abs(new Random().nextInt()));
	}

	@Override
	public void next(GameService game) {
		GameState nextState = game.getPlayer().equals(SystemMessages.TileStates.BLACK) ?  new PlayerTurnState() : new AITurnState();
		game.setGameState(nextState);	
	}
}
