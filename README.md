Build with: ./mvnw build package

This launches tests too.

Then in one terminal launch server : java -jar server/target/quarkus-app/quarkus-run.jar 

In another terminal launch client: java -jar client-test/target/quarkus-app/quarkus-run.jar 

Finally curl http://localhost8083/command/ARG where ARG in 
APIGET GET NOCLOSEGET APIPOST POST NOCLOSEPOST APISTREAM STREAM NOCLOSESTREAM



