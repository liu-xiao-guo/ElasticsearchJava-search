This is a very simple sample application showing how to do DSL queries for Elasticseach. Before you run the application, please index the following document into Elasticsearch using Kibana:

PUT twitter
{
  "mappings": {
    "properties": {
      "DOB": {
        "type": "date"
      },
      "address": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "age": {
        "type": "long"
      },
      "city": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "country": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "location": {
        "type": "geo_point"
      },
      "message": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "province": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "uid": {
        "type": "long"
      },
      "user": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      }
    }
  }
}

POST twitter/_bulk
{"index":{"_id":1}}
{"user":"双榆树-张三","DOB":"1992-08-03","message":"今儿天气不错啊，出去转转去","uid":1,"age":30,"city":"北京","province":"北京","country":"中国","address":"中国北京市海淀区","location":{"lat":"39.970718","lon":"116.325747"}}
{"index":{"_id":2}}
{"user":"东城区-老刘","DOB":"1990-07-14","message":"出发，下一站云南！","uid":2,"age":32,"city":"北京","province":"北京","country":"中国","address":"中国北京市东城区台基厂三条3号","location":{"lat":"39.904313","lon":"116.412754"}}
{"index":{"_id":3}}
{"user":"东城区-李四","DOB":"1997-09-23","message":"happy birthday!","uid":3,"age":25,"city":"北京","province":"北京","country":"中国","address":"中国北京市东城区","location":{"lat":"39.893801","lon":"116.408986"}}
{"index":{"_id":4}}
{"user":"朝阳区-老贾","DOB":"1980-06-30","message":"123,gogogo","uid":4,"age":42,"city":"北京","province":"北京","country":"中国","address":"中国北京市朝阳区建国门","location":{"lat":"39.718256","lon":"116.367910"}}
{"index":{"_id":5}}
{"user":"朝阳区-老王","DOB":"1996-06-18","message":"Happy BirthDay My Friend!","uid":5,"age":26,"city":"北京","province":"北京","country":"中国","address":"中国北京市朝阳区国贸","location":{"lat":"39.918256","lon":"116.467910"}}
{"index":{"_id":6}}
{"user":"虹桥-老吴","DOB":"2000-04-05","message":"好友来了都今天我生日，好友来了,什么 birthday happy 就成!","uid":7,"age":22,"city":"上海","province":"上海","country":"中国","address":"中国上海市闵行区","location":{"lat":"31.175927","lon":"121.383328"}}
