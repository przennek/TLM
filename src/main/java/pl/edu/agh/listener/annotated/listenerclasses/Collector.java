package pl.edu.agh.listener.annotated.listenerclasses;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.listener.annotated.annotations.TestType;
import pl.edu.agh.listener.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.listener.globals.PriorityAwareListener;
import pl.edu.agh.model.ws.TestClass;

import java.io.IOException;
import java.nio.file.Path;

import static pl.edu.agh.listener.annotated.listenerclasses.FileMarker.registeredTests;
import static pl.edu.agh.listener.util.FileMarkerHelper.*;
import static pl.edu.agh.listener.util.ListenerHelper.hasAnnotation;

/**
 * Created by Przemek on 16.10.2016.
 */
public class Collector extends PriorityAwareListener {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Collector.class);

    public Collector() {
        this.priority = 99;
    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
       // nothing to do here
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // TODO if and ONLY if ALL test passed
        if(hasAnnotation(iInvokedMethod, TestType.class)) {
            try {
                Path path = preparePath(iInvokedMethod.getTestMethod());
                String token = getToken(path);
                if ("".equals(token)) {
                    log.error("Error while reading token from test file, does your OS support TLM?");
                    throw new TokenCouldNotBeParsedException();
                }
                if(!isTestInDB(token)) {
                    // TODO build ws call
                    // iInvokedMethod.getTestMethod().getRealClass().getMethods()
                    TestClass testClass = new TestClass();
                    // register test
                    if(!register(testClass) && registeredTests.contains(path)) {
                        // fail! rollback marks
                        System.out.println("Failed to register token: " + token + ", on file: " + path.toString());
                        deleteMark(path);
                    }
                }
            } catch (TokenCouldNotBeParsedException ignored) {
                // consume
            } catch (IOException e) {
                log.error("Configuration provided is incorrect. TLM won't work as expected.");
            }
        }
    }

    private Boolean register(TestClass testClass) {
        return false;
    }

    // TODO fill up
    private Boolean isTestInDB(String token) {
        return false;
    }
}
