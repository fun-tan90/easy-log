```shell
java -javaagent:C:\IDE\elastic-apm-agent-1.42.0.jar -Delastic.apm.service_name=easy-log -Delastic.apm.server_urls=http://172.16.8.31:8200 -Delastic.apm.environment=dev -Delastic.apm.application_packages=com.chj.easy.log -jar easy-log-app-1.1.7.jar
```