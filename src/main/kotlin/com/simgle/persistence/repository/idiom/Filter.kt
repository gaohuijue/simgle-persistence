package com.simgle.persistence.repository.idiom

import java.io.Serializable

class Filter : Serializable {
    var field: String? = null
    var values: List<Any>? = null
    var operator: FilterOperator? = null

    companion object {
        fun build(block: Filter.() -> Unit): Filter = Filter().apply(block)
    }
}

