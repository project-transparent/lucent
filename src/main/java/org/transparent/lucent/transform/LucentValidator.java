package org.transparent.lucent.transform;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

/**
 * A tree validator, typically instantiated by a {@link LucentTranslator} to facilitate filtering.
 * <p>
 * This class will analyze different trees from the {@code com.sun.tools.javac.tree} package
 * and decide whether or not they're considered 'valid' based on its implementation.
 * <p>
 * This is useful for when you need to do the same transformations but with slightly different validation logic.
 *
 * @author Maow
 * @version %I%
 * @see LucentBaseValidator
 * @since 1.0.0
 */
public interface LucentValidator {
    // TODO: Document
    boolean validate(JCTree tree);

    /**
     * Validates a type declaration.
     * <p>
     * This can be a class, interface, enum, or annotation.
     *
     * @param clazz a {@link JCTree} representing a type declaration
     * @return whether or not this type declaration is valid
     */
    boolean validateClass(JCClassDecl clazz);

    /**
     * Validates a field declaration.
     * <p>
     * Like {@link #validateParameter(JCVariableDecl)} and {@link #validateLocal(JCVariableDecl)},
     * the tree might need to be inspected first to confirm its type.
     *
     * @param field a {@link JCTree} representing a field declaration
     * @return whether or not this field declaration is valid
     */
    boolean validateField(JCVariableDecl field);

    /**
     * Validates a method declaration.
     *
     * @param method a {@link JCTree} representing a method declaration
     * @return whether or not this method declaration is valid
     */
    boolean validateMethod(JCMethodDecl method);

    /**
     * Validates a parameter declaration.
     * <p>
     * Like {@link #validateField(JCVariableDecl)} and {@link #validateLocal(JCVariableDecl)},
     * the tree might need to be inspected first to confirm its type.
     *
     * @param parameter a {@link JCTree} representing a parameter
     * @return whether or not this parameter is valid
     */
    boolean validateParameter(JCVariableDecl parameter);


    /**
     * Validates a local variable declaration.
     * <p>
     * Like {@link #validateField(JCVariableDecl)} and {@link #validateParameter(JCVariableDecl)},
     * the tree might need to be inspected first to confirm its type.
     *
     * @param local a {@link JCTree} representing a local variable
     * @return whether or not this local variable is valid
     */
    boolean validateLocal(JCVariableDecl local);
}