# 基础镜像
FROM openjdk:8-jdk-alpine

# 作者 邮箱
LABEL maintainer="tan90 897546244@qq.com"

# 同步时间
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
    apk update && \
    apk add wget curl vim && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo 'Asia/Shanghai' > /etc/timezone && \
    set -xe && apk --no-cache add ttf-dejavu fontconfig

# build时变量
ARG PROJECT_NAME
ARG SERVER_PORT

# run时变量
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
ENV HEAP_SIZE 256m
ENV SPRING_PROFILES_ACTIVE dev
ENV APP_NAME $PROJECT_NAME

# 容器卷
VOLUME /app

# 暴露的端口
EXPOSE $SERVER_PORT

# exec 进入容器后的目录
WORKDIR /app

# 添加jar包
ADD jar/$PROJECT_NAME*.jar /$PROJECT_NAME.jar

# CMD 运行命令
# java启动参数共分为三类
# 其一是标准参数（-），所有的JVM实现都必须实现这些参数的功能，而且向后兼容；
# 其二是非标准参数又称为扩展参数（-X），默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容；
# 其三是非Stable参数（-XX），此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用；
CMD java -Duser.timezone=Asia/Shanghai -Xms$HEAP_SIZE -Xmx$HEAP_SIZE -Xss1m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./$APP_NAME.hprof -Xloggc:./$APP_NAME.gc -jar /$APP_NAME.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE