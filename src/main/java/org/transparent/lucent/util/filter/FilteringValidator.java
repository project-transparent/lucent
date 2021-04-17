package org.transparent.lucent.util.filter;

import com.sun.tools.javac.tree.JCTree;
import org.transparent.lucent.transform.LucentBaseValidator;
import org.transparent.lucent.util.State;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sun.tools.javac.tree.JCTree.*;
import static org.transparent.lucent.util.State.*;

// TODO: Document methods
public class FilteringValidator extends LucentBaseValidator {
    private final Set<String> annotations;
    private final State anyDefault;
    private final State classDefault;
    private final State methodDefault;
    private final State fieldDefault;

    public FilteringValidator(Set<Class<? extends Annotation>> annotations,
                              State anyDefault,
                              State classDefault,
                              State methodDefault,
                              State fieldDefault) {
        this.annotations = annotations
                .stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toSet());
        this.anyDefault = anyDefault;
        this.classDefault = classDefault;
        this.methodDefault = methodDefault;
        this.fieldDefault = fieldDefault;
    }

    @Override
    public boolean validate(JCTree tree) {
        if (tree instanceof JCClassDecl)
            return validateClass((JCClassDecl) tree);
        else if (tree instanceof JCMethodDecl)
            return validateMethod((JCMethodDecl) tree);
        else if (tree instanceof JCVariableDecl)
            return validateField((JCVariableDecl) tree);
        return anyDefault.orElse(false);
    }

    @Override
    public boolean validateClass(JCClassDecl clazz) {
        return classDefault
                .orElse(isAnnotated(clazz.mods));
    }

    @Override
    public boolean validateMethod(JCMethodDecl method) {
        return methodDefault
                .orElse(isAnnotated(method.mods));
    }

    @Override
    public boolean validateField(JCVariableDecl field) {
        return fieldDefault
                .orElse(isAnnotated(field.mods));
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

    public static final class Builder {
        private final Set<Class<? extends Annotation>> annotations = new HashSet<>();
        private State anyDefault = NONE;
        private State classDefault = NONE;
        private State methodDefault = NONE;
        private State fieldDefault = NONE;

        @SafeVarargs
        public final Builder annotations(Class<? extends Annotation>... annotations) {
            this.annotations.addAll(Arrays.asList(annotations));
            return this;
        }

        public Builder annotations(Set<Class<? extends Annotation>> annotations) {
            this.annotations.addAll(annotations);
            return this;
        }

        public Builder anyDefault(State anyDefault) {
            this.anyDefault = anyDefault;
            return this;
        }

        public Builder classDefault(State classDefault) {
            this.classDefault = classDefault;
            return this;
        }

        public Builder methodDefault(State methodDefault) {
            this.methodDefault = methodDefault;
            return this;
        }

        public Builder fieldDefault(State fieldDefault) {
            this.fieldDefault = fieldDefault;
            return this;
        }

        public FilteringValidator build() {
            return new FilteringValidator(
                    annotations,
                    anyDefault,
                    classDefault,
                    methodDefault,
                    fieldDefault
            );
        }
    }
}