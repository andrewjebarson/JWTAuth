FROM openjdk:11
ADD target/token.jar token.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","token.jar"]