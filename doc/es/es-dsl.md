```http request
# 查看es
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

```
安装ik分词器
https://github.com/medcl/elasticsearch-analysis-ik
```
