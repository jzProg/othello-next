package com.jzprog.othellonext.src.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jzprog.othellonext.src.model.Action;
import com.jzprog.othellonext.src.model.StateInfo;

import static com.jzprog.othellonext.src.utils.SystemMessages.TileStates.*;

@Component
public class AITurnState implements GameState {
	
	@Autowired
	private MinMaxSearch minMax;

	@Override
	public void makeMove(GameService game) {
		StateInfo info = new StateInfo(); // TODO fill object
		Action AIMove = minMax.makeDecision(info);
		game.setCurrentMove(AIMove);
		game.putDisc(game.getPlayer().equals(BLACK) ? WHITE : BLACK);
	}

	@Override
	public void next(GameService game) {
		GameState nextState = game.isTerminal() ?  new EndState() : new PlayerTurnState();
		game.setGameState(nextState);	
	}

}
