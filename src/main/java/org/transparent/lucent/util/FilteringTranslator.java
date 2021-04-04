package org.transparent.lucent.util;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeTranslator;
import org.transparent.lucent.transform.LucentTranslator;

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
    private final Set<String> annotations;
    private final LucentTranslator translator;

    public FilteringTranslator(
            Set<Class<? extends Annotation>> annotations,
            LucentTranslator translator) {
        this.annotations = annotations
                .stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toSet());
        this.translator = translator;
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
        if (isAnnotated(clazz))
            clazz.accept(translator);
    }

    private void filterMember(JCTree tree) {
        if (isAnnotated(tree))
            tree.accept(translator);
    }

    private boolean isAnnotated(JCTree tree) {
        JCModifiers mods = null;
        if (tree instanceof JCClassDecl)
            mods = ((JCClassDecl) tree).mods;
        else if (tree instanceof JCVariableDecl)
            mods = ((JCVariableDecl) tree).mods;
        else if (tree instanceof JCMethodDecl)
            mods = ((JCMethodDecl) tree).mods;
        if (mods != null) return isAnnotated(mods);
        return false;
    }

    private boolean isAnnotated(JCModifiers mods) {
        if (mods.annotations.isEmpty()) return false;
        return mods.annotations.stream().anyMatch(annotation ->
                annotations.contains(
                        annotation.type.tsym
                                .getQualifiedName()
                                .toString()
                ));
    }
}