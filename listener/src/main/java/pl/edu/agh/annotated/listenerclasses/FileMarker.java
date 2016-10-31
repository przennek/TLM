package pl.edu.agh.annotated.listenerclasses;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.globals.PriorityAwareListener;
import pl.edu.agh.util.FileMarkerHelper;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Przemek on 27.10.2016.
 */
public class FileMarker extends PriorityAwareListener {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Collector.class);

    public FileMarker() {
        super(100);
    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // nothing to do here
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iTestResult.isSuccess()) {
            try {
                FileMarkerHelper helper = new FileMarkerHelper();
                Path testCasePath = helper.preparePath(iInvokedMethod.getTestMethod());
                if ("".equals(helper.getToken(testCasePath))) {
                    helper.markTestClass(testCasePath);
                }
            } catch (TokenCouldNotBeParsedException | IOException ignored) {
                // carry on
            }
        }
    }

    @Override
    public String toString() {
        return "<FileMarker>";
    }
}
