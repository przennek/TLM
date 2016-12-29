package pl.edu.agh.cassandra.services

import com.websudos.phantom.dsl._
import pl.edu.agh.configuration.cassandra.ProductionTestDatabase
import pl.edu.agh.models.{DbTest, DbTestTree}

import scala.concurrent.Future

/**
  * Created by Kamil on 29.12.2016.
  */

trait TestsService extends ProductionTestDatabase {

  def findTestById(id: UUID): Future[Option[DbTest]] = {
    database.testsRepository.findTestById(id)
  }
  def findTestTreeByModuleName(moduleName: String): Future[Option[DbTestTree]] = {
    database.testsTreeRepository.findTestTreeByModuleName(moduleName)
  }

}

object TestsService extends TestsService with ProductionTestDatabase