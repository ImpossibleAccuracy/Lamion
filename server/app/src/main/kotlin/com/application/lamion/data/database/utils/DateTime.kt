package com.application.lamion.data.database.utils

import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Function

fun Column<LocalDateTime>.datePart(part: String) = DatePart(this, part)

class DatePart(
    private val expr: Expression<*>,
    private val part: String,
) : Function<Int>(IntegerColumnType()), WindowFunction<Int> {
    override fun toQueryBuilder(queryBuilder: QueryBuilder): Unit = queryBuilder {
        +"DATE_PART(\'"
        +part
        +"\', "
        +expr
        +")"
    }

    override fun over(): WindowFunctionDefinition<Int> {
        return WindowFunctionDefinition(IntegerColumnType(), this)
    }
}