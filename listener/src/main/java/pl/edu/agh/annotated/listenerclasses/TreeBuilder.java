package pl.edu.agh.annotated.listenerclasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.globals.GlobalListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.util.FileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String crawl(String path, Boolean tokenActivated) throws JsonProcessingException {
        Map<String, List<String>> dirMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Files.walk(Paths.get(path)).filter(p -> !Files.isDirectory(p)).forEach(p -> {
                final String key = p.getParent().toString().replaceAll("\\\\", "\\/");

                try {
                    final String token = FileHelper.get().getToken(p);
                    if (!"".equals(token) || !tokenActivated) {
                        dirMap.putIfAbsent(key, new ArrayList<String>());
                        List<String> tmp = dirMap.get(key);
                        tmp.add(token);
                        dirMap.put(p.getParent().toString().replaceAll("\\\\", "\\/"), tmp);
                    }
                } catch (TokenCouldNotBeParsedException e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return objectMapper.writeValueAsString(dirMap);
    }

    public static void crawl2(String path, Boolean tokenActivated) {
        File inputFolder = Paths.get(path).toFile();
        traverse(inputFolder, "");
    }

    public static void traverse(File parentNode, String leftIndent) {
        if (parentNode.isDirectory()) {
            System.out.println(leftIndent + parentNode.getName());

            // Use left padding to create tree structure in the console output.
            leftIndent += "   ";

            File childNodes[] = parentNode.listFiles();
            for (File childNode : childNodes) {
                traverse(childNode, leftIndent);
            }
        } else {
            System.out.println(leftIndent + parentNode.getName());
        }
    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // do nothing
    }
}
