package com.jzprog.othellonext.src.services.validation;

import org.springframework.stereotype.Service;

import com.jzprog.othellonext.src.model.Validatable;
import com.jzprog.othellonext.src.services.GameState;
import com.jzprog.othellonext.src.services.InitialState;
import com.jzprog.othellonext.src.utils.SystemMessages;
import com.jzprog.othellonext.src.utils.SystemMessages.ValidationTypes;

@Service
public class SelectTurnValidator implements ValidationService {

	@Override
	public boolean validate(Validatable object, Object... extraInfo) {
		return ((GameState)object) instanceof InitialState;
	}

	@Override
	public String getErrorMessage() {
		return SystemMessages.SELECT_TURN_INVALID_ERROR;
	}

	@Override
	public ValidationTypes getType() {
		return SystemMessages.ValidationTypes.SELECT_VAIDATION;

	}

}
