#!/bin/sh
# 构建过程变量SERVER_PORT、PROJECT_NAME、MQTT_TCP_PORT
# 启动时变量HEAP_SIZE、SPRING_PROFILES_ACTIVE
# -t 后面为镜像名称和TAG
docker build --build-arg PROJECT_NAME=easy-log-server --build-arg SERVER_PORT=1203 --build-arg MQTT_TCP_PORT=1883 --build-arg MQTT_WEBSOCKET_PORT=8083 . -t registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log-server:1.1.1
docker push registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log-server:1.1.1
docker tag registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log-server:1.1.1 registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log-server:latest
docker push registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log-server:latest