#!/bin/sh
# 构建过程变量SERVER_PORT、PROJECT_NAME、MQTT_TCP_PORT、MQTT_WEBSOCKET_PORT
# 启动时变量HEAP_SIZE、SPRING_PROFILES_ACTIVE
# -t 后面为镜像名称和TAG
buildArg="--build-arg PROJECT_NAME=easy-log-app --build-arg SERVER_PORT=1124"
imageName="registry.cn-hangzhou.aliyuncs.com/tan90/easy-log"

docker build $buildArg . -t $imageName:$tag
docker tag $imageName:$tag $imageName:latest
docker login --username=陈浩杰1991 --password qingquaner151628  registry.cn-hangzhou.aliyuncs.com
docker push $imageName:$tag
docker push $imageName:latest