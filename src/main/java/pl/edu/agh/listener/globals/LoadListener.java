package pl.edu.agh.listener.globals;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Przemek on 16.10.2016.
 */
public class LoadListener extends GlobalListener {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadListener.class);
    private static final String GLOBAL_LISTENERS_DIR = "src/main/java/pl/edu/agh/listener/globals/listenerclasses";
    private static final String ANN_LISTENERS_DIR = "src/main/java/pl/edu/agh/listener/annotated/listenerclasses";

    private List<GlobalListener> globalListeners;
    private List<PriorityAwareListener> annListeners;

    @Override
    public void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        executeGlobalListeners(iInvokedMethod, iTestResult);
        log.info(String.format("%s <> Loaded all global listeners!", this.toString()));
        executeListeners(iInvokedMethod, iTestResult);
        log.info(String.format("%s <> Loaded all annotation based listeners!", this.toString()));
    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        globalListeners.forEach(listener -> listener.after(iInvokedMethod, iTestResult));
        log.info(String.format("%s <> Cleaned up all global listeners!", this.toString()));
        annListeners.forEach(listener -> listener.afterInvocation(iInvokedMethod, iTestResult));
        log.info(String.format("%s <> Cleaned up annotation based listeners!", this.toString()));

    }

    @Override
    public String toString() {
        return "<LoadListener>";
    }

    private void executeGlobalListeners(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        globalListeners = loadListeners(GLOBAL_LISTENERS_DIR);
        Collections.sort(globalListeners);
        globalListeners.forEach(listener -> {
            listener.before(iInvokedMethod, iTestResult);
            log.info(String.format("%s <> loaded %s", this.toString(), listener.toString()));
        });
    }

    private void executeListeners(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        annListeners = loadListeners(ANN_LISTENERS_DIR);
        Collections.sort(annListeners);
        annListeners.forEach(listener -> {
            listener.beforeInvocation(iInvokedMethod, iTestResult);
            log.info(String.format("%s <> loaded %s", this.toString(), listener.toString()));
        });
    }

    private <T> List<T> loadListeners(String path) {
        List<T> listeners = new LinkedList<>();
        try {
            Files.list(Paths.get(path)).forEach(listener -> {
                try {
                    String className = listener.toString()
                            .replaceAll("\\\\", ".")
                            .replaceAll("src.main.java.", "")
                            .replaceAll(".java", "");
                    listeners.add((T) Class.forName(className).getConstructor().newInstance());
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new SkipException(e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new SkipException(e.getMessage());
        }
        return listeners;
    }
}
