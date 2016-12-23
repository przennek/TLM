package pl.edu.agh.configuration.filters

import java.io.IOException
import java.util
import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import pl.edu.agh.models.User
import pl.edu.agh.sessionmanager.SessionManager

/**
  * Created by Kamil on 02.12.2016.
  */
class AuthFilter extends Filter {
  def destroy() {
    // Do nothing
  }

  @throws[IOException]
  @throws[ServletException]
  def doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
    val request: HttpServletRequest = req.asInstanceOf[HttpServletRequest]
    val cookie: String = if (request.getHeader("cookie") == null) "" else request.getHeader("cookie");
    val headers: util.Map[String, String] = new util.HashMap[String, String]

    for(el <- cookie.split(";")) {
      val t: Array[String] = if (el.contains("=")) el.split("=") else el.split(":")
      headers.put(t(0).toString.trim, t(1).toString.trim)
    }

    val sessionId: String = headers.get("auth-token")
    val user: User = SessionManager.sessionIds.get(sessionId)
    if (user != null) {
      request.getSession.setAttribute("ROLE", user.role)
      request.getSession.setAttribute("USERNAME", user.login)
      chain.doFilter(req, res)
    }  else res.asInstanceOf[HttpServletResponse].setStatus(401)
  }

  @throws[ServletException]
  def init(arg0: FilterConfig) {
    // Do nothing
  }
}