package org.example;

import com.google.auto.service.AutoService;
import org.example.GroupClasses;

import javax.annotation.processing.*;
import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;
@AutoService(Processor.class)
@SupportedAnnotationTypes("org.example.Strategies")
@SupportedSourceVersion(SourceVersion.RELEASE_15)


public class AnnotationProcessor extends AbstractProcessor {
    Elements elements;
    Filer filer;
    LinkedHashMap<String, GroupClasses> groupClassesLinkedHashMap = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elements = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Strategies.class)) {
            TypeElement annotation=(TypeElement) element;
            AnnotatedClass annotatedClass = null;
            try {
                annotatedClass = new AnnotatedClass(annotation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            GroupClasses groupClasses = groupClassesLinkedHashMap.get(annotatedClass.canonicalName);
            if (groupClasses == null) {
                groupClasses = new GroupClasses(annotatedClass.canonicalName);
                groupClassesLinkedHashMap.put(annotatedClass.canonicalName, groupClasses);
            }
            try {
                groupClasses.add(annotatedClass);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
        for (GroupClasses groupClasses : groupClassesLinkedHashMap.values()) {
            try {
                groupClasses.generateCode(filer,elements);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            groupClassesLinkedHashMap.clear();

        }
        return true;
    }
}
