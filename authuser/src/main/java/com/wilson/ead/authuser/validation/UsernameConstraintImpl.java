package com.wilson.ead.authuser.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameConstraintImpl implements ConstraintValidator<UsernameConstraint, String> {

	@Override
	public void initialize(UsernameConstraint constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
	
	@Override
	public boolean isValid(String usernama, ConstraintValidatorContext constraintValidatorContext) {
		
		if(usernama == null || usernama.trim().isEmpty() || usernama.contains(" ")) {
			return false;
		}
		
		return true;
	}

}
