package org.transparent.lucent.util;

import org.transparent.lucent.processor.LucentProcessor;

import javax.lang.model.element.ElementKind;
import java.util.Arrays;
import java.util.Optional;

/**
 * A wrapper for {@link ElementKind} that contains only type element kinds.
 * This includes: Classes, interfaces, annotations, and enums.
 * <p>
 * This class is required by {@link LucentProcessor} as it processes root elements
 * and specifies supported types via this enum.
 *
 * @author Maow
 * @version %I%
 * @since 1.0.0
 */
public enum TypeKind {
    CLASS       (ElementKind.CLASS),
    INTERFACE   (ElementKind.INTERFACE),
    ANNOTATION  (ElementKind.ANNOTATION_TYPE),
    ENUM        (ElementKind.ENUM);

    private final ElementKind underlying;

    TypeKind(ElementKind underlying) {
        this.underlying = underlying;
    }

    /**
     * Returns a {@link ElementKind} equivalent of this {@code TypeKind}.
     * <p>
     * {@code TypeKind} contains an underlying reference to
     * different constants from {@code ElementKind} that act as the equivalent of these constants.
     *
     * @return the underlying {@link ElementKind} reference
     */
    public ElementKind getElementKind() {
        return underlying;
    }

    /**
     * Returns the equivalent {@code TypeKind} of an {@link ElementKind}
     * <p>
     * This will return null if there is no associated value.
     *
     * @param kind an element kind, must be one of a type.
     * @return an optional containing the equivalent {@code TypeKind} or null
     */
    public static Optional<TypeKind> valueOf(ElementKind kind) {
        return Arrays
                .stream(values())
                .filter(type ->
                        type.underlying == kind)
                .findFirst();
    }
}
