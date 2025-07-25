package com.challang.backend.auth.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
public @interface CurrentUser {
}
