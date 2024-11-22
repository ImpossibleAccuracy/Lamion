package com.lamion.data.database.utils

import org.jetbrains.exposed.sql.AndOp
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Op

fun allAnd(vararg expressions: Expression<Boolean>?): Op<Boolean> {
    return AndOp(expressions.toList().filterNotNull())
}