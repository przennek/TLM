package pl.edu.agh.configuration.filters;

import pl.edu.agh.models.User;
import pl.edu.agh.sessionmanager.SessionManager;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamil on 02.12.2016.
 */
public class AuthFilter implements Filter {

    @Override
    public void destroy() {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String cookie = request.getHeader("cookie");
        if(cookie == null ) cookie = "";
        Map<String, String> headers = new HashMap<>();
        Arrays.stream(cookie.split(";")).forEach(el -> {
            String[] t = el.contains("=") ? el.split("=") : el.split(":");
            headers.put(t[0].trim(), t[1].trim());
         });
        String sessionId = headers.get("auth-token");
        User user = SessionManager.sessionIds.get(sessionId);
        if(user != null) {
            request.getSession().setAttribute("ROLE", user.role());
            request.getSession().setAttribute("USERNAME", user.login());
            chain.doFilter(req, res);
        } else {
            ((HttpServletResponse) res).setStatus(401);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // Do nothing
    }
}