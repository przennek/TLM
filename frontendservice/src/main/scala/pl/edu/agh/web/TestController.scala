package pl.edu.agh.web

import java.io.{ByteArrayOutputStream, OutputStream}
import java.util.UUID

import scala.collection.JavaConversions._
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}
import pl.edu.agh.cassandra.services.TestsService

import scala.collection.mutable.ListBuffer
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
      return resultEither.right.get.get.jsonData
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
        val mapper  = new ObjectMapper()
        val jResult: java.util.List[String] = ListBuffer(resultEither.right.get: _*)
        return mapper.writeValueAsString(mapper.writeValueAsString(jResult))
      } else  {
        throw new Exception("Not found")
      }
    }
}
