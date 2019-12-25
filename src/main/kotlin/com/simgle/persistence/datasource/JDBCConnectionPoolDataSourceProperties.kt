package com.simgle.persistence.datasource

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("com.simgle.jdbc-cp")
open class JDBCConnectionPoolDataSourceProperties {
    open var dataSource: HikariConfig? = null
    open var dataSources: MutableMap<String, HikariConfig>? = null
    open var defaultDataSourceKey: String? = null
}