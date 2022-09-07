package org.example;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javawriter.JavaWriter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.io.Writer;
import java.util.EnumSet;
import java.util.LinkedHashMap;

public class GroupClasses {
    String uniqueName;
    String sufix="Factory";
    public GroupClasses(String uniqueName){
        this.uniqueName=uniqueName;
    }
    LinkedHashMap<NamesStr,AnnotatedClass> classLinkedHashMap =new LinkedHashMap<>();
    public void add(AnnotatedClass annotatedClass) throws Exception {
        if(classLinkedHashMap.get(annotatedClass.getId())!=null){
            throw new Exception("Class was already created");
        }
        classLinkedHashMap.put(annotatedClass.getId(),annotatedClass);
    }
    public void generateCode(Filer filler, Elements elements) throws IOException {
        TypeElement fatherClassName=elements.getTypeElement(uniqueName);
        String NameClass=sufix+fatherClassName.getSimpleName();
        PackageElement packageElement=elements.getPackageOf(fatherClassName);
        MethodSpec.Builder method = MethodSpec.methodBuilder("execStrategy")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(NamesStr.class, "strategy")
                .returns(TypeName.get(fatherClassName.asType()));
        method.beginControlFlow("if (strategy == null)")
                .addStatement("throw new IllegalArgumentException($S)", "Id is invalid!")
                .endControlFlow();


        for (AnnotatedClass item : classLinkedHashMap.values()) {
            method.beginControlFlow("if (NamesStr."+item.getId()+".equals(strategy))")
                    .addStatement("return new $L()", item.getClassElement().getQualifiedName().toString())
                    .endControlFlow();
        }
        method.addStatement("throw new IllegalArgumentException($S + strategy)", "Unknown strategy ");

        TypeSpec typeSpec = TypeSpec.classBuilder(NameClass).addMethod(method.build()).build();
        String packName = packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
        JavaFile.builder(packName, typeSpec).build().writeTo(filler);

        MethodSpec.Builder methodForAnotherClass=MethodSpec.methodBuilder("main").
                addModifiers(Modifier.PUBLIC,Modifier.STATIC).
                returns(void.class).addParameter(String[].class,"args").
                addStatement("Item item=new Item($S,5000)","SomeItem").
                addStatement("IStrategy istrategy=FactoryIStrategy.execStrategy(NamesStr.SubstractionCost)").
                addStatement("istrategy.execOperation(item)");
        TypeSpec typeSpec1=TypeSpec.classBuilder("Main").addMethod(methodForAnotherClass.build()).build();
        JavaFile.builder(packName,typeSpec1).build().writeTo(filler);
        /**
        Writer writer=filler.createClassFile(uniqueName+sufix).openWriter();
        JavaWriter javaWriter=new JavaWriter(writer);
        javaWriter.emitPackage("org.example");
        javaWriter.beginType(NameClass,"class", EnumSet.of(Modifier.PUBLIC));
        javaWriter.emitEmptyLine();
        javaWriter.emitEmptyLine();
        javaWriter.beginMethod(uniqueName,"execStrategy",EnumSet.of(Modifier.PUBLIC),"NamesStr","id","Item...","items");
        javaWriter.beginControlFlow("if (id == null)");
        javaWriter.emitStatement("throw new IOException(\"Id is incorrect\");");
        javaWriter.endControlFlow();
        for(AnnotatedClass annotatedClass: classLinkedHashMap.values()){
            javaWriter.emitEmptyLine();
            javaWriter.beginControlFlow("if(id==NameStr.%s)",annotatedClass.getId());
            javaWriter.emitStatement("return new %s()",annotatedClass.getClassElement().getQualifiedName().toString());
            javaWriter.endControlFlow();
        }
        javaWriter.emitEmptyLine();
        javaWriter.endMethod();
        javaWriter.endType();
        javaWriter.close();
         **/

    }
}
