package com.lamion.feature.projects.users.data

import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.data.database.table.project.UserTable
import com.lamion.domain.model.Id
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.div
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.kotlin.datetime.KotlinLocalDateColumnType

object UserDataSource {
    fun getUsersCount(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Long = UserTable
        .innerJoin(EventTable)
        .select(UserTable.id)
        .where(
            UserTable.project.eq(projectId)
                .and(isUserCountingAsTotalWhere(start, end))
        )
        .having {
            isUserCountingAsTotalHaving()
        }
        .groupBy(UserTable.id)
        .count()

    fun getActiveUsersCount(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Long = UserTable
        .innerJoin(EventTable)
        .select(UserTable.id)
        .where(
            UserTable.project.eq(projectId)
                .and(isUserCountingAsActiveWhere(start, end))
        )
        .groupBy(UserTable.id)
        .having {
            isUserCountingAsActiveHaving(
                sourceColumn = EventTable.id.count().castTo(DoubleColumnType()),
                projectId = projectId,
                start = start,
                end = end,
            )
        }
        .count()

    fun getUserCountGroupByDate(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Map<LocalDate, Long> {
        val countQuery = UserTable.id.countDistinct().alias("user_count")
        val dateQuery = EventTable.createdAt.castTo(KotlinLocalDateColumnType()).alias("event_date")

        return UserTable
            .innerJoin(EventTable)
            .select(countQuery, dateQuery)
            .where(
                UserTable.project.eq(projectId)
                    .and(UserDataSource.isUserCountingAsTotalWhere(start, end))
            )
            .groupBy(dateQuery)
            .orderBy(dateQuery)
            .having {
                UserDataSource.isUserCountingAsTotalHaving()
            }
            .toList()
            .associate {
                it[dateQuery] to it[countQuery]
            }
    }

    fun getActiveUsersGroupByDate(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Map<LocalDate, Long> {
        val userIdQuery = UserTable.id.alias("user_id")
        val dateQuery = EventTable.createdAt.castTo(KotlinLocalDateColumnType()).alias("event_date")

        val subqueryAlias = UserTable
            .innerJoin(EventTable)
            .select(
                userIdQuery,
                dateQuery,
            )
            .where(
                UserTable.project.eq(projectId)
                    .and(UserDataSource.isUserCountingAsActiveWhere(start, end))
            )
            .groupBy(
                UserTable.id,
                dateQuery
            )
            .having {
                UserDataSource.isUserCountingAsActiveGroupByDateHaving(
                    sourceColumn = EventTable.id.count().castTo(DoubleColumnType()),
                    projectId = projectId,
                    start = start,
                    end = end,
                )
            }
            .orderBy(userIdQuery)
            .orderBy(dateQuery)
            .alias("subquery")

        val subUserCountQuery = Count(subqueryAlias[userIdQuery], true)
        val subDateQuery = subqueryAlias[dateQuery]

        return subqueryAlias
            .select(subUserCountQuery, subDateQuery)
            .groupBy(subDateQuery)
            .toList()
            .associate {
                it[subDateQuery] to it[subUserCountQuery]
            }
    }

    fun isUserCountingAsTotalWhere(start: LocalDateTime, end: LocalDateTime): Op<Boolean> =
        EventTable.createdAt.between(start, end)

    fun isUserCountingAsTotalHaving(): Op<Boolean> =
        EventTable.id.count().greaterEq(0)

    fun isUserCountingAsActiveWhere(start: LocalDateTime, end: LocalDateTime): Op<Boolean> =
        EventTable.createdAt.between(start, end)

    private fun isUserCountingAsActiveHaving(
        sourceColumn: Expression<Double>,
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Op<Boolean> {
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
                sourceColumn.greater(wrapAsExpression(it))
            }
    }

    fun isUserCountingAsActiveGroupByDateHaving(
        sourceColumn: Expression<Double>,
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Op<Boolean> {
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
            .let {
                sourceColumn.greater(it)
            }
    }
}