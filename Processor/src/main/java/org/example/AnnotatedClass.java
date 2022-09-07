package org.example;

import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.io.IOException;

public class AnnotatedClass {
    private NamesStr id;
    String canonicalName;
    String simpleName;
    private TypeElement classElement;
    public AnnotatedClass(@NotNull TypeElement classElement) throws IOException {
        this.id = classElement.getAnnotation(Strategies.class).idStr();
        this.classElement = classElement;
        if (id == null) {
            throw new IOException("Id is incorrect");
        }
        try {
            Class clas = classElement.getAnnotation(Strategies.class).type();
            canonicalName = clas.getCanonicalName();
            simpleName = clas.getSimpleName();
        } catch (MirroredTypeException exception) {
            canonicalName = ((TypeElement) ((DeclaredType) exception.getTypeMirror()).asElement()).getQualifiedName().toString();
            simpleName = ((TypeElement) ((DeclaredType) exception.getTypeMirror()).asElement()).getSimpleName().toString();
        }
    }
    public NamesStr getId(){
        return id;
    }
    public String getCanonicalName(){
        return canonicalName;
    }
    public String getSimpleName(){
        return simpleName;
    }
    public TypeElement getClassElement(){
        return classElement;
    }
}
