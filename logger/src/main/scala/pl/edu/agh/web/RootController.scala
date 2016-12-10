package pl.edu.agh.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class RootController {

  @RequestMapping(Array("/"))
  def handleRootRequest(): String = "redirect:/logs"
}
