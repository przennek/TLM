package pl.edu.agh.exceptions;

/**
 * Created by przemek on 01.11.16.
 */
public class TLMPropertiesNotFoundException extends RuntimeException {
    public TLMPropertiesNotFoundException() {
        super();
    }

    public TLMPropertiesNotFoundException(String message) {
        super(message);
    }
}
