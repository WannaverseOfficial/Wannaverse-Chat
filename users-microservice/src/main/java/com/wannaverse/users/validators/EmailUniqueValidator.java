package com.wannaverse.users.validators;

import com.wannaverse.users.annotations.EmailUnique;
import com.wannaverse.users.controllers.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userService.isEmailAddressInUse(email);
    }
}
