### 搜素操作

```
{
  "query":{
    "查询类型" : "查询条件"
  }
}
```
- 全文检索
```shell
# match
GET /daily-easy-log-2023-07-14/_search
{
  "query":{
    "match" : {
      "loggerName": "DemoController"
    }
  }
}

# multi_match fields数组过多时效率不高，不推荐使用，建议使用copy_to的方式
GET /daily-easy-log-2023-07-14/_search
{
  "query":{
    "multi_match": {
      "query": "test",
      "fields": ["appEnv","appName"]
    }
  }
}

```

- 精确查询

```shell
# 精确查询

# term(词条)查询
GET /daily-easy-log-2023-07-14/_search
{
  "query":{
    "term": {
      "appEnv": {
        "value": "dev"
      }
    }
  }
}

# term(词条)查询
GET /daily-easy-log-2023-07-14/_search
{
  "query":{
    "term": {
      "appEnv": {
        "value": "dev"
      }
    }
  }
}

# range(范围)查询
GET /daily-easy-log-2023-07-14/_search
{
  "query":{
    "range": {
      "dateTime": {
        "gte": "2023-07-14 16:15:17.000",
        "lte": "2023-07-14 16:35:17.000"
      }
    }
  }
}
```
- 地理查询
```
# 距离中心查询
{
  "query":{
    "geo_distance": {
      "distance": "15km",
      "FILED":"32.21,121.5"
    }
  }
}

```
- 相关性算分查询
```shell


```
- bool查询