package com.simgle.persistence.repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * 可以根据业务自己扩展方便的增删改查，少一层转换就快一些。
 * 常用的简单的增删改查如下
 * @see AbstractJpaRepository
 */
abstract class AbstractRepository {
    @PersistenceContext
    private val entityManager: EntityManager? = null

    protected fun entityManager(): EntityManager {
        return entityManager!!
    }
}