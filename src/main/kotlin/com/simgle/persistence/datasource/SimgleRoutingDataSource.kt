package com.simgle.persistence.datasource

import com.simgle.core.Constant
import com.simgle.core.tool.ThreadBinds
import com.zaxxer.hikari.HikariDataSource
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
open class SimgleRoutingDataSource(
        private val JDBCConnectionPoolDataSourceProperties: JDBCConnectionPoolDataSourceProperties
) : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any? {
        return ThreadBinds.get(Constant.DATA_SOURCE_KEY)
    }

    @PostConstruct
    open fun postConstruct() {
        val config = JDBCConnectionPoolDataSourceProperties.dataSource
        if (config != null) {
            this.setDefaultTargetDataSource(HikariDataSource(config))
            this.setTargetDataSources(mutableMapOf())
        } else {
            val defaultDataSourceKey = JDBCConnectionPoolDataSourceProperties.defaultDataSourceKey
                    ?: throw RuntimeException("多数据源必须设置默认数据源。")
            val multiDataSources = mutableMapOf<Any, Any>()
            (JDBCConnectionPoolDataSourceProperties.dataSources ?: throw RuntimeException("没有多数据源配置")).forEach {
                multiDataSources[it.key] = HikariDataSource(it.value)
            }
            val defaultDataSource = multiDataSources[defaultDataSourceKey]
                    ?: throw RuntimeException("默认数据源key没有对应到多数据源配置。")

            this.setDefaultTargetDataSource(defaultDataSource)
            this.setTargetDataSources(multiDataSources)
        }

        this.afterPropertiesSet()
    }
}