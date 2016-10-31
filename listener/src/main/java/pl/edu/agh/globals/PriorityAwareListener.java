package pl.edu.agh.globals;

import org.testng.IInvokedMethodListener;

/**
 * Created by Przemek on 27.10.2016.
 */
public abstract class PriorityAwareListener implements IInvokedMethodListener, Comparable<PriorityAwareListener> {
    protected int priority;

    public PriorityAwareListener(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(PriorityAwareListener listener) {
        if (this.priority == listener.priority) return 0;
        return this.priority < listener.priority ? 1 : -1;
    }
}
