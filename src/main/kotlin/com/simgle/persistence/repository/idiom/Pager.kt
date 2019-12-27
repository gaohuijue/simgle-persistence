package com.simgle.persistence.repository.idiom

class Pager {
    var offset: Int? = null
    var limit: Int? = null

    companion object {
        fun build(block: Pager.() -> Unit): Pager = Pager().apply(block)
    }
}