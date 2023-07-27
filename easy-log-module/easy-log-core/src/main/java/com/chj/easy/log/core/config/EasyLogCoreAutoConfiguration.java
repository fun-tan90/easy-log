package com.chj.easy.log.core.config;

import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.exception.ServiceException;
import com.chj.easy.log.core.property.EasyLogEsProperties;
import com.chj.easy.log.core.property.EasyLogStreamProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 14:38
 */
@ComponentScan(EasyLogConstants.CORE_SCAN_BASE_PACKAGES)
@EnableConfigurationProperties({EasyLogEsProperties.class, EasyLogStreamProperties.class})
public class EasyLogCoreAutoConfiguration {

    @Resource
    EasyLogEsProperties easyLogEsProperties;

    @Resource
    EasyLogStreamProperties easyLogStreamProperties;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        // 处理地址
        String address = easyLogEsProperties.getAddress();
        if (!StringUtils.hasLength(address)) {
            throw new ServiceException("please config the es address");
        }
        if (!address.contains(":")) {
            throw new ServiceException("the address must contains port and separate by ':'");
        }
        String schema = !StringUtils.hasLength(easyLogEsProperties.getSchema())
                ? "http" : easyLogEsProperties.getSchema();
        List<HttpHost> hostList = new ArrayList<>();
        Arrays.stream(easyLogEsProperties.getAddress().split(","))
                .forEach(item -> hostList.add(new HttpHost(item.split(":")[0],
                        Integer.parseInt(item.split(":")[1]), schema)));

        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostList.toArray(new HttpHost[]{});

        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            // 设置心跳时间,最大连接数,最大连接路由
            Optional.ofNullable(easyLogEsProperties.getKeepAliveMillis()).ifPresent(p -> httpClientBuilder.setKeepAliveStrategy((response, context) -> p));
            Optional.ofNullable(easyLogEsProperties.getMaxConnTotal()).ifPresent(httpClientBuilder::setMaxConnTotal);
            Optional.ofNullable(easyLogEsProperties.getMaxConnPerRoute()).ifPresent(httpClientBuilder::setMaxConnPerRoute);

            // 设置账号密码
            String username = easyLogEsProperties.getUsername();
            String password = easyLogEsProperties.getPassword();
            if (StringUtils.hasLength(username) && StringUtils.hasLength(password)) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(username, password));
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            return httpClientBuilder;
        });

        // 设置超时时间之类的
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            Optional.ofNullable(easyLogEsProperties.getConnectTimeout()).ifPresent(requestConfigBuilder::setConnectTimeout);
            Optional.ofNullable(easyLogEsProperties.getSocketTimeout()).ifPresent(requestConfigBuilder::setSocketTimeout);
            Optional.ofNullable(easyLogEsProperties.getConnectionRequestTimeout())
                    .ifPresent(requestConfigBuilder::setConnectionRequestTimeout);
            return requestConfigBuilder;
        });
        return new RestHighLevelClient(builder);
    }

    @Bean
    public StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> streamMessageListenerContainerOptions() {
        return StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(easyLogStreamProperties.getPollTimeout())
                .batchSize(easyLogStreamProperties.getPullBatchSize())
                .executor(EasyLogThreadPool.EASY_LOG_FIXED_THREAD_POOL)
                .errorHandler(e -> {
                })
                .build();
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(RedisConnectionFactory factory,
                                                                                                                    StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> streamMessageListenerContainerOptions) {
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer =
                StreamMessageListenerContainer.create(factory, streamMessageListenerContainerOptions);
        listenerContainer.start();
        return listenerContainer;
    }
}
