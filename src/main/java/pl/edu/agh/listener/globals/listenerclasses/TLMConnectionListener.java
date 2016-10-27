package pl.edu.agh.listener.globals.listenerclasses;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.listener.globals.GlobalListener;

/**
 * Created by Przemek on 16.10.2016.
 */

// TODO create connection with TLM
public class TLMConnectionListener extends GlobalListener {
    public TLMConnectionListener() {
        this.priority = 90;
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
