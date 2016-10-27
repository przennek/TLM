package pl.edu.agh.listener.util;

import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;

import java.lang.annotation.Annotation;

/**
 * Created by Przemek on 26.10.2016.
 */
public class ListenerHelper {
    public static boolean hasAnnotation(IInvokedMethod method, Class<? extends Annotation> annotation) {
        ITestNGMethod testNGMethod = method.getTestMethod();
        return hasAnnotation(testNGMethod, annotation);
    }

    public static boolean hasAnnotation(ITestNGMethod method, Class<? extends Annotation> annotation) {
        return method.getConstructorOrMethod().getMethod().isAnnotationPresent(annotation)
                || method.getInstance().getClass().isAnnotationPresent(annotation);
    }

    public static String getTestClassName(IInvokedMethod method) {
        return method.getTestMethod().getTestClass().getRealClass().toString().split(" ")[1];
    }
}
