package com.application.lamion.data.datasource

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.domain.model.Id
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.div
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.kotlin.datetime.KotlinLocalDateColumnType

object UserDataSource {
    fun isUserCountingAsTotalWhere(start: LocalDateTime, end: LocalDateTime): Op<Boolean> =
        EventTable.createdAt.between(start, end)

    fun isUserCountingAsTotalHaving(): Op<Boolean> =
        EventTable.id.count().greaterEq(0)

    fun isUserCountingAsActiveWhere(start: LocalDateTime, end: LocalDateTime): Op<Boolean> =
        EventTable.createdAt.between(start, end)

    fun isUserCountingAsActiveHaving(
        sourceColumn: Expression<Double>,
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): GreaterOp = sourceColumn.greater(
        getAverageEventsPerUserCountSubquery(
            projectId = projectId,
            start = start,
            end = end,
        )
    )

    fun isUserCountingAsActiveGroupByDateHaving(
        sourceColumn: Expression<Double>,
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): GreaterOp = sourceColumn.greater(
        getAverageEventsPerUserCountSubqueryGroupByDate(
            projectId = projectId,
            start = start,
            end = end,
        )
    )

    private fun getAverageEventsPerUserCountSubquery(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Expression<Double?> {
        val resultQuery = EventTable.id.count().castTo(DoubleColumnType())
            .div(EventTable.user.countDistinct().castTo(DoubleColumnType()))
            .alias("event_count")

        return EventTable
            .innerJoin(FunctionTable)
            .select(resultQuery)
            .where(
                FunctionTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(start, end))
            ).let {
                wrapAsExpression(it)
            }
    }

    private fun getAverageEventsPerUserCountSubqueryGroupByDate(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): ExpressionWithColumnType<Double> {
        val resultQuery = EventTable.id.count().castTo(DoubleColumnType())
            .div(EventTable.user.countDistinct().castTo(DoubleColumnType()))
            .alias("event_count")

        val dateQuery = EventTable.createdAt.castTo(KotlinLocalDateColumnType()).alias("event_date")

        val subquery = EventTable
            .innerJoin(FunctionTable)
            .select(
                resultQuery,
                dateQuery,
            )
            .where(
                FunctionTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(start, end))
            )
            .groupBy(dateQuery)
            .alias("subquery")

        return subquery
            .select(Avg(subquery[resultQuery], 2))
            .let {
                wrapAsExpression<Double>(it)
            }
            .castTo(DoubleColumnType())
    }
}