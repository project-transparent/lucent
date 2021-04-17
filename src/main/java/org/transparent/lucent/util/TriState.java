package org.transparent.lucent.util;

// TODO: Document
public enum TriState {
    TRUE,
    FALSE,
    NONE,
    ;

    public boolean isNone() {
        return this == NONE;
    }

    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    public boolean orElse(boolean or) {
        return (!isNone())
                ? toBoolean()
                : or;
    }

    public boolean toBoolean() {
        switch (this) {
            case TRUE: return true;
            case FALSE:
            case NONE:
            default:
                return false;
        }
    }

    public String toString() {
        return name().toLowerCase();
    }
}