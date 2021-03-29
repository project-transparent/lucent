package org.transparent.lucent.processor;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import org.transparent.lucent.transform.LucentTranslator;
import org.transparent.lucent.transform.LucentValidator;
import org.transparent.lucent.util.TypeKind;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A wrapper around {@link AbstractProcessor} that allows for cleaner AST modification.
 * <p>
 * This processor will process root elements,
 * skip the ones that aren't supported by the processor,
 * and invoke {@link LucentTranslator#translate(JCTree, Element)} on them.
 * <p>
 * It is up to the associated {@link LucentTranslator}
 * specified by {@link #getTranslator()} to filter and transform trees obtained from
 * the processor.
 *
 * @author Maow
 * @version %I%
 * @see LucentValidator
 * @see Trees
 * @see Context
 * @since 1.0.0
 */
public abstract class LucentProcessor extends AbstractProcessor {
    private final Set<TypeKind> kinds;
    private LucentTranslator translator;

    protected Trees trees;
    protected Context context;
    protected TreeMaker factory;
    protected Names names;

    /**
     * Initializes all per-processor values.
     */
    public LucentProcessor() {
        kinds = getSupportedTypeKinds();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        trees = Trees.instance(env);
        context = ((JavacProcessingEnvironment) env)
                .getContext();
        factory = TreeMaker.instance(context);
        names = Names.instance(context);
        translator = getTranslator();
    }

    /**
     * <b>This will only process if at least one annotation is found.</b>
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (annotations.size() > 0)
            return process(env);
        return false;
    }

    /**
     * Goes through root elements supplied by the {@link RoundEnvironment}
     * and filters them via the supported type kinds, then sends them and their {@link JCTree} to the
     * associated {@link LucentTranslator}.
     * <p>
     * Calls additional {@link Hook}-annotated methods per stage that can be overridden if necessary.
     *
     * @param env an instance of {@code RoundEnvironment} supplied by the annotation processing framework
     * @return a boolean stating whether or not these annotation types are claimed by this processor
     */
    protected boolean process(RoundEnvironment env) {
        process();
        final Set<? extends Element> elements =
                env.getRootElements();
        for (Element element : elements) {
            if (supported(element.getKind())) {
                final JCTree tree = (JCTree) trees.getTree(element);
                preTranslate(tree, element, translator);
                if (translator != null)
                    translator.translate(tree, element);
                postTranslate(tree, element, translator);
                continue;
            }
            notSupported(element);
        }
        return false;
    }

    /**
     * Called when {@link #process(RoundEnvironment)} is called.
     */
    @Hook
    protected void process() {}

    /**
     * Called after an instance of {@link JCTree} has been acquired but before it has been translated.
     *
     * @param tree an instance of {@code JCTree} associated with the element
     * @param element the current root element
     * @param translator this processor's instance of {@link LucentTranslator}
     */
    @Hook
    protected void preTranslate(JCTree tree, Element element, LucentTranslator translator) {}

    /**
     * Called after an instance of {@link JCTree} has been translated.
     *
     * @param tree an instance of {@code JCTree} associated with the element
     * @param element the current root element
     * @param translator this processor's instance of {@link LucentTranslator}
     */
    @Hook
    protected void postTranslate(JCTree tree, Element element, LucentTranslator translator) {}

    /**
     * Called when an element's kind doesn't match {@link #getSupportedTypeKinds()}
     *
     * @param element the invalid element
     */
    @Hook
    protected void notSupported(Element element) {}

    /**
     * Returns the type kinds that this processor supports.
     * <p>
     * Any non-matching types are discarded during processing.
     *
     * @return a set of supported type kinds
     */
    public Set<TypeKind> getSupportedTypeKinds() {
        return Collections.singleton(TypeKind.CLASS);
    }

    /**
     * Called by {@link #process(RoundEnvironment)} to confirm validity of an element's kind.
     * <p>
     * This method has to convert the element kind to a type kind to check equality.
     *
     * @param kind the element kind of a root element
     * @return whether or not this element kind is supported
     */
    private boolean supported(ElementKind kind) {
        final TypeKind type = TypeKind
                .valueOf(kind)
                .orElse(null);
        if (type != null)
            return kinds
                    .contains(type);
        return false;
    }

    /**
     * Returns the associated {@link LucentTranslator} for this processor.
     * <p>
     * This method returns {@code null} by default in case this processor
     * doesn't perform any transformations.
     * If the translator is null, transforming will be skipped entirely.
     * <p>
     * The translator handles the walking and transformation of
     * valid elements found by the processor.
     *
     * @return an instance of {@code LucentTranslator}
     */
    public LucentTranslator getTranslator() {
        return null;
    }

    /**
     * Returns an instance of {@link LucentTranslator} based on the fields of this processor.
     * <p>
     * This method isn't preferable if you add more parameters to the constructor of your translator,
     * as it is meant to serve as a utility method for {@link #getTranslator()}.
     *
     * @param constructor a functional interface representing the constructor of a {@code LucentTranslator}
     * @return an instance of {@code LucentTranslator}
     */
    protected LucentTranslator translator(
            BiFunction<Names, TreeMaker,
                    LucentTranslator> constructor) {
        return constructor.apply(names, factory);
    }

    /**
     * Returns a single supported annotation.
     * <p>
     * This or {@link #getSupportedAnnotations()} must be overridden as this method returns null by default.
     *
     * @return a {@code Class} representing the supported annotation
     * @see Processor#getSupportedAnnotationTypes()
     */
    public Class<? extends Annotation> getSupportedAnnotation() {
        return null;
    }

    /**
     * Returns a {@code Set} of supported annotations.
     * <p>
     * This or {@link #getSupportedAnnotation()} must be overridden
     * as {@code getSupportedAnnotation()} returns null by default.
     * <p>
     * If this method is not overridden, it returns a singleton set
     * containing the annotation returned by {@code getSupportedAnnotation()}
     *
     * @return a {@code Set} of {@code Class}es representing the supported annotations
     * @see Processor#getSupportedAnnotationTypes()
     */
    public Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(
                getSupportedAnnotation());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return getSupportedAnnotations()
                .stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toSet());
    }
}