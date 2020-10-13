package com.jzprog.othellonext.src.services.validation;

import org.springframework.stereotype.Service;

import com.jzprog.othellonext.src.model.Validatable;
import com.jzprog.othellonext.src.utils.SystemMessages;
import com.jzprog.othellonext.src.utils.SystemMessages.ValidationTypes;

@Service
public class ResetValidator implements ValidationService {

	@Override
	public boolean validate(Validatable object, Object... extraInfo) {
		return true;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public ValidationTypes getType() {
		return SystemMessages.ValidationTypes.RESET_GAME_VALIDATION;
	}

}
