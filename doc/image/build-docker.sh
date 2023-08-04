#!/bin/sh
version=`awk '/<version>[^<]+<\/version>/{gsub(/<version>|<\/version>/,"",$1);print $1;exit;}' ../../pom.xml`
export tag=$version

# 打包跳过docker构建image
mvn clean install -DskipTests=true -DskipDocker=true -f ../../pom.xml
cd easy-log-app-event/ && sh docker-build.sh && cd -
cd easy-log-app-server/ && sh docker-build.sh && cd -
