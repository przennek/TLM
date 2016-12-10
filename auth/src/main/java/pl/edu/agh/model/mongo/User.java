package pl.edu.agh.model.mongo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by Przemek on 23.10.2016.
 */
@Getter @Setter @Accessors(fluent = true)
@ToString
public class User {
    public String login;
    public String password;
    public String role;
}
