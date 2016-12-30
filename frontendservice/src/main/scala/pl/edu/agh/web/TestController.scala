package pl.edu.agh.web

import java.util.UUID

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RestController}
import pl.edu.agh.cassandra.services.TestsService

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

@Controller
class TestController() {
  @RestController
  @RequestMapping(Array("/getTest"))
  def getTest(tokenId: String): String = {
    val testsService = TestsService.findTestById(UUID.fromString(tokenId));
    val result = Await.ready(testsService, 20.seconds).value.get
    val resultEither = result match {
      case Success(t) => Right(t)
      case Failure(e) => Left(e)
    }
    if (resultEither.isRight && resultEither.right.get.isDefined) {
      val mapper  = new ObjectMapper() with ScalaObjectMapper
      mapper.registerModule(DefaultScalaModule)
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      return mapper.writeValueAsString(resultEither.right.get.get)
    } else  {
      throw new Exception("Not found")
    }
  }

  @RestController
  @RequestMapping(Array("/getTestTree"))
  def getTestTree(moduleName: String): String = {
    val testsService = TestsService.findTestTreeByModuleName(moduleName);
    val result = Await.ready(testsService, 20.seconds).value.get
    val resultEither = result match {
      case Success(t) => Right(t)
      case Failure(e) => Left(e)
    }
    if (resultEither.isRight && resultEither.right.get.isDefined) {
      return resultEither.right.get.get.jsonData
    } else {
      throw new Exception("Not found")
    }
  }

    @RestController
    @RequestMapping(Array("/getAllTestTrees"))
    def getAllTestTrees(): String = {
      val testsService = TestsService.findAllTestTreesNames();
      val result = Await.ready(testsService, 20.seconds).value.get
      val resultEither = result match {
        case Success(t) => Right(t)
        case Failure(e) => Left(e)
      }
      if (resultEither.isRight && resultEither.right.get.length > 0) {
        val mapper  = new ObjectMapper() with ScalaObjectMapper
        mapper.registerModule(DefaultScalaModule)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.writeValueAsString(mapper.writeValueAsString(resultEither.right.get))
      } else  {
        throw new Exception("Not found")
      }
    }
}
