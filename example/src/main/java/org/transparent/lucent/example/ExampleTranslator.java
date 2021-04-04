package org.transparent.lucent.example;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import org.transparent.lucent.transform.LucentTranslator;
import org.transparent.lucent.transform.LucentValidator;

import javax.lang.model.element.Element;

public final class ExampleTranslator extends LucentTranslator {
    public ExampleTranslator(Names names, TreeMaker factory, LucentValidator validator) {
        super(names, factory, validator);
    }

    @Override
    public void visitClassDef(JCClassDecl tree) {
        super.visitClassDef(tree);
        tree.defs = tree.defs
                .append(field());
        result = tree;
    }

    private JCVariableDecl field() {
        final JCModifiers mods = factory.Modifiers(
                Flags.PRIVATE | Flags.FINAL);
        final Name name = names.fromString("generated");
        final JCExpression type = factory.Ident(
                names.fromString("String"));
        final JCExpression init = factory.Literal("A generated string.");
        return factory.VarDef(mods, name, type, init);
    }
}
