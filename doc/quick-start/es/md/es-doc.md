### 文档CRUD

- 新增文档
```shell
POST /heima/_doc/1
{
  "email": "897546244@qq.com",
  "info": "我是中国人，我骄傲",
  "name":{
    "firstname":"陈",
    "lastname":"浩杰"
  },
  "sex": "男"
}

{
  "_index" : "heima",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 25,
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "_seq_no" : 24,
  "_primary_term" : 1
}

```
- 查询文档
```shell
GET /heima/_doc/1

{
  "_index" : "heima",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 15,
  "_seq_no" : 14,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "email" : "897546244@qq.com",
    "info" : "我是中国人，我骄傲",
    "name" : {
      "firstname" : "陈",
      "lastname" : "浩杰"
    },
    "sex" : "男"
  }
}

```
- 删除文档
```shell
DELETE /heima/_doc/1

{
  "_index" : "heima",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 16,
  "result" : "deleted",
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "_seq_no" : 15,
  "_primary_term" : 1
}
```
- 修改文档
```shell
# 全量修改
PUT /heima/_doc/1
{
  "email": "897546244@qq.com",
  "info": "我是中国人，我骄傲",
  "name":{
    "firstname":"陈",
    "lastname":"浩杰1"
  },
  "sex": "男1"
}

{
  "_index" : "heima",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 2,
  "result" : "updated",
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "_seq_no" : 27,
  "_primary_term" : 1
}

#局部更新
POST /heima/_update/2
{
  "doc": {
    "email": "1@qq.com"
  }
}

{
  "_index" : "heima",
  "_type" : "_doc",
  "_id" : "2",
  "_version" : 2,
  "result" : "updated",
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "_seq_no" : 29,
  "_primary_term" : 1
}
```