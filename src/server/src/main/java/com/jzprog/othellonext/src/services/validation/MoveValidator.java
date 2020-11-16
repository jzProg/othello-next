package com.jzprog.othellonext.src.services.validation;

import org.springframework.stereotype.Service;

import com.jzprog.othellonext.src.advices.LogMethodInfo;
import com.jzprog.othellonext.src.model.Action;
import com.jzprog.othellonext.src.model.StateInfo;
import com.jzprog.othellonext.src.model.Validatable;
import com.jzprog.othellonext.src.services.AITurnState;
import com.jzprog.othellonext.src.services.GameState;
import com.jzprog.othellonext.src.services.PlayerTurnState;
import com.jzprog.othellonext.src.utils.SystemMessages;
import com.jzprog.othellonext.src.utils.SystemMessages.TileStates;
import com.jzprog.othellonext.src.utils.SystemMessages.ValidationTypes;

@Service
public class MoveValidator implements ValidationService {
	
	private String errorMessage = SystemMessages.INVALID_MOVE_ERROR;

	@Override
	public boolean validate(Validatable object, Object... extraInfo) {
		return ((((GameState)object) instanceof PlayerTurnState && ((StateInfo)extraInfo[1]).getPlayerToMove().equals(((TileStates)extraInfo[2]).name())) 
				|| (((GameState)object) instanceof AITurnState && !(((StateInfo)extraInfo[1]).getPlayerToMove().equals(((TileStates)extraInfo[2]).name()))))
				&& extraInfo != null 
				&& isValidMove(((Action)extraInfo[0]), ((StateInfo)extraInfo[1]));
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public ValidationTypes getType() {
		return SystemMessages.ValidationTypes.MOVE_VALIDITY;
	}
	
	private void setErrorMessage(String message) {
		errorMessage = message;
	}
	
	@LogMethodInfo
	private boolean isValidMove(Action move, StateInfo state) {
	  if(state.getUtility() == -1 && (move == null || !state.canPlay(move.getPlayerToMove().name()) )){  // if current player is unable to play
	    setErrorMessage(SystemMessages.NO_AVAILABLE_MOVE_ERROR);
	    return false;
	  } else if (state.getUtility() == -1 && state.isEmptySquare(move.getX(), move.getY())) {	    
	     boolean check = state.validateMove(move);
	     if (!check) {
		  setErrorMessage(SystemMessages.NON_EMPTY_MOVE_ERROR);
		  state.analyzeUtility();
		  return false;
		 }
	  }
	  return true;
	}

}