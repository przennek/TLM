package pl.edu.agh.models;

/**
 * Created by Kamil on 02.12.2016.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
@Getter
@Setter
@Accessors(fluent = true)
@ToString
public class User {
    public String login;
    public String role;
}
