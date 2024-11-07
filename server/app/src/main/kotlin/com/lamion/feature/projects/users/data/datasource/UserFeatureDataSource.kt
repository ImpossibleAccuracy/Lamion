package com.lamion.feature.projects.users.data.datasource

import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.UserTable
import com.lamion.data.datasource.UserDataSource
import com.lamion.domain.model.Id
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.KotlinLocalDateColumnType

object UserFeatureDataSource {
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
}