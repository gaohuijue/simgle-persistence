package com.simgle.persistence.repository

import com.simgle.persistence.repository.idiom.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Order
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

/**
 * 基本上与spring-data-jpa的repository的接口一致。
 */
abstract class AbstractJpaRepository<T, ID>(
        private val modelClazz: Class<T>
) : AbstractRepository() {

    open fun getOne(id: ID): T? {
        return entityManager().find(modelClazz, id)
    }

    open fun count(): Long {
        val cb = entityManager().criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        val root = query.from(modelClazz)
        query.select(cb.count(root))

        return entityManager().createQuery(query).singleResult
    }

    open fun count(filters: List<Filter>): Long {
        val cb = entityManager().criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        val root = query.from(modelClazz)
        query.select(cb.count(root))

        val predicates = buildRestrictions(filters, root, cb)
        query.where(*predicates.toTypedArray())

        return entityManager().createQuery(query).singleResult
    }

    open fun count(vararg restrictions: Predicate): Long {
        val cb = entityManager().criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        val root = query.from(modelClazz)
        query.select(cb.count(root))
        query.where(*restrictions)
        return entityManager().createQuery(query).singleResult
    }

    open fun findAll(): List<T> {
        val cb = entityManager().criteriaBuilder
        val query = cb.createQuery(modelClazz)
        query.from(modelClazz)
        return entityManager().createQuery(query).resultList
    }

    open fun findAll(filters: List<Filter>, sorters: List<Sorter>, pager: Pager): List<T> {
        val cb = entityManager().criteriaBuilder
        val query = cb.createQuery(modelClazz)
        val root = query.from(modelClazz)

        val predicates = buildRestrictions(filters, root, cb)
        query.where(*predicates.toTypedArray())

        val orders = buildOrders(sorters, root, cb)
        query.orderBy(*orders.toTypedArray())

        val typedQuery = entityManager().createQuery(query)
        typedQuery.firstResult = pager.offset ?: throw RuntimeException("分页器offset不能为空。")
        typedQuery.maxResults = pager.limit ?: throw RuntimeException("分页器limit不能为空。")

        return typedQuery.resultList
    }

    open fun findAll(field: String, value: Any): List<T> {
        val cb = entityManager().criteriaBuilder
        val query = cb.createQuery(modelClazz)
        val root = query.from(modelClazz)

        query.where(cb.equal(root.get<Any>(field), value))

        return entityManager().createQuery(query).resultList
    }

    open fun save(entity: T) {
        entityManager().persist(entity)
    }

    open fun saveAll(entities: Iterable<T>) {
        val em = entityManager()
        entities.forEach {
            em.persist(it)
        }
    }

    open fun delete(entity: T) {
        entityManager().remove(entity)
    }

    open fun deleteAll(entities: Iterable<T>) {
        val em = entityManager()
        entities.forEach {
            em.remove(it)
        }
    }

    open fun deleteAll(filters: List<Filter>) {
        val cb = entityManager().criteriaBuilder
        val criteriaDelete = cb.createCriteriaDelete(modelClazz)
        val root = criteriaDelete.from(modelClazz)
        criteriaDelete.where(*buildRestrictions(filters, root, cb).toTypedArray())
        entityManager().createQuery(criteriaDelete).executeUpdate()
    }

    open fun deleteAll(field: String, value: Any) {
        val cb = entityManager().criteriaBuilder
        val criteriaDelete = cb.createCriteriaDelete(modelClazz)
        val root = criteriaDelete.from(modelClazz)
        criteriaDelete.where(
                cb.equal(root.get<Any>(field), value)
        )
        entityManager().createQuery(criteriaDelete).executeUpdate()
    }

    private fun <T> buildRestrictions(filters: List<Filter>, root: Root<T>, cb: CriteriaBuilder): List<Predicate> {
        val predicates = mutableListOf<Predicate>()
        filters.forEach {
            val fieldName = it.field ?: throw RuntimeException("过滤器field不能为null")
            val values = it.values
            when (it.operator) {
                FilterOperator.Equals -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.equal(root.get<Any>(fieldName), values[0]))
                }
                FilterOperator.LessThan -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.lessThan(root.get<Comparable<Any>>(fieldName), values[0] as Comparable<Any>))
                }
                FilterOperator.LessThanEquals -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.lessThanOrEqualTo(root.get<Comparable<Any>>(fieldName), values[0] as Comparable<Any>))
                }
                FilterOperator.GreaterThan -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.greaterThan(root.get<Comparable<Any>>(fieldName), values[0] as Comparable<Any>))
                }
                FilterOperator.GreaterThanEquals -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.greaterThanOrEqualTo(root.get<Comparable<Any>>(fieldName), values[0] as Comparable<Any>))
                }
                FilterOperator.Between -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.between(root.get<Comparable<Any>>(fieldName), values[0] as Comparable<Any>, values[1] as Comparable<Any>))
                }
                FilterOperator.In -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    val inExpression = cb.`in`(root.get<Any>(fieldName))
                    values.forEach { value ->
                        inExpression.value(value)
                    }
                    predicates.add(inExpression)
                }
                FilterOperator.LeftPercentLike -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.like(root.get<String>(fieldName), "%${values[0]}"))
                }
                FilterOperator.RightPercentLike -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.like(root.get<String>(fieldName), "${values[0]}%"))
                }
                FilterOperator.Like -> {
                    values ?: throw RuntimeException("过滤器values不能为null")
                    predicates.add(cb.like(root.get<String>(fieldName), "%${values[0]}%"))
                }
                FilterOperator.IsNull -> {
                    predicates.add(cb.isNull(root.get<Any>(fieldName)))
                }
                else -> {
                    throw RuntimeException("过滤器操作符(${it.operator})不合法")
                }
            }
        }
        return predicates
    }

    private fun <T> buildOrders(sorters: List<Sorter>, root: Root<T>, cb: CriteriaBuilder): List<Order> {
        val orders = mutableListOf<Order>()
        sorters.forEach {
            when (it.direction) {
                SorterDirection.ASC -> {
                    orders.add(cb.asc(root.get<Any>(it.field)))
                }
                SorterDirection.DESC -> {
                    orders.add(cb.desc(root.get<Any>(it.field)))
                }
            }
        }
        return orders
    }
}