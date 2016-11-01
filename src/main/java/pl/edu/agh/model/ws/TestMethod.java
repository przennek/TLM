package pl.edu.agh.model.ws;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 31.10.2016.
 */

    @Getter @Setter @Accessors(fluent = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
    public class TestMethod {
        private String methodName;
        private String methodComment;
        private List<JavaDocTag> methodTags = new ArrayList<>();
        private List<TestMethodParameter> methodParameters = new ArrayList<>();
    }

