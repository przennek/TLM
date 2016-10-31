package pl.edu.agh.listener.util;

import org.assertj.core.util.VisibleForTesting;
import org.testng.ITestNGMethod;
import pl.edu.agh.listener.exceptions.TokenCouldNotBeParsedException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Przemek on 24.10.2016.
 */
public class FileMarkerHelper {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FileMarkerHelper.class);
    private static final String PROPERTIES_PATH = "src/main/resources/tlm.properties";

    public static String markTestClass(Path path) {
        // TODO verify in db if token is unique
        String uniqueToken = uniqueToken(() -> UUID.randomUUID().toString(), String -> true);
        try {
            Files.setAttribute(path, "user:test_class_id", uniqueToken.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
        return uniqueToken;
    }

    public static void deleteMark(Path path) {
        try {
            Files.setAttribute(path, "user:test_class_id", "".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Path preparePath(ITestNGMethod method) throws IOException {
        // read test path from properties
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_PATH));
        } catch (IOException e) {
            log.error("Error while reading properties file, check your TLM configuration!");
            log.error(e.getMessage(), e);
            throw e;
        }

        // absolute path to test case
        String packagePath = properties.getProperty("testpackagepath").replaceAll("\"", "");
        return Paths.get(packagePath + "/" + method.getTestClass().toString().replaceAll("\\.", "/").replaceAll("]", "").split(" ")[2] + ".java");
    }

    public static String getToken(Path path) throws TokenCouldNotBeParsedException {
        byte[] readToken;

        if (!Files.exists(path)) {
            log.error("Error while reading test file on path: " + path.toString());
            throw new TokenCouldNotBeParsedException();
        }

        try {
            readToken = (byte[]) Files.getAttribute(path, "user:test_class_id");
        } catch (NoSuchFileException e) {
            return "";
        } catch (FileSystemException e) {
            return "";
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TokenCouldNotBeParsedException();
        }
        return new String(readToken, StandardCharsets.UTF_8);
    }

    @VisibleForTesting
    protected static String uniqueToken(Supplier<String> tokenGenerator, Function<String, Boolean> isUnique) {
        final Integer MAX_RETRIES = 10;
        Integer n = 0;
        String token = tokenGenerator.get();
        while (!isUnique.apply(token)) {
            if (n++ > MAX_RETRIES) throw new RuntimeException("Max retries exceeded! Check sanity of your db!");
            token = tokenGenerator.get();
        }
        return token;
    }
}
