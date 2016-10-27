package pl.edu.agh.listener.util;

import org.assertj.core.util.VisibleForTesting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Przemek on 24.10.2016.
 */
public class FileMarker {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FileMarker.class);

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
