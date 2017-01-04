package pl.edu.agh.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.messaging.Sender;
import pl.edu.agh.models.SessionData;

import java.util.Collection;

/**
 * Created by Przemek on 21.11.2016.
 */
@RestController
public class AuthorizationEndpoint {
    @Autowired
    Sender sender;

    @Autowired
    ObjectMapper mapper;

    private static TLMLogger logger = TLMLogger.getLogger(AuthorizationEndpoint.class.getName());

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping("/login-success")
    public String auth(@AuthenticationPrincipal UserDetails userDetails, @CookieValue(value = "auth-token", defaultValue = "") String authCookie) {
        Collection<SimpleGrantedAuthority> roles = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        SessionData sessionData = new SessionData();
        sessionData
                .sessionId(authCookie)
                .username(userDetails.getUsername())
                .role(roles.toArray()[0].toString());
        try {
            sender.sendOverTopic("auth-exchange", "auth.token.broadcast.login", mapper.writeValueAsString(sessionData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return "{authorization: \"false\"}";
        }
        return "{authorization: \"true\"}";
    }

    @RequestMapping("/logout-success")
    public String logout(@CookieValue(value = "auth-token", defaultValue = "") String authCookie) {
        SessionData sessionData = new SessionData();
        sessionData
                .sessionId(authCookie)
                .username("")
                .role("");
        try {
            sender.sendOverTopic("auth-exchange", "auth.token.broadcast.login", mapper.writeValueAsString(sessionData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return "{logout: \"false\"}";
        }
        return "{logout: \"true\"}";
    }
}
