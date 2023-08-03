#!/bin/sh
# 打包跳过docker构建image
mvn clean install -DskipTests=true -DskipDocker=true
# mvn clean install -DskipTests=true