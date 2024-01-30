package com.cb.it.releations.kv

import com.cb.base.scylla.Scylla4SessionBean
import com.cb.base.scylla.ScyllaConfig
import com.cb.it.releations.kv.infra.config.ScyllaMapperBuilder
import com.cb.it.releations.kv.infra.persist.RelationDao
import org.junit.jupiter.api.Test

internal class RelationDaoTest {


    private var relationDao: RelationDao

    init {
        val sessionBean = Scylla4SessionBean(
            ScyllaConfig(listOf("10.200.68.4"), "JDC_SHANGHAI")
        ).apply { afterPropertiesSet() }

        val mapper = ScyllaMapperBuilder(sessionBean.session).build()
        relationDao = mapper.relationDao(
            "test",
            "relations",
            "soft"
        )
    }

    @Test
    fun queryByFromId() {
        relationDao.queryByFromId("u:1").forEach { println(it) }
    }

    @Test
    fun countByFromId() {
        println(relationDao.countByFromId("u:1"))
        println(relationDao.countByFromId("u:4"))
    }

    @Test
    fun queryByFromIdAndRelationOrderByCreateAt() {
        relationDao.queryByFromIdAndRelationOrderByCreateAt(
            "u:1",
            "create",
            10,
            15,
            1
        ).forEach { println(it) }
    }
}