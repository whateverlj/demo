package com.example.annotation_complier;

import com.example.annotation.BindPath;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 要处理的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> activitys = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        Map<String, String> map = new HashMap<>();
        for (Element activity : activitys) {
            TypeElement typeElement = (TypeElement) activity;
            String key = typeElement.getAnnotation(BindPath.class).value();
            String activityName = typeElement.getQualifiedName().toString();
            map.put(key, activityName+".class");
        }

        if (map.size() > 0) {
            Writer mWriter = null;
            String activityName = "ActivityUtil" + System.currentTimeMillis();
            try {
                JavaFileObject sourceFile = mFiler.createSourceFile("com.router.util." + activityName);
                mWriter = sourceFile.openWriter();
                mWriter.write("package com.router.util;\n" +
                        "\n" +
                        "import com.example.router.IRouter;\n" +
                        "import com.example.router.Router;\n" +
                        "\n" +
                        "public class " + activityName + " implements IRouter {\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void putActivity() {");
                Iterator<String> iterator =map.keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    String className = map.get(key);
                    mWriter.write("Router.getInstance().addActivity(\""+key+"\","+
                            className+");\n");
                }
                mWriter.write("}\n}\n");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(mWriter!=null){
                    try {
                        mWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        return false;
    }
}
