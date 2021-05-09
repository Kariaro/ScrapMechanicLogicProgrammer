package com.hardcoded.util;

import java.lang.annotation.*;

/**
 * When this annotation is used on a method it says that the
 * returned value of the method will never be {@code null}.
 * 
 * @author HardCoded
 * @sinnce v0.1
 */
@Documented
@Target(ElementType.METHOD)
public @interface NotNull {
	
}
