package pl.edu.agh.configuration.cassandra

import java.net.InetAddress
import com.typesafe.config.ConfigFactory
import com.websudos.phantom.connectors.{ContactPoints}
import scala.collection.JavaConversions._

object Connector {
  val cassandraConfig = ConfigFactory.load("cassandra.conf")
  val hosts = cassandraConfig.getStringList("cassandra.host")
  val inets = hosts.map(InetAddress.getByName)
  val keyspace: String = cassandraConfig.getString("cassandra.keyspace")

  lazy val connector = ContactPoints(hosts).withClusterBuilder(
    _.withCredentials(
      cassandraConfig.getString("cassandra.username"),
      cassandraConfig.getString("cassandra.password")
    )
  ).keySpace(keyspace)

}
