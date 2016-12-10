package pl.edu.agh.web

@Controller
class RootController {

  @RequestMapping(Array("/"))
  def handleRootRequest(): String = "redirect:/logs"
}
