package com.simgle.persistence.datasource;

import java.lang.annotation.*;

/**
 * 在controller方法入口(比如有@RequestMapping)处使用，决定当前线程(事务)使用那个数据源。<br/>
 * 这并不是真正的多数据源，一个HTTP请求只能使用一个数据库连接。
 *
 * @see DataSourceDecideInterceptor
 * @see DataSourceDecideWebMvcConfig
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UseDataSource {
    String value();
}
