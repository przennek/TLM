package pl.edu.agh.util;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.globals.listenerclasses.TLMConnectionListener;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by Przemek on 26.10.2016.
 */
public class ListenerHelper {
    public static boolean hasAnnotation(IInvokedMethod method, Class<? extends Annotation> annotation) {
        ITestNGMethod testNGMethod = method.getTestMethod();
        return hasAnnotation(testNGMethod, annotation);
    }

    // TODO get rid of this dirty hack
    public static String getAnnotationValue(IInvokedMethod method, Class<? extends Annotation> annotation) {
        ITestNGMethod testNGMethod = method.getTestMethod();
        Annotation ann;
        if (testNGMethod.getConstructorOrMethod().getMethod().isAnnotationPresent(annotation)) {
            ann = (Annotation) Arrays.stream(testNGMethod.getConstructorOrMethod().getMethod().getAnnotations())
                    .filter(ant -> ant.toString().contains("@pl.edu.agh.annotated.annotations.TestType")).toArray()[0];
        } else {
            ann = (Annotation) Arrays.stream(testNGMethod.getInstance().getClass().getAnnotations())
                    .filter(ant -> ant.toString().contains("@pl.edu.agh.annotated.annotations.TestType")).toArray()[0];
        }
        return ann.toString().substring(ann.toString().indexOf("value=") + 6, ann.toString().length()-1);
    }

    public static HttpPost prepareRequest(String endpointName) throws IOException {
        Properties properties = FileHelper.get().getTlmProperties();
        HttpPost postRequest = new HttpPost(
                String.format("http://%s:%s/testregister/%s/",
                        properties.getProperty("gate"),
                        properties.getProperty("port"),
                        endpointName
                )
        );
        postRequest.setHeader(new BasicHeader("Cookie: auth-token", TLMConnectionListener.sessionId));
        return postRequest;
    }

    public static boolean hasAnnotation(ITestNGMethod method, Class<? extends Annotation> annotation) {
        return method.getConstructorOrMethod().getMethod().isAnnotationPresent(annotation)
                || method.getInstance().getClass().isAnnotationPresent(annotation);
    }

    public static String getTestClassName(IInvokedMethod method) {
        return method.getTestMethod().getTestClass().getRealClass().toString().split(" ")[1];
    }
}
