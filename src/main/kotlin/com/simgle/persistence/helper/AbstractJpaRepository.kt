package com.simgle.persistence.helper

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

abstract class AbstractJpaRepository<T, ID>(
        private val modelClazz: Class<T>
) {
    @PersistenceContext
    private val entityManager: EntityManager? = null

    protected fun entityManager(): EntityManager {
        return entityManager!!
    }

    open fun getOne(id: ID): T? {
        return entityManager().find(modelClazz, id)
    }
}