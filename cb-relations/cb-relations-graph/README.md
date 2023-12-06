## 1. Intro

`gremlin` 的语法风格应该就是 `Groovy`, 所以基本上命令行就是 **代码实现**

> 准备 schema

```groovy
graph = JanusGraphFactory.open('conf/janusgraph-inmemory.properties')
mgmt = graph.openManagement()
mgmt.printSchema()

// 1. 边
// 关注边

follow = mgmt.makeEdgeLabel('follow').multiplicity(MULTI).make()

// 收藏边
fav = mgmt.makeEdgeLabel('fav').multiplicity(MULTI).make()
// 点赞边
like=mgmt.makeEdgeLabel('like').multiplicity(MULTI).make()

// 2. 点
userLabel = mgmt.makeVertexLabel('user').make()
noteLabel = mgmt.makeVertexLabel('note').make()

// 3. 属性

// 创建时间
createAt = mgmt.makePropertyKey('createAt').dataType(Long.class).cardinality(Cardinality.SINGLE).make()
// userId
userId = mgmt.makePropertyKey('userId').dataType(String.class).cardinality(Cardinality.SINGLE).make()

// noteId
noteId = mgmt.makePropertyKey('noteId').dataType(String.class).cardinality(Cardinality.SINGLE).make()

// 4. 定义约束, userId 的唯一性 和 noteId 的唯一性
mgmt.buildIndex('byUserIdUnique', Vertex.class).addKey(userId).unique().indexOnly(userLabel).buildCompositeIndex()
mgmt.buildIndex('byNoteIdUnique', Vertex.class).addKey(noteId).unique().indexOnly(noteLabel).buildCompositeIndex()

mgmt.commit()
```


> 添加数据


```groovy
// 1. 增加用户
u1 = graph.addVertex(T.label, 'user', 'userId', 'u1')
u2 = graph.addVertex(T.label, 'user', 'userId', 'u2')
u3 = graph.addVertex(T.label, 'user', 'userId', 'u3')
u4 = graph.addVertex(T.label, 'user', 'userId', 'u4')


g = graph.traversal()  // 获取一个新的图遍历对象
users = g.V().hasLabel('user').valueMap('userId').toList()  // 获取所有 "user" 顶点的 "userId" 属性


// 2. 用户关注用户
u1.addEdge('follow', u2)
u1.addEdge('follow', u3)
u2.addEdge('follow', u4)

g = graph.traversal()  // 获取一个新的图遍历对象
g.V().has('user', 'userId', 'u1').out('follow').values('userId').toList()


// 3. 增加笔记
n1 = graph.addVertex(T.label, 'note', 'noteId', 'n1');
n2 = graph.addVertex(T.label, 'note', 'noteId', 'n2');
n3 = graph.addVertex(T.label, 'note', 'noteId', 'n3');
n4 = graph.addVertex(T.label, 'note', 'noteId', 'n4');


// 4. 增加收藏关系
//
u1.addEdge('fav', n1);
u2.addEdge('fav', n1);
u2.addEdge('fav', n2);
u2.addEdge('fav', n3);
u3.addEdge('fav', n3);
u3.addEdge('fav', n4);
graph.tx().commit();
```

> 简单的2个查询.


```groovy
// 1. 深度查询
// 思路: 从 u1 出发，找到 u1 关注的人和  二度关注的人 然后 union 起来

g.V().has('user', 'userId', 'u1').union(out('follow'), out('follow').out('follow')).dedup().values('userId').toList()


// 2. 基于 用户的关注关系 给 u1 推荐笔记
// 思路:
// 1. 找到用户关注的人
// 2. 查询这些人收藏的笔记
g.V().has('user', 'userId', 'u1').out('follow').out('fav').toList()

```
