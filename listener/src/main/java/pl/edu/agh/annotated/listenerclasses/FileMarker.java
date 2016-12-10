package pl.edu.agh.annotated.listenerclasses;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.LoadListener;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.globals.PriorityAwareListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.util.FileHelper;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Przemek on 27.10.2016.
 */
public class FileMarker extends PriorityAwareListener {
    private static TLMLogger log = TLMLogger.getLogger(FileMarker.class.getName());

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
                Path testCasePath = FileHelper.get().preparePath(iInvokedMethod.getTestMethod());
                if ("".equals(FileHelper.get().getToken(testCasePath))) {
                    FileHelper.get().markTestClass(testCasePath);
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
