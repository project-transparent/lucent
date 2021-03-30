package org.transparent.lucent.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// An example annotation.
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Example {
}
