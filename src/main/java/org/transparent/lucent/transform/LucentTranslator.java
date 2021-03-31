package org.transparent.lucent.transform;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import org.transparent.lucent.processor.LucentProcessor;

import javax.lang.model.element.Element;

/**
 * Allows the filtering and transformation of {@link JCTree} instances.
 * <p>
 * These are usually instantiated by {@link LucentProcessor} and are called when a valid element
 * has been processed and is able to be transformed.
 * <p>
 * The processor's filtering is lightweight however, and as such, this class provides access to a
 * {@link LucentValidator} which allows individual types of {@code JCTree}s to be validated based on the
 * implementation of the validator.
 *
 * @author Maow
 * @version %I%
 * @see LucentBaseValidator
 * @since 1.0.0
 */
public abstract class LucentTranslator extends TreeTranslator {
    protected LucentValidator validator;
    protected final Names names;
    protected final TreeMaker factory;

    /**
     * Instantiates the validator as well as provides
     * access to the processor's {@link Names} and {@link TreeMaker}.
     *
     * @param names an instance of {@code Names} that can be used to get instances of {@link Name}
     * @param factory an instance of {@code TreeMaker} that allows for transformation of trees
     */
    public LucentTranslator(Names names, TreeMaker factory) {
        this.validator = getValidator();
        this.names = names;
        this.factory = factory;
    }

    /**
     * Returns the validator associated with this translator.
     * <p>
     * If necessary, it's possible to use this to have
     * dynamic validation set up for your processor as the {@link #validator}
     * field of all translators is mutable.
     *
     * @return the associated {@link LucentValidator}
     */
    protected LucentValidator getValidator() {
        return null;
    }

    /**
     * Where the {@link JCTree} transformations occur.
     *
     * @param tree the target {@code JCTree}
     * @param element the {@link Element} associated with this tree
     */
    public abstract void translate(JCTree tree, Element element);
}

