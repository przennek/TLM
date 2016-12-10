package pl.edu.agh;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.testng.SkipException;
import pl.edu.agh.globals.GlobalListener;
import pl.edu.agh.globals.PriorityAwareListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.util.FileHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


/**
 * Created by Przemek on 16.10.2016.
 */
public class LoadListener extends GlobalListener {
    private static TLMLogger log = TLMLogger.getLogger(LoadListener.class.getName());
    private URL GLOBAL_LISTENERS_DIR;
    private URL ANN_LISTENERS_DIR;

    private List<GlobalListener> globalListeners;
    private List<PriorityAwareListener> annListeners;

    public LoadListener() {
        super(-1);
        GLOBAL_LISTENERS_DIR = getClass().getResource("./globals/listenerclasses");
        ANN_LISTENERS_DIR = getClass().getResource("./annotated/listenerclasses");
    }

    @Override
    public void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        executeGlobalListeners(iInvokedMethod, iTestResult);
        log.debug(String.format("%s <> Loaded all global listeners!", this.toString()), null);
        executeListeners(iInvokedMethod, iTestResult);
        log.debug(String.format("%s <> Loaded all annotation based listeners!", this.toString()), null);
    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        globalListeners.forEach(listener -> listener.after(iInvokedMethod, iTestResult));
        log.debug(String.format("%s <> Cleaned up all global listeners!", this.toString()), null);
        annListeners.forEach(listener -> listener.afterInvocation(iInvokedMethod, iTestResult));
        log.debug(String.format("%s <> Cleaned up annotation based listeners!", this.toString()), null);

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
            log.debug(String.format("%s <> loaded %s", this.toString(), listener.toString()), null);
        });
    }

    private void executeListeners(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        annListeners = loadListeners(ANN_LISTENERS_DIR);
        Collections.sort(annListeners);
        annListeners.forEach(listener -> {
            listener.beforeInvocation(iInvokedMethod, iTestResult);
            log.debug(String.format("%s <> loaded %s", this.toString(), listener.toString()), null);
        });
    }

    private <T> List<T> loadListeners(URL path) {
        List<T> listeners = new LinkedList<>();
        try {
            String strPath = '/' == path.getPath().toCharArray()[0]
                    ? path.getPath().substring(1, path.getPath().length()) : path.getPath();

            Files.list(Paths.get(strPath)).forEach(listener -> {
                try {
                    String className = listener.toString()
                            .replaceAll("\\\\", ".")
                            .replaceAll("/", ".")
                            .replaceAll("src.main.java.", "");
                    className = className.substring(className.indexOf("pl.edu.agh"), className.lastIndexOf("."));
                    listeners.add((T) Class.forName(className).getConstructor().newInstance());
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new SkipException(e.getMessage());
                }
            });
        } catch (IOException e) {
            log.error("Error while loading listeners.", e);
            throw new SkipException(e.getMessage());
        }
        return listeners;
    }
}