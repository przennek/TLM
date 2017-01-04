package pl.edu.agh.cassandra.repositories

import com.websudos.phantom.dsl._
import pl.edu.agh.models.{DbTestTree}
import scala.concurrent.Future

class TestTreeModel extends CassandraTable[TestsTreeRepository, DbTestTree] {

  override def tableName: String = "tests_tree"

  object moduleName extends StringColumn(this) with PartitionKey[String]{ override lazy val name = "moduleName" }
  object jsonData extends StringColumn(this)
  override def fromRow(r: Row): DbTestTree = DbTestTree(moduleName(r), jsonData(r))
}

abstract class TestsTreeRepository extends TestTreeModel with RootConnector {

  def findTestTreeByModuleName(moduleName: String): Future[Option[DbTestTree]] = {
    select
      .where(_.moduleName eqs moduleName)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .one()
  }

  def findAllTestTreesNames(): Future[List[String]] = {
    select(_.moduleName).all()
      .consistencyLevel_=(ConsistencyLevel.ALL).fetch()
  }
}
