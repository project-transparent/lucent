package org.transparent.lucent.processor;

import javax.annotation.processing.RoundEnvironment;
import java.lang.annotation.*;

/**
 * Marks a method as being a {@link LucentProcessor} hook.
 * <p>
 * This methods are called during different stages of the {@link LucentProcessor#process(RoundEnvironment)} method
 * to give developers more control over processing, as well as allowing for error reporting.
 * <p>
 * This annotation is purely metadata meant to differentiate annotated methods from other methods in the class.
 *
 * @author Maow
 * @version %I%
 * @since 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Hook {
}