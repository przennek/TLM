package pl.edu.agh.annotated.listenerclasses;

import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.globals.PriorityAwareListener;
import pl.edu.agh.model.ws.TestClass;
import pl.edu.agh.util.FileMarkerHelper;
import pl.edu.agh.util.ListenerHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Przemek on 16.10.2016.
 */
public class Collector extends PriorityAwareListener {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Collector.class);

    public Collector() {
        super(99);
    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // nothing to do here
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (ListenerHelper.hasAnnotation(iInvokedMethod, TestType.class)) {
            if (iTestResult.isSuccess()) {
                try {
                    FileMarkerHelper helper = new FileMarkerHelper();
                    Path path = helper.preparePath(iInvokedMethod.getTestMethod());
                    String token = helper.getToken(path);
                    if ("".equals(token)) {
                        log.error("Error while reading token from test file, does your OS support TLM?");
                        throw new TokenCouldNotBeParsedException();
                    }
                    if (!isTestInDB(token)) {
                        TestClass testClass = new TestClass()
                                .tokenId(token)
                                .className(ListenerHelper.getTestClassName(iInvokedMethod))
                                .testMethods(parseOutTestMethods(iInvokedMethod));

                        // register test
                        if (!register(testClass)) {
                            log.error("Failed to register token: " + token + ", on file: " + path.toString());
                            helper.deleteMark(path);
                        }
                    }
                } catch (TokenCouldNotBeParsedException ignored) {
                    // consume
                } catch (IOException e) {
                    log.error("Configuration provided is incorrect. TLM won't work as expected.");
                }
            }
        }
    }

    private List<String> parseOutTestMethods(IInvokedMethod iInvokedMethod) {
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

    // TODO make an endpoint call
    private Boolean register(TestClass testClass) {
        return false;
    }

    // TODO fill up
    private Boolean isTestInDB(String token) {
        return false;
    }

    @Override
    public String toString() {
        return "<Collector>";
    }
}
