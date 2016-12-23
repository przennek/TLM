package pl.edu.agh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.agh.messaging.Sender;

import java.util.Collection;

/**
 * Created by Przemek on 21.11.2016.
 */
@Controller
public class AuthorizationEndpoint {
    @Autowired
    Sender sender;

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping("/login-success")
    public String auth(@AuthenticationPrincipal UserDetails userDetails, @CookieValue(value = "auth-token", defaultValue = "") String authCookie) {
        Collection<SimpleGrantedAuthority> roles = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        sender.sendOverTopic("auth-exchange", "auth.token.broadcast.login", "{sessionId: \""+authCookie +"\", role: \""+roles.toArray()[0].toString()+"\", username: \""+userDetails.getUsername()+"\"}");
        return "{authorization: \"true\"}";
    }

    @RequestMapping("/logout-success")
    public String logout(@CookieValue(value = "auth-token", defaultValue = "") String authCookie) {
        sender.sendOverTopic("auth-exchange", "auth.token.broadcast.logout", "{sessionId: \""+authCookie +"\"}");
        return "{logout: \"true\"}";
    }
}
