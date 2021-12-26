FROM eclipse-temurin:17-alpine as build
RUN apk add --no-cache bash maven

WORKDIR /helidon

ENV MAVEN_CLI_OPTS="-B -e -ntp"

ADD pom.xml .
ADD src src
RUN mvn $MAVEN_CLI_OPTS package -Pjlink-image -Dmaven.test.skip

# ---
FROM alpine:3.14

RUN addgroup -S helidon && adduser -S helidon -G helidon

WORKDIR /helidon
USER helidon
COPY --from=build /helidon/target/scd-example-jri ./
ENTRYPOINT ["bin/java", "-XX:SharedArchiveFile=lib/start.jsa", "-Xshare:auto", "-jar", "app/scd-example.jar"]
EXPOSE 8080
