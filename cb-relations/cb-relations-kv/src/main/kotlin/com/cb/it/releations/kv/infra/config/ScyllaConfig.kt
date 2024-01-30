package com.cb.it.releations.kv.infra.config

import com.cb.base.scylla.Scylla4SessionBean
import com.cb.it.releations.kv.infra.persist.RelationDao
import com.datastax.oss.driver.api.mapper.annotations.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Mapper
interface ScyllaMapper {
    @DaoFactory
    fun relationDao(
        @DaoKeyspace keyspace: String?, @DaoTable table: String?, @DaoProfile profileName: String?
    ): RelationDao
}

@Configuration
open class ScyllaConfig {

    //  @Bean
    //  public SyllaSessionBean blogDbScyllaSession() {
    //    /*暂时不修改默认配置, 默认的参数已经不错了, https://duitang.feishu.cn/docs/doccnCc83KKHEYM9o8YiorFN2Mf */
    //    return ScyllaCfgHelper.buildDefault(this.syllaBlogDbUrl, localDc);
    //  }
    @Bean
    open fun scyllaSessionBean(
        @Value("\${biz.scylla.testDB}") url: String,
        @Value("\${biz.scylla.testDB.dc}") dc: String,
    ): Scylla4SessionBean {
        logger.info("blogDbScylla4Session is building, ips:${url}, dc:${dc}")
        return Scylla4SessionBean(
            com.cb.base.scylla.ScyllaConfig(
                ips = url.split(","),
                dc = dc
            )
        )
    }

    @Bean
    open fun scyllaMapper(
        scyllaSessionBean: Scylla4SessionBean,
    ): ScyllaMapper {
        return ScyllaMapperBuilder(scyllaSessionBean.session).build()
    }

    @Bean
    open fun relationDao(
        scyllaMapper: ScyllaMapper,
    ): RelationDao {
        return scyllaMapper.relationDao("test", "relation", "soft")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ScyllaConfig::class.java)
    }
}