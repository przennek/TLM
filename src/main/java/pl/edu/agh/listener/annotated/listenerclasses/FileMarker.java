package pl.edu.agh.listener.annotated.listenerclasses;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.listener.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.listener.globals.PriorityAwareListener;
import pl.edu.agh.listener.util.FileMarkerHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.agh.listener.util.FileMarkerHelper.getToken;
import static pl.edu.agh.listener.util.FileMarkerHelper.preparePath;

/**
 * Created by Przemek on 27.10.2016.
 */
public class FileMarker extends PriorityAwareListener {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Collector.class);
    public static List<Path> registeredTests = new ArrayList<>();

    public FileMarker() {
        this.priority = 100;
    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // nothing to do here
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iTestResult.isSuccess()) {
            try {
                Path testCasePath = preparePath(iInvokedMethod.getTestMethod());
                if ("".equals(getToken(testCasePath))) {
                    registeredTests.add(testCasePath);
                    FileMarkerHelper.markTestClass(testCasePath);
                }
            } catch (TokenCouldNotBeParsedException | IOException ignored) {
                // carry on
            }
        }
    }
}
