ARG JAVA_VERSION="17"

FROM docker.io/library/eclipse-temurin:${JAVA_VERSION}-jdk-alpine AS builder

ARG MAVEN_CLI_OPTS="--batch-mode --errors --fail-at-end --show-version"
ENV MAVEN_OPTS="-Dhttps.protocols=TLSv1.2 -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN -Dorg.slf4j.simpleLogger.showDateTime=true -Djava.awt.headless=true"

WORKDIR /src/sitodo
COPY . .
RUN ./mvnw ${MAVEN_CLI_OPTS} -DskipTests clean package

FROM docker.io/library/eclipse-temurin:${JAVA_VERSION}-jre-alpine AS runner

ARG USER_NAME=sitodo
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

RUN addgroup -g ${USER_GID} ${USER_NAME} \
    && adduser -h /opt/sitodo -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

USER ${USER_NAME}
WORKDIR /opt/sitodo
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/sitodo/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]

LABEL org.opencontainers.image.source="https://github.com/addianto/sitodo" \
      org.opencontainers.image.licenses="MIT" \
      org.opencontainers.image.title="Sitodo"
