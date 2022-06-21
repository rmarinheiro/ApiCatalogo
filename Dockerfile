FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/catalogo-0.0.1-SNAPSHOT.jar catalogo.jar
ENTRYPOINT ["java","-jar","/catalogo.jar"]