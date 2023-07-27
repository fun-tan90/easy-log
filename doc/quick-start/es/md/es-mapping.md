### 索引操作

- 创建索引
```shell
PUT /heima
{
  "mappings": {
    "properties": {
      "info":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "email":{
        "type": "keyword",
        "index": false
      },
      "name":{
        "type": "object", 
        "properties": {
          "firstname":{
            "type": "keyword"
          },
          "lastname":{
              "type": "keyword"
          }
        }
      }
    }
  }
}

{
  "acknowledged" : true,
  "shards_acknowledged" : true,
  "index" : "heima"
}
```
- 查询索引
```shell
GET /heima

{
  "heima" : {
    "aliases" : { },
    "mappings" : {
      "properties" : {
        "email" : {
          "type" : "keyword",
          "index" : false
        },
        "info" : {
          "type" : "text",
          "analyzer" : "ik_smart"
        },
        "name" : {
          "properties" : {
            "firstname" : {
              "type" : "keyword"
            },
            "lastname" : {
              "type" : "keyword"
            }
          }
        }
      }
    },
    "settings" : {
      "index" : {
        "creation_date" : "1689301561530",
        "number_of_shards" : "1",
        "number_of_replicas" : "1",
        "uuid" : "c_VPP9t6S9KyKqYloh4fow",
        "version" : {
          "created" : "7090399"
        },
        "provided_name" : "heima"
      }
    }
  }
}
```
- 修改索引(新增)
```shell
# 无法修改字段，只能新增字段
PUT /heima/_mapping
{
  "properties": {
      "sex":{
        "type": "keyword"
      }
    }
}

{
  "acknowledged" : true
}

```
- 删除索引
```shell
DELETE /heima

{
  "acknowledged" : true
}
```