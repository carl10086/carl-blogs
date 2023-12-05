package com.cb.base.scylla

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.config.DriverConfigLoader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import java.net.InetSocketAddress


data class ScyllaConfig(
    val ips: List<String>,
    val dc: String = "",
    val classPathConf: String = "scylladb/application.conf",
)

open class Scylla4SessionBean(
    private val config: ScyllaConfig,
) : InitializingBean, DisposableBean {

    lateinit var session: CqlSession
        private set

    override fun afterPropertiesSet() {
        val builder = CqlSession.builder().addContactPoints(config.ips.map {
            InetSocketAddress(it, 9042)
        })

        if (config.dc.isNotBlank())
            builder.withLocalDatacenter(config.dc)

        builder.withConfigLoader(DriverConfigLoader.fromClasspath(config.classPathConf))

        this.session = builder.build()
    }

    override fun destroy() {
        session.close()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ScyllaConfig::class.java)
    }

}