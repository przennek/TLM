package pl.edu.agh.beans;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Przemek on 20.11.2016.
 */
@Configuration
public class AuthConfig {
    private static MongoClient mongoClient;

    @Autowired
    MongoDatabase database;

    @Bean
    public MongoDatabase database() {
        final String host = "localhost";
        final String port = "27017";
        final String dbName = "test";

        MongoClientOptions.Builder options = MongoClientOptions.builder();
        options.socketKeepAlive(true);

        if (mongoClient == null) {
            mongoClient = new MongoClient(new MongoClientURI(String.format("mongodb://%s:%s", host, port)));
        }

        return mongoClient.getDatabase(dbName);
    }

    @Bean
    public MongoCollection users() {
        return database.getCollection("user");
    }
}
