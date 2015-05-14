# panda-ws
Panda websocket server.

mvn package && java -jar -Dspring.profiles.active=production -DredisPassword=xxxxxxx target/panda-ws-1.0-SNAPSHOT.jar

or 

mvn spring-boot:run -Dspring.profiles.active=production -DredisPassword=xxxx 
