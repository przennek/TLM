package pl.edu.agh.model.ws;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Przemek on 26.10.2016.
 */
@Getter @Setter
public class TestClass {
    private String tokenId;
    private String className;
    private List<String> testMethods;
}
