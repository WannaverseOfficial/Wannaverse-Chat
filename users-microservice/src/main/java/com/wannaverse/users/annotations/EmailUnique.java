package com.wannaverse.users.annotations;

import com.wannaverse.users.validators.EmailUniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailUniqueValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUnique {
    String message() default "Email address is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
