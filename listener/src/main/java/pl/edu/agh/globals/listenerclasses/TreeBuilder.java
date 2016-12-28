package pl.edu.agh.globals.listenerclasses;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import pl.edu.agh.annotated.model.FileEntry;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.globals.GlobalListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.util.FileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Przemek on 18.12.2016.
 */
public class TreeBuilder extends GlobalListener {
    private static TLMLogger log = TLMLogger.getLogger(TreeBuilder.class.getName());

    public TreeBuilder() {
        super(98);
    }

    @Override
    public void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        try {
            System.out.println(crawl(FileHelper.get().getTestPackagePath(), true));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String crawl(String path, Boolean tokenActivated) {
        File inputFolder = Paths.get(path).toFile();
        FileEntry parentNode = new FileEntry(inputFolder);
        return walk(parentNode, tokenActivated);
    }

    public String walk(FileEntry parentNode, Boolean tokenActivated) {
        if (parentNode.directory()) {
            File childNodes[] = parentNode.listFiles();
            for (File childNode : childNodes) {
                FileEntry fileEntry = new FileEntry(childNode);
                try {
                    if ("".equals(FileHelper.get().getToken(fileEntry.file().toPath())) || !tokenActivated) {
                        parentNode.addSuccessor(fileEntry);
                    }
                } catch (TokenCouldNotBeParsedException e) {
                    // consume
                }
                walk(fileEntry, tokenActivated);
            }
        }
        return parentNode.toString();
    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // do nothing
    }
}
