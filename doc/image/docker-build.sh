#!/bin/sh
# 构建过程变量SERVER_PORT、PROJECT_NAME、MQTT_TCP_PORT、MQTT_WEBSOCKET_PORT
# 启动时变量HEAP_SIZE、SPRING_PROFILES_ACTIVE
# -t 后面为镜像名称和TAG
tag="1.1.3"
docker build --build-arg PROJECT_NAME=easy-log-server --build-arg SERVER_PORT=1203 --build-arg MQTT_TCP_PORT=1883 --build-arg MQTT_WEBSOCKET_PORT=8083 . -t registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log:$tag
docker tag registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log:$tag registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log:latest
docker login --username=陈浩杰1991 --password qingquaner151628  registry.cn-hangzhou.aliyuncs.com
docker push registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log:$tag
docker push registry.cn-hangzhou.aliyuncs.com/chenhj/easy-log:latest