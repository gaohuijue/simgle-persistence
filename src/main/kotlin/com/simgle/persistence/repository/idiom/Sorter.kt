package com.simgle.persistence.repository.idiom

import java.io.Serializable

class Sorter : Serializable {
    var field: String? = null
    var direction: SorterDirection = SorterDirection.ASC

    companion object {
        fun build(block: Sorter.() -> Unit): Sorter = Sorter().apply(block)
    }
}