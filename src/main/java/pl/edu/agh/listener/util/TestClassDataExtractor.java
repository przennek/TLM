package pl.edu.agh.listener.util;

import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import pl.edu.agh.listener.exceptions.TestClassDataParseException;
import pl.edu.agh.model.ws.TestClass;
import pl.edu.agh.model.ws.TestMethod;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static pl.edu.agh.listener.util.ListenerHelper.getTestClassName;

/**
 * Created by Kamil on 31.10.2016.
 */
public class TestClassDataExtractor {
    public static void extractData(TestClass resultTestClass, IInvokedMethod iInvokedMethod, Path path) throws TestClassDataParseException {
        List<String> testMethodNames =  parseOutTestMethods(iInvokedMethod);
        List<TestClass> parsedJavaDoc = JavaDocParser.parse(path);
        resultTestClass.className(getTestClassName(iInvokedMethod));
        if(parsedJavaDoc.size() != 1 ) {
            throw new TestClassDataParseException();
        }
        TestClass testClass =  parsedJavaDoc.get(0);
        resultTestClass.classComment(testClass.classComment());
        resultTestClass.classTags(testClass.classTags());
        for(String testMethodName: testMethodNames) {
            Optional<TestMethod> testMethod = testClass.testMethods().stream()
                    .filter(x -> testMethodName.indexOf(x.methodName()) >=0).findFirst();
            if(!testMethod.isPresent()) {
                throw new TestClassDataParseException();
            }
            resultTestClass.testMethods().add(testMethod.get());
        }
    }

    private static List<String> parseOutTestMethods(IInvokedMethod iInvokedMethod) {
        List<ITestNGMethod> methodList = Arrays.asList(iInvokedMethod.getTestMethod().getTestClass().getTestMethods());
        List<String> toStringMethods = new ArrayList<>(methodList.size());
        methodList.stream()
                .filter(ITestNGMethod::isTest)
                .forEach(method -> {
                    String methodName = method.toString();
                    methodName = methodName.substring(0, methodName.indexOf("["));
                    toStringMethods.add(methodName);
                });
        return toStringMethods;
    }
}
