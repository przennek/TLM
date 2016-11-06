package pl.edu.agh.globals.listenerclasses;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.globals.GlobalListener;

/**
 * Created by Przemek on 16.10.2016.
 */

// TODO create connection with TLM
public class TLMConnectionListener extends GlobalListener {
    // TODO move this scratch to designated encrypted session file.
    private static String sessionId;

    public TLMConnectionListener() {
        super(98);
    }

    @Override
    public void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

    }

    @Override
    public String toString() {
        return "<TLMConnectionListener>";
    }
}
