package org.transparent.lucent.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public final class CollectionsUtil {
    private CollectionsUtil() {}

    @SafeVarargs
    public static <T> Set<T> hashSetOf(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    @SafeVarargs
    public static <T> Set<T> linkedHashSetOf(T... elements) {
        return new LinkedHashSet<>(Arrays.asList(elements));
    }
}
