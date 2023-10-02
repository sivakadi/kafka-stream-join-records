# kafka-stream-join-records
This project joins order event messages from two different topics and send the joined order event message to one topic.

#### Do the following to rung this application Spring Cloud Stream Application

Build this application.

```
./mvnw clean package
```

Then run the resultant jar.

```
java -jar target/kafka-ssl-demo-0.0.1-SNAPSHOT.jar
```

Or run it from an IDE.
This application also has two suppliers configured to simulate the production of necessary messages at the expected topics. There is also a consumer configured to consume the   
joined messages and printing them on the application's console.