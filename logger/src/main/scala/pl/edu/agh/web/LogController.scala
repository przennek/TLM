package pl.edu.agh.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

import scalaj.http.{Http, HttpOptions}


@Controller
@RequestMapping(Array("/logs"))
class LogController () {
  @RestController
  @RequestMapping(Array("/getAllByLevel"))
  def level(level: String): String = {
    val result = Http(s"http://localhost:9200/log/$level/_search?pretty&filter_path=hits.hits._source.*")
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString
    result.toString
  }

  @RestController
  @RequestMapping(Array("/getContainingMessage"))
  def contains(msg: String): String = {
    val result = Http(s"http://localhost:9200/log/_search?pretty&filter_path=hits.hits._source.*")
      .postData(s"""{"query" : {"match" : { "message" : "$msg" }}}""")
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString
    result.toString
  }
}