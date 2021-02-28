FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/descatalog-0.0.1-SNAPSHOT.jar descatalog.jar
ENTRYPOINT ["java","-jar","/descatalog.jar"]