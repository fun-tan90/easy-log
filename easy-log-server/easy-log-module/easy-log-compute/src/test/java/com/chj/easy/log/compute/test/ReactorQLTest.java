package com.chj.easy.log.compute.test;

import cn.hutool.json.JSONUtil;
import org.jetlinks.reactor.ql.ReactorQL;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/4 9:30
 */
public class ReactorQLTest {

    public static void main(String[] args) {
        Map<String, String> log = new HashMap<>();
        log.put("appName", "easy-log-example-boot2-logback");
        log.put("namespace", "test");
        log.put("content", "我是中国人");
        log.put("timeStamp", "1691108558208");

        String sql = "select * from test where (timeStamp >= '1691110778899') and (namespace = 'test') and (appName in ('app01', 'app02')) and (level in ('INFO', 'DEBUG')) and (level like '%hello%') and (lineNumber = '23') and (currIp in ('192.168.0.1')) and (content like '%我%'or content like '%是%'or content like '%中国人%'or content like '%中国%'or content like '%国人%')";

        ReactorQL.builder()
                .sql(sql) //按每秒分组,并计算流中数据平均值,如果平均值大于2则下游收到数据.
                .build()
                .start(Flux.just(log))
                .subscribe(n -> {
                            System.out.println(JSONUtil.toJsonPrettyStr(n));
                        }
                        , err -> {
                            System.out.println("errorConsumer = " + err);
                        }, () -> {
                            System.out.println("completeConsumer");
                        });
    }
}
