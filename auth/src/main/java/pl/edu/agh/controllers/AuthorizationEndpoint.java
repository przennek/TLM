package pl.edu.agh.controllers;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import pl.edu.agh.messaging.Sender;

import java.util.Collection;

/**
 * Created by Przemek on 21.11.2016.
 */
@RestController
public class AuthorizationEndpoint {
    @Autowired
    Sender sender;

    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping("/login")
    public String auth(@AuthenticationPrincipal UserDetails userDetails) {
        Collection<SimpleGrantedAuthority> roles = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        String sessionid = RequestContextHolder.currentRequestAttributes().getSessionId();
        sender.sendOverTopic("auth-exchange", "auth.token.broadcast.sessionid", "{sessionId: \""+sessionid +"\", role: \""+roles.toArray()[0].toString()+"\", username: \""+userDetails.getUsername()+"\"}");
        return "{authorization: \"true\"}";
    }

    @RequestMapping("/logout")
    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.setAuthenticated(false);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
