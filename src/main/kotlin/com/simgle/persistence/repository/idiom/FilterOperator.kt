package com.simgle.persistence.repository.idiom

enum class FilterOperator {
    Equals, LessThan, GreaterThan, LessThanEquals, GreaterThanEquals,
    LeftPercentLike, RightPercentLike, Like, Between, In,
    IsNull
}