package pl.edu.agh.listener.globals;

import org.testng.IInvokedMethodListener;

/**
 * Created by Przemek on 27.10.2016.
 */
public abstract class PriorityAwareListener implements IInvokedMethodListener, Comparable<PriorityAwareListener> {
    protected int priority;

    @Override
    public int compareTo(PriorityAwareListener listener) {
        if (this.priority == listener.priority) return 0;
        return this.priority < listener.priority ? 1 : -1;
    }
}
