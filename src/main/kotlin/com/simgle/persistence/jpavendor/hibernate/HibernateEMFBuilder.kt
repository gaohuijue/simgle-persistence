package com.simgle.persistence.jpavendor.hibernate

import com.simgle.core.Constant
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import javax.sql.DataSource

@Configuration
open class HibernateEMFBuilder {
    @Bean
    open fun entityManagerFactory(builder: EntityManagerFactoryBuilder, dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        return builder.dataSource(dataSource).packages(Constant.BASE_PACKAGE).build()
    }
}