```shell
java -javaagent:D:\code\easy-log\doc\apm\elastic-apm-agent-1.31.0.jar -Delastic.apm.service_name=easy-log -Delastic.apm.server_urls=http://localhost:8200 -Delastic.apm.environment=test -Delastic.apm.application_packages=com.chj.easy.log -jar easy-log-app-1.1.7.jar
```