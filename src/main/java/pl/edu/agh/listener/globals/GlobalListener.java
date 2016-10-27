package pl.edu.agh.listener.globals;

import lombok.Getter;
import lombok.Setter;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

/**
 * Created by Przemek on 16.10.2016.
 */
@Getter @Setter
public abstract class GlobalListener implements IInvokedMethodListener, Comparable<GlobalListener> {
    private Boolean executed = false;
    private Boolean cleanedAfter = false;
    protected int priority;

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
        if(cleanedAfter) {
            cleanedAfter = true;
            after(iInvokedMethod, iTestResult);
        }
    }

    @Override
    public int compareTo(GlobalListener listener) {
        if (this.priority == listener.priority) return 0;
        return this.priority < listener.priority ? 1 : -1;
    }

    public abstract void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult);

    public abstract void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult);
}
