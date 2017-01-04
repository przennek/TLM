package pl.edu.agh.cassandra.repositories

import java.util.UUID
import com.websudos.phantom.dsl._
import pl.edu.agh.models.DbTest
import scala.concurrent.Future

class TestModel extends CassandraTable[TestRepository, DbTest] {

  override def tableName: String = "tests"

  object tokenId extends TimeUUIDColumn(this) with PrimaryKey[UUID] { override lazy val name = "tokenId" }
  object className extends StringColumn(this)
  object moduleName extends StringColumn(this)
  object editLog extends com.websudos.phantom.column.ListColumn[TestRepository, DbTest, String](this)
  object jsonData extends StringColumn(this)

  override def fromRow(r: Row): DbTest = DbTest(tokenId(r), className(r), moduleName(r), editLog(r), jsonData(r))
}

abstract class TestRepository extends TestModel with RootConnector {

  def findTestById(id: UUID): Future[Option[DbTest]] = {
    select
      .where(_.tokenId eqs id)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .one()
  }
}
