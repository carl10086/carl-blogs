## 1-Intro

项目的 `demo` 是一个 关系图谱功能设计.

## 2-Purpose

`fromId->relations->toId` 是唯一id

这个系统应该重读取.

1. 根据 `fromId` 查询所有的内容 ;
2. 根据 `fromId` + `relation` 查询 ;
3. 判断 `fromId` + `relation` + `toId` 是否存在 ;
4. 根据 `fromId` + `relation` + `createAt 区间` 查询 ;


## 3-Solution By Kv

```cassandraql
CREATE TABLE relations
(
    fromId   text,
    relation text,
    toId     text,
    createAt bigint,
    PRIMARY KEY (fromId, relation, toId)
);

CREATE MATERIALIZED VIEW relations_by_createAt AS
SELECT fromId, relation, createAt, toId
FROM relations
WHERE fromId IS NOT NULL
  AND relation IS NOT NULL
  AND createAt IS NOT NULL
  AND toId IS NOT NULL
PRIMARY KEY (fromId, relation, createAt, toId)
WITH CLUSTERING ORDER BY (createAt DESC);
```

下面是一个 `demo`

```cassandraql
-- 准备数据
INSERT into relations(fromId, relation, toId, createAt) VALUES ('u:1', 'create', 'n:1', 10);
INSERT into relations(fromId, relation, toId, createAt) VALUES ('u:1', 'create', 'n:2', 11);
INSERT into relations(fromId, relation, toId, createAt) VALUES ('u:1', 'create', 'n:3', 13);


-- 查询用户的 u:1 所有关系
SELECT * FROM relations where fromId = 'u:1';

-- 查询 某个用户 u:1 创建的所有 笔记
SELECT * FROM relations where fromId = 'u:1' and relation ='create';

-- 查询某个用户 u:1 是否创建了笔记 n:1
SELECT * FROM relations where fromId = 'u:1' and relation = 'create' and toId = 'n:1';

-- 查询  某个用户 u:1 最近创建的 1 个 note
SELECT * FROM relations_by_createAt where fromId = 'u:1' AND relation='create' AND createAt> 10 ORDER BY  createAt DESC limit 1;
```

评估:

1. 直接基于 `scylladb` 的性能应该非常的顶 ;
2. 缺点: 不支持复杂的图查询，因为没有图索引 ;
3. 如果有热点数据，可以考虑把 `PRIMARY KEY ((fromId, relation), toId)` 来优化数据倾斜问题, 可能会好一些 ;
4. 可以考虑 利用再创建一个视图 `(toId, relation, fromId)` 就直接支持了 逆向关系的查询，例如被关注，被收藏 ;



```

## 4. Solution By Graph