#!/bin/sh
# 打包跳过docker构建image
export tag="1.1.3"
mvn clean install -DskipTests=true -DskipDocker=true -f ../pom.xml
cd image/easy-log-app-event/ && sh docker-build.sh && cd -
cd image/easy-log-app-server/ && sh docker-build.sh && cd -