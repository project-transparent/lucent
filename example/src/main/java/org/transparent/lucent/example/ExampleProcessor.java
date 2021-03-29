package org.transparent.lucent.example;

import com.sun.tools.javac.tree.JCTree;
import org.transparent.lucent.processor.LucentProcessor;
import org.transparent.lucent.transform.LucentTranslator;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public final class ExampleProcessor extends LucentProcessor {
    @Override
    public LucentTranslator getTranslator() {
        return translator(ExampleTranslator::new);
    }

    @Override
    protected void process() {
        System.out.println("Process");
    }

    @Override
    protected void preTranslate(JCTree tree, Element element, LucentTranslator translator) {
        System.out.println("Pre-Translate");
    }

    @Override
    protected void postTranslate(JCTree tree, Element element, LucentTranslator translator) {
        System.out.println("Post-Translate");
    }

    @Override
    protected void notSupported(Element element) {
        System.out.println("Not Supported");
    }

    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Example.class;
    }
}