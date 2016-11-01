package pl.edu.agh.listener.globals;

import lombok.Getter;
import lombok.Setter;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;

/**
 * Created by Przemek on 16.10.2016.
 */
@Getter @Setter
public abstract class GlobalListener extends PriorityAwareListener {
    private Boolean executed = false;
    private Boolean cleanedAfter = false;

    protected GlobalListener() {
        this.priority = 100;
    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if(!executed) {
            executed = true;
            before(iInvokedMethod, iTestResult);
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if(!cleanedAfter) {
            cleanedAfter = true;
            after(iInvokedMethod, iTestResult);
        }
    }

    public abstract void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult);

    public abstract void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult);
}
