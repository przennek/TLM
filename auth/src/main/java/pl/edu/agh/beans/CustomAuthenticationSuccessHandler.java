package pl.edu.agh.beans;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import pl.edu.agh.models.User;
import pl.edu.agh.sessionmanager.SessionManager;
import pl.edu.agh.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamil on 04.12.2016.
 */
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    public CustomAuthenticationSuccessHandler() {
        super();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String cookie = httpServletRequest.getHeader("cookie");
        if(cookie == null ) cookie = "";
        Map<String, String> headers = new HashMap<>();
        Arrays.stream(cookie.split(";")).forEach(el -> {
            String[] t = el.contains("=") ? el.split("=") : el.split(":");
            headers.put(t[0].trim(), t[1].trim());
        });
        String sessionId = headers.get("auth-token");
        User user = SessionManager.sessionIds.get(sessionId);
        if(user != null) {
            return;
        }
        Cookie newCookie = new Cookie("auth-token", Utils.generateAuthToken());
        newCookie.setPath("/");
        httpServletResponse.addCookie(newCookie);
        if(httpServletRequest.getParameter("ref") != null) {
            httpServletResponse.sendRedirect(httpServletRequest.getParameter("ref") + "/login-success");
        }
    }
}
