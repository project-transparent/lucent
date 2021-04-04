package org.transparent.lucent.util;

import java.util.function.Function;

/**
 * A functional interface returning a type from three parameters.
 *
 * @author Maow
 * @version %I%
 * @param <T> first parameter
 * @param <U> second parameter
 * @param <V> third parameter
 * @param <W> type to return
 * @see Function
 * @since 1.0.0
 */
@FunctionalInterface
public interface TriFunction<T, U, V, W> {
    W apply(T t, U u, V v);
}