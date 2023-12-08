## 1. Intro


## 2.  completion suggester

PUT cb-sch-completion
```json
{
  "mappings": {
    "properties": {
      "suggest": {
        "type": "completion",
        "analyzer": "whitespace",
        "search_analyzer": "whitespace"
      }
    }
  }
}
```


POST cb-sch-completion/_bulk
```json lines
{ "index" : { } }
{"suggest":"苹果 耳机"}
{ "index" : { } }
{"suggest":"苹果 电脑"}
{ "index" : { } }
{"suggest":"苹果 手机"}
{ "index" : { } }
{"suggest":"苹果 手表"}
{ "index" : { } }
{"suggest":"苹果 平板电脑"}
{ "index" : { } }
{"suggest":"小米 耳机"}
{ "index" : { } }
{"suggest":"小米 电脑"}
{ "index" : { } }
{"suggest":"小米 手机"}
{ "index" : { } }
{"suggest":"小米 手表"}
{ "index" : { } }
{"suggest":"小米 平板电脑"}
```


GET cb-sch-completion/_search
```json
{
  "_source": ["suggest"],
  "suggest": {
    "tst": {
      "prefix": "小米 电脑",
      "completion": {
        "field": "suggest",
        "size": 3,
        "fuzzy": {
          "fuzziness": "AUTO"
        } 
      }
    }
  }
}
```