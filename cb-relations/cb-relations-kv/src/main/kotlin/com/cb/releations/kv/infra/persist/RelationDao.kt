package com.cb.releations.kv.infra.persist

import com.datastax.oss.driver.api.mapper.annotations.*
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention
import java.util.stream.Stream

@Entity
@NamingStrategy(
    convention = [NamingConvention.SNAKE_CASE_INSENSITIVE]
)
data class RelationDO(
    val fromId: String,
    val toId: String,
    val relation: String,
    val createAt: Long,
)

@Dao
interface RelationDao {

    //    @Query("select * from test.relation where from_id = :fromId")
    @Query("select * from \${qualifiedTableId} where from_id = :fromId")
    fun queryByFromId(fromId: String): Stream<RelationDO>

    @Query("select count(*) from \${qualifiedTableId} where from_id = :fromId")
    fun countByFromId(fromId: String): Long

    @Query("select * from \${qualifiedTableId} where from_id = :fromId and relation = :relation")
    fun queryByFromIdAndRelation(fromId: String, relation: String): Stream<RelationDO>

    @Query("select count(*) from \${qualifiedTableId} where from_id = :fromId and relation = :relation")
    fun countByFromIdAndRelation(fromId: String, relation: String): Long


    @Query("select * from test.relations_by_create_at where from_id = :fromId and relation = :relation and create_at > :createAtStart and create_at < :createAtEnd order by create_at limit :size")
    fun queryByFromIdAndRelationOrderByCreateAt(
        fromId: String,
        relation: String,
        createAtStart: Long,
        createAtEnd: Long,
        size: Int
    ): Stream<RelationDO>


    @Insert
    fun save(data: RelationDO)


}