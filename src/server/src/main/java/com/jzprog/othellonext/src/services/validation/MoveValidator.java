package com.jzprog.othellonext.src.services.validation;

import org.springframework.stereotype.Service;

import com.jzprog.othellonext.src.model.Action;
import com.jzprog.othellonext.src.model.Validatable;
import com.jzprog.othellonext.src.services.GameState;
import com.jzprog.othellonext.src.services.PlayerTurnState;
import com.jzprog.othellonext.src.utils.SystemMessages;
import com.jzprog.othellonext.src.utils.SystemMessages.ValidationTypes;

@Service
public class MoveValidator implements ValidationService {

	@Override
	public boolean validate(Validatable object, Object... extraInfo) {
		return ((GameState)object) instanceof PlayerTurnState && extraInfo != null && isValidMove(((Action)extraInfo[0]), ((SystemMessages.TileStates[][])extraInfo[1]));
	}

	@Override
	public String getErrorMessage() {
		return SystemMessages.INVALID_MOVE_ERROR;
	}

	@Override
	public ValidationTypes getType() {
		return SystemMessages.ValidationTypes.MOVE_VALIDITY;
	}
	
	private boolean isValidMove(Action move, SystemMessages.TileStates[][] gameBoard) {
		// TODO implement
		return true;
	}

}