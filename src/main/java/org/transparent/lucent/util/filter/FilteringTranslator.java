package org.transparent.lucent.util.filter;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeTranslator;
import org.transparent.lucent.transform.LucentTranslator;
import org.transparent.lucent.transform.LucentValidator;

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
    private final LucentTranslator translator;
    private final LucentValidator validator;
    private final boolean filterMembers;

    public FilteringTranslator(
            LucentTranslator translator,
            LucentValidator validator, boolean filterMembers) {
        this.translator = translator;
        this.validator = validator;
        this.filterMembers = filterMembers;
    }

    public void filter(JCTree tree) {
        if (tree instanceof JCClassDecl)
            filterClass((JCClassDecl) tree);
    }

    private void filterClass(JCClassDecl clazz) {
        if (validator.validate(clazz)) {
            clazz.accept(translator);
            if (filterMembers) {
                clazz.defs.forEach(this::filterMember);
            }
        }
    }

    private void filterMember(JCTree tree) {
        if (validator.validate(tree))
            tree.accept(translator);
    }
}