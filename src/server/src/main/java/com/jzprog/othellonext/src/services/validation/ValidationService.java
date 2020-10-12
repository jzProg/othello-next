package com.jzprog.othellonext.src.services.validation;

import com.jzprog.othellonext.src.model.Validatable;
import com.jzprog.othellonext.src.utils.SystemMessages.ValidationTypes;

public interface ValidationService {
	
	boolean validate(Validatable object, Object... extraInfo);
	String getErrorMessage();
    ValidationTypes getType();
}
