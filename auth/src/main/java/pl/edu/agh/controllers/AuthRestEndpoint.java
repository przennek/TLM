package pl.edu.agh.controllers;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Przemek on 23.12.2016.
 */
@RestController
public class AuthRestEndpoint {
    @RequestMapping("/sessionStatus")
    public String status(@CookieValue(value = "auth-token", defaultValue = "") String authCookie) {
        return "{authorization: \"true\"}";
    }
}
