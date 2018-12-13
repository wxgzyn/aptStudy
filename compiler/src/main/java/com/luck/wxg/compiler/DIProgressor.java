package com.luck.wxg.compiler;

import com.google.auto.service.AutoService;
import com.luck.wxg.annotation.DIActivity;
import com.luck.wxg.annotation.DIView;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.luck.wxg.annotation.DIActivity"})
public class DIProgressor extends AbstractProcessor {
    private Elements elementUtils;
    private Types typesUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typesUtils = processingEnvironment.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null) {
            Set<? extends Element> elementsAnnotationWith = roundEnvironment.getElementsAnnotatedWith(DIActivity.class);
            if (elementsAnnotationWith != null) {
                TypeElement typeElement = elementUtils.getTypeElement("android.app.Activity");
                for (Element element: elementsAnnotationWith) {
                    TypeMirror typeMirror = element.asType();
                    DIActivity annotation = element.getAnnotation(DIActivity.class);
                    System.out.print("@@@@@@@@@@@@@@@@@@@" + typeMirror);
                    if (typesUtils.isSubtype(typeMirror, typeElement.asType())) {
                        TypeElement classElement = (TypeElement) element;
                        //参数名
                        System.out.println("``````````````````");
                        ParameterSpec atlas = ParameterSpec.builder(ClassName.get(typeMirror), "activity").build();
                        //创建方法
                        MethodSpec.Builder method = MethodSpec.methodBuilder("findById")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .returns(TypeName.VOID)
                                .addParameter(atlas);
                        //创建函数体
                        List<? extends Element> allMembers = elementUtils.getAllMembers(classElement);
                        for (Element member: allMembers) {
                            DIView diView = member.getAnnotation(DIView.class);
                            if (diView == null) {
                                continue;
                            }

                            method.addStatement(String.format("activity.%s = (%s) activity.findViewById(%s)",
                                    member.getSimpleName(),//注解节点变量的名称
                                    ClassName.get(member.asType()).toString(),//注解节点变量的类型
                                    diView.value()));//注解的值
                            System.out.print("method:::::::::" + member.getSimpleName());
                        }
                        //创建类
                        TypeSpec typeSpec = TypeSpec.classBuilder("ManagerFindBy" + element.getSimpleName())
                                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                .build();
                        //创建Javaclass 文件
                        JavaFile javaFile = JavaFile.builder("com.luck.wxg.find.by", typeSpec).build();
                        try {
                            javaFile.writeTo(processingEnv.getFiler());
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        throw new IllegalArgumentException("@DIActivity must of Activity");
                    }
                }
            }
            return true;
        }
        return false;
    }
}
