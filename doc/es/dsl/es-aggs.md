### 聚合查询

```shell
GET /daily-easy-log-2023-07-15/_search
{
  "size": 0, 
  "aggs": {
    "numAgg": {
      "terms": {
        "field": "level",
        "size": 5
        , "order": {
          "_key": "desc"
        }
      }
    }
  }
}
```