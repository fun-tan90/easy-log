```http request
# 查看es状态
GET /
```

```http request
# 分词器测试
POST /_analyze

{
  "text": "我是中国人，我爱中国",
  "analyzer": "standard"
}
```

