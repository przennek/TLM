package pl.edu.agh.utils;


import java.util.UUID;


/**
 * Created by Kamil on 10.12.2016.
 */
public class Utils {
    public static String generateAuthToken()  {
        return  UUID.randomUUID().toString();
    }
}
