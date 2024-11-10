###########################
# Build the Spring Server #
###########################

FROM maven:3.8.6-openjdk-18 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY src/main/resources/stockfish-ubuntu-x86-64 /stockfish
RUN chmod +x /stockfish
RUN mvn -f /usr/src/app/pom.xml clean install


###################
# Run it with JDK #
###################

#FROM openjdk:18-jdk-alpine
#VOLUME /tmp
#COPY --from=build /usr/src/app/target/* /usr/app/
#COPY --from=build /stockfish /usr/app/stockfish
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/usr/app/backend-0.0.1-SNAPSHOT.jar"]

FROM ubuntu:latest


RUN apt-get update && \
    apt-get install -y openjdk-18-jdk

WORKDIR /usr/app

VOLUME /tmp

COPY --from=build /usr/src/app/target/* /usr/app/

COPY --from=build /stockfish /usr/app/stockfish

EXPOSE 8080


ENTRYPOINT ["java", "-jar", "/usr/app/backend-0.0.1-SNAPSHOT.jar"]

