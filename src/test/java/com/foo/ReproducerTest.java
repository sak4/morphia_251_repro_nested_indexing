package com.foo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.testcontainers.containers.MongoDBContainer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.MongoClientSettings.builder;

public class ReproducerTest {

    private MongoDBContainer mongoDBContainer;
    private String connectionString;

    private Datastore datastore;

    @Test
    public void reproduce() {
        final MyEntity myEntity = new MyEntity("parent", new Child("child"));
        datastore.save(myEntity);

        final List<Document> indices = new ArrayList<>();
        datastore.getCollection(MyEntity.class).listIndexes().into(indices);

        // Expecting 2 indices following 1.3 behavior: the default _id index and one for the embedded Child field
        Assert.assertEquals(2, indices.size());
    }

    @NotNull
    public String databaseName() {
        return "morphia_repro";
    }

    @NotNull
    public String dockerImageName() {
        return "mongo:7";
    }

    @BeforeClass
    private void setup() {
        mongoDBContainer = new MongoDBContainer(dockerImageName());
        mongoDBContainer.start();
        connectionString = mongoDBContainer.getReplicaSetUrl(databaseName());

        MongoClient mongoClient = MongoClients.create(builder()
                                                  .uuidRepresentation(UuidRepresentation.STANDARD)
                                                  .applyConnectionString(new ConnectionString(connectionString))
                                                  .build());

        datastore = Morphia.createDatastore(mongoClient, databaseName());

        System.out.println("MongoDB mapped port: " + mongoDBContainer.getMappedPort(27017));
        System.out.println("Connection string: " + connectionString);
    }
}
