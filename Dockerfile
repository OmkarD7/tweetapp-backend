FROM openjdk:8
ADD target/tweetapp-0.0.1.jar tweetapp-0.0.1.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "tweetapp-0.0.1.jar"]
