package pl.edu.agh.util;

import com.sun.javadoc.*;
import com.sun.tools.javadoc.Main;
import pl.edu.agh.exceptions.TestClassDataParseException;
import pl.edu.agh.model.ws.JavaDocTag;
import pl.edu.agh.model.ws.TestClass;
import pl.edu.agh.model.ws.TestMethod;
import pl.edu.agh.model.ws.TestMethodParameter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kamil on 31.10.2016.
 */

public class JavaDocParser extends Doclet {
    public static List<TestClass> parsedDocs;

    public static List<TestClass> parse(Path path) throws TestClassDataParseException {
        //Execute JavaDoc Generation process and get required data
        try {
            Main.execute("", JavaDocAnalyzer.class.getName(), new String[]{path.toString()});
        } catch (Exception e) {
            throw new TestClassDataParseException();
        }
        return parsedDocs;
    }

    /**
     * JavaDoc parser - see http://docs.oracle.com/javase/6/docs/technotes/guides/javadoc/doclet/overview.html
     */
    public static class JavaDocAnalyzer {

        public static List<JavaDocTag> getTags(Doc elDocs) {
            List<JavaDocTag> tags = new ArrayList<>(elDocs.tags().length);
            for (Tag elTag : elDocs.tags()) {
                tags.add(new JavaDocTag()
                        .tagName(elTag.name())
                        .tagText(elTag.text()));
            }
            return tags;
        }

        public static List<TestMethodParameter> getParameters(MethodDoc methodDoc) {
            List<TestMethodParameter> methodParams = new ArrayList<>();
            ParamTag[] paramTags = methodDoc.paramTags();
            for (Parameter param : methodDoc.parameters()) {
                Optional<ParamTag> foundTag = Arrays.stream(paramTags).filter(x -> x.name().equals(param.name()))
                        .findFirst();
                if (foundTag.isPresent()) {
                    methodParams.add(new TestMethodParameter()
                            .paramName(foundTag.get().name())
                            .paramDescription(foundTag.get().text())
                            .paramType(param.typeName()));
                }
            }
            return methodParams;
        }

        public static boolean start(RootDoc root) {
            parsedDocs = new ArrayList<>(root.classes().length);
            for (ClassDoc classDoc : root.classes()) {
                TestClass parsedClass = new TestClass()
                        .className(classDoc.qualifiedName())
                        .classTags(getTags(classDoc))
                        .classComment(classDoc.commentText());
                for (MethodDoc methodDoc : classDoc.methods()) {
                    TestMethod testMethod = new TestMethod()
                            .methodName(methodDoc.name() + methodDoc.signature())
                            .methodComment(methodDoc.commentText())
                            .methodTags(getTags(methodDoc))
                            .methodParameters(getParameters(methodDoc));
                    parsedClass.testMethods().add(testMethod);
                }
                parsedDocs.add(parsedClass);
            }
            return false;
        }
    }
}
