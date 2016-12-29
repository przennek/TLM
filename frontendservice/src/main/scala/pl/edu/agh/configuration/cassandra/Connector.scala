package pl.edu.agh.configuration.cassandra

/**
  * Created by Kamil on 28.12.2016.
  * */
import java.net.InetAddress

import com.datastax.driver.core.{CodecRegistry, TypeCodec}
import com.datastax.driver.extras.codecs.MappingCodec
import com.typesafe.config.ConfigFactory
import com.websudos.phantom.connectors.{ContactPoint, ContactPoints}
import pl.edu.agh.models.{DbTest, DbTestLog}

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
  ).keySpace(keyspace);


  lazy val cassandraConnector = ContactPoint.embedded.noHeartbeat().keySpace("tlm")
}
