package pl.edu.agh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import pl.edu.agh.messaging.Sender;

/**
 * Created by Przemek on 21.11.2016.
 */
@RestController
public class AuthorizationEndpoint {
    @Autowired
    Sender sender;

    @Secured({"ROLE_USER"})
    @RequestMapping("/auth")
    public String auth(@AuthenticationPrincipal UserDetails userDetails) {
        String sessionid = RequestContextHolder.currentRequestAttributes().getSessionId();
        sender.sendOverTopic("auth-exchange", "auth.token.broadcast.sessionid", sessionid);
        return "{authorization: \"true\"}";
    }
}
