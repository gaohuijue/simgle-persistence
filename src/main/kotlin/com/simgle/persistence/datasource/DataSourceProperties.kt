package com.simgle.persistence.datasource

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("com.simgle.jdbc")
open class DataSourceProperties {
    open var dataSource: HikariConfig? = null
    open var dataSources: MutableMap<String, HikariConfig>? = null
    open var defaultDataSourceKey: String? = null
}