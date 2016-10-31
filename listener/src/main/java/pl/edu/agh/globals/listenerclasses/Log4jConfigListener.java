package pl.edu.agh.globals.listenerclasses;

import org.apache.log4j.BasicConfigurator;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.globals.GlobalListener;

/**
 * Created by Przemek on 16.10.2016.
 */
// TODO create fancy log4j config as in http://logging.apache.org/log4j/1.2/manual.html
public class Log4jConfigListener extends GlobalListener {

    public Log4jConfigListener() {
        super(99);
    }

    @Override
    public void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        BasicConfigurator.configure();
    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // pass
    }

    @Override
    public String toString() {
        return "<Log4jConfigListener>";
    }
}
