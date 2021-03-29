package org.transparent.lucent.transform;

import com.sun.tools.javac.tree.JCTree.*;

/**
 * A default implementation of {@link LucentValidator} so
 * that specific methods can be overridden instead of all of them.
 * <p>
 * All methods in this class automatically return {@code true}.
 *
 * @author Maow
 * @version %I%
 * @since 1.0.0
 */
public class LucentBaseValidator implements LucentValidator {
    /** {@inheritDoc} */
    @Override
    public boolean validateClass(JCClassDecl clazz) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean validateField(JCVariableDecl field) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean validateMethod(JCMethodDecl method) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean validateParameter(JCVariableDecl parameter) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean validateLocal(JCVariableDecl local) {
        return true;
    }
}