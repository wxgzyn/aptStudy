package com.luck.wxg.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.luck.wxg.annotation.TestAnnotation"})
public class TestProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
// 创建方法
        MethodSpec main = MethodSpec.methodBuilder("onClick")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(String[].class,"args")
                .addStatement("$T.out.println($S)",System.class,"Hello JavaPoet")
                .build();
        //创建类
        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWord")
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addMethod(main)
                .build();

        //创建Java文件
        JavaFile file = JavaFile.builder("com.ecample.test", typeSpec)
                .build();

        try {
            file.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
