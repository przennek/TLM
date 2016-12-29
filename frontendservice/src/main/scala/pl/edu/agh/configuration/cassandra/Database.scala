package pl.edu.agh.configuration.cassandra

/**
  * Created by Kamil on 29.12.2016.
  */
import com.websudos.phantom.dsl._
import pl.edu.agh.cassandra.repositories.{TestRepository, TestsTreeRepository}
import pl.edu.agh.configuration.cassandra.Connector._

class TestsDatabase(override val connector: KeySpaceDef) extends Database(connector) {
  object testsRepository extends TestRepository with connector.Connector
  object testsTreeRepository extends TestsTreeRepository with connector.Connector
}


object TestsDb extends TestsDatabase(connector)

trait TestsDatabaseProvider {
  def database: TestsDatabase
}
trait ProductionTestDatabase extends TestsDatabaseProvider {
  override val database = TestsDb
}
