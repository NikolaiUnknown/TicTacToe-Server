FROM gradle:jdk21 AS build
WORKDIR /server
COPY ./build.gradle ./
COPY ./settings.gradle ./
RUN gradle build --dry-run || return 0
COPY . .
RUN gradle build

FROM alpine/java:21-jre AS final
WORKDIR /server
COPY --from=build /server/build/libs/*-SNAPSHOT.jar /server/server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","server.jar"]