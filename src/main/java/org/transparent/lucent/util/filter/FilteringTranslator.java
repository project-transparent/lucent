package org.transparent.lucent.util.filter;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeTranslator;
import org.transparent.lucent.transform.LucentTranslator;
import org.transparent.lucent.transform.LucentValidator;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This implementation of a {@link TreeTranslator} stores a {@link LucentTranslator}
 * and filters through annotated elements so that manual filtering doesn't need to be done.
 *
 * @author Maow
 * @version %I%
 * @since 1.0.0
 */
// TODO: Document methods
public final class FilteringTranslator extends TreeTranslator {
    protected final LucentTranslator translator;
    protected final LucentValidator validator;

    public FilteringTranslator(
            LucentTranslator translator,
            LucentValidator validator) {
        this.translator = translator;
        this.validator = validator;
    }

    public void filter(JCTree tree) {
        if (tree instanceof JCClassDecl) {
            final JCClassDecl clazz = (JCClassDecl) tree;
            filterClass(clazz);
            for (JCTree def : clazz.defs)
                filterMember(def);
        }
    }

    private void filterClass(JCClassDecl clazz) {
        if (validator.validateClass(clazz))
            clazz.accept(translator);
    }

    private void filterMember(JCTree tree) {
        if (validator.validate(tree))
            tree.accept(translator);
    }
}