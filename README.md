# kafka-stream-join-records
This project joins two different order messages to one order record.

### Do the following to rung this application Spring Cloud Stream Application

Build this application.

```
./mvnw clean package
```

Then run the resultant jar.

```
java -jar target/kafka-ssl-demo-0.0.1-SNAPSHOT.jar
```

Or Run it from an IDE.
This application also has two suppliers configured to simulate the necessary order messages arriving the expected topics. 
As a result of this, you should see the joined message getting printed on the application's console.