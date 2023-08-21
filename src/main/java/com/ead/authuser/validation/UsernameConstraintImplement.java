package com.ead.authuser.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameConstraintImplement implements ConstraintValidator<UsernameConstraint, String> {

    @Override
    public void initialize(UsernameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // trim é para remover os espaços em branco, username.contains(" ")  verifica se tem algum espaço em branco
        // entre os nomes, por exemplo "Lucas Santos" tem um espaço entre o nome e o sobrenome, isso não pode ocorrer.
        if (username == null || username.trim().isEmpty() || username.contains(" ")) {
            return false;
        }
        return true;
    }
}
