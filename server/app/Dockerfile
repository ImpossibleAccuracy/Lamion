FROM gradle:jdk21 AS build

ENV BUILD_PATH=/usr/build

USER root
WORKDIR $BUILD_PATH

COPY gradle.properties .
COPY gradle/ gradle/

COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY server/shared server/shared
COPY server/app server/app

RUN gradle clean :server:app:build

FROM openjdk:21-jdk

ENV BUILD_PATH=/usr/build
ENV APP_PATH=/usr/app
ENV JAR_NAME=server.jar

EXPOSE $PORT

RUN mkdir $APP_PATH
WORKDIR $APP_PATH

COPY --from=build $BUILD_PATH/server/app/build/libs/*.jar $APP_PATH/$JAR_NAME

ENTRYPOINT exec java -jar $APP_PATH/$JAR_NAME
