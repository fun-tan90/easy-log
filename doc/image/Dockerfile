# 基础镜像
FROM openjdk:8-jdk-alpine

# 作者 邮箱
LABEL maintainer="tan90 897546244@qq.com"

#同步时间
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
    apk update && apk add wget curl vim && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone \

# build时变量
ARG PROJECT_NAME
ARG SERVER_PORT
ARG MQTT_TCP_PORT
ARG MQTT_WEBSOCKET_PORT

# run时变量
ENV HEAP_SIZE 256m
ENV SPRING_PROFILES_ACTIVE dev
ENV APP_NAME $PROJECT_NAME

# 容器卷
VOLUME /app

# 暴露的端口
EXPOSE $SERVER_PORT $MQTT_TCP_PORT $MQTT_WEBSOCKET_PORT

# exec 进入容器后的目录
WORKDIR /app

# 添加jar包
COPY app/$PROJECT_NAME*.jar ./$PROJECT_NAME.jar

# ENTRYPOINT 运行命令
CMD java -jar /app/$APP_NAME.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE -Xms$HEAP_SIZE -Xmx$HEAP_SIZE -Xss1m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:./$APP_NAME.gc -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./$APP_NAME.hprof -Duser.timezone=Asia/Shanghai