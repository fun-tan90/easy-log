#!/bin/sh
cd ../../easy-log-client/easy-log-appender
mvn clean install org.apache.maven.plugins:maven-deploy-plugin:2.8.2:deploy -DskipTests
