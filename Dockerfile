FROM hub.kce.ksyun.com/ezone-public/openjdk:8u302-jre-alpine3.15

ARG APPLICATION_FILE="output/*-application.tar.gz"
ARG JAVA_DEBUG_PORT=8209
ARG JMX_REMOTE_PORT=8099
ARG JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
ARG JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote.port=${JMX_REMOTE_PORT} -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
ARG JAVA_DEBUG_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=${JAVA_DEBUG_PORT},server=y,suspend=n"
ARG JAVA_HEAP_OPTS="-Xmx512m -Xms512m -Xmn128m -Xss256k -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m"
ARG JAVA_MEM_OPTS="-server ${JAVA_HEAP_OPTS} -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"
ARG APM_MONITOR="disable"
ARG APP_NAME=""
ARG APP_DIR="/app"
ARG APP_CONFIG=""
ARG ENV="dev"
ARG RELEASE_VERSION="1.0.0"
ARG DOSTART="false"

# JVM ENV
ENV JAVA_DEBUG_PORT=${JAVA_DEBUG_PORT} \
    JMX_REMOTE_PORT=${JMX_REMOTE_PORT} \
    JAVA_OPTS=${JAVA_OPTS} \
    JAVA_JMX_OPTS=${JAVA_JMX_OPTS} \
    JAVA_DEBUG_OPTS=${JAVA_DEBUG_OPTS} \
    JAVA_HEAP_OPTS=${JAVA_HEAP_OPTS} \
    JAVA_MEM_OPTS=${JAVA_MEM_OPTS} \
    APM_MONITOR=${APM_MONITOR} \
    APP_NAME=${APP_NAME} \
    APP_DIR=${APP_DIR} \
    APP_CONFIG=${APP_CONFIG} \
    ENV=${ENV} \
    VERSION=${RELEASE_VERSION} \
    DOSTART=${DOSTART} \
    BACKUP="false" \
    NOHUP="false"

RUN set -x && \
    apk add bash tzdata && \
    mkdir -p /app /app/logs /app/data && \
    rm -rf /var/cache/apk/*

ADD ${APPLICATION_FILE} /app
VOLUME /app/logs /app/data
WORKDIR /app

ENTRYPOINT ["/bin/sh", "/app/bin/start.sh"]
CMD ["debug"]
