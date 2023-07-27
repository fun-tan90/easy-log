# 索引模板

- 创建索引模板 

```shell
PUT _index_template/easy-log-template
```
```json
{
  "index_patterns": "easy-log-*",
  "priority": "1",
  "data_stream": {
  },
  "template": {
    "index.lifecycle.name": "easy-log-policy",
    "settings": {
      "number_of_shards": "5",
      "number_of_replicas": "1"
    },
    "mappings": {
      "dynamic": "false",
      "properties": {
        "id": {
          "type": "keyword"
        },
        "namespace": {
          "type": "keyword"
        },
        "appName": {
          "type": "keyword"
        },
        "content": {
          "type": "text",
          "analyzer": "ik_smart"
        },
        "currIp": {
          "type": "keyword"
        },
        "dateTime": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss.SSS"
        },
        "level": {
          "type": "keyword"
        },
        "lineNumber": {
          "type": "keyword"
        },
        "loggerName": {
          "type": "keyword"
        },
        "method": {
          "type": "keyword"
        },
        "preIp": {
          "type": "keyword"
        },
        "spanId": {
          "type": "keyword"
        },
        "threadName": {
          "type": "keyword",
          "index": false
        },
        "traceId": {
          "type": "keyword"
        }
      }
    }
  }
}
```

- 获取索引模板
```shell
GET _index_template/easy-log-template
```

# Data Stream 数据流
- 创建数据流

```shell
PUT _ilm/policy/easy-log-policy
```
```json
{
  "policy": {
    "phases": {
      "hot": {
        "actions": {
          "set_priority": {
            "priority": 100
          },
          "rollover": {
            "max_age": "1d",
            "max_size": "10gb",
            "max_docs": 10000000
          }
        }
      },
      "delete": {
        "min_age": "30d",
        "actions": {
          "delete": {
            "delete_searchable_snapshot": true
          }
        }
      }
    }
  }
}
```

- 获取数据流
```shell
GET _data_stream/easy-log-policy
```