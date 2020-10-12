package com.jzprog.othellonext.src.services.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jzprog.othellonext.src.advices.LogMethodInfo;
import com.jzprog.othellonext.src.model.Validatable;
import com.jzprog.othellonext.src.model.ValidationResponse;

import com.jzprog.othellonext.src.utils.SystemMessages.ValidationTypes;

@Service
public class ValidationStrategy {
	
	private Map<ValidationTypes, ValidationService> validatorMap;
	
	@Autowired
	public ValidationStrategy(List<ValidationService> validators) {
		validatorMap = new HashMap<>();
		validators.forEach(validator -> validatorMap.put(validator.getType(), validator));
	}
	
	@LogMethodInfo
	public ValidationResponse provideValidation(ValidationTypes type, Validatable object, Object... extraInfo) {
		boolean result = true;
		String message = null;
		ValidationService validator = validatorMap.get(type);
		if (!validator.validate(object, extraInfo)) {
			result = false;
			message = validator.getErrorMessage();
		}
		return new ValidationResponse(result, message);
	}
}
