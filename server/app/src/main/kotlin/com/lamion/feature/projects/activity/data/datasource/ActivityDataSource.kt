package com.lamion.feature.projects.activity.data.datasource

import com.lamion.data.database.table.project.*
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.data.database.utils.datePart
import com.lamion.domain.model.Id
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.KotlinLocalDateColumnType

object ActivityDataSource {
    fun getFeatureWithTotalEventsCount(
        eventsCountQuery: Expression<Long>,
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
        count: Int,
    ) = FeatureTable
        .innerJoin(FeatureFunctionRef)
        .innerJoin(FunctionTable)
        .innerJoin(EventTable)
        .select(
            eventsCountQuery,
            *FeatureTable.columns.toTypedArray(),
        )
        .where(
            FeatureTable.project.eq(projectId)
                .and(EventTable.createdAt.between(start, end))
                .and(FeatureTable.deleted.eq(false))
                .and(FunctionTable.deleted.eq(false))
        )
        .groupBy(*FeatureTable.columns.toTypedArray())
        .orderBy(eventsCountQuery, SortOrder.DESC)
        .limit(count)
        .toList()

    fun getUserActivityTime(
        projectId: Id,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Pair<Int, Long>> {
        val partQuery = EventTable.createdAt.datePart("hour")
        val countQuery = UserTable.id.countDistinct().alias("count")

        return EventTable
            .innerJoin(UserTable)
            .select(partQuery, countQuery)
            .where(
                UserTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(startDate, endDate))
            )
            .groupBy(partQuery)
            .orderBy(partQuery)
            .toList()
            .map {
                it[partQuery] to it[countQuery]
            }
    }

    fun getUserActivityInfo(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<Pair<LocalDate, Long>> {
        val countQuery = UserTable.id.count()
        val dayQuery = UserTable.createdAt.castTo(KotlinLocalDateColumnType())

        return getAvg(
            set = UserTable,
            countQuery = countQuery,
            dateQuery = dayQuery,
            where = {
                UserTable.project.eq(projectId)
                    .and(UserTable.createdAt.between(start, end))
            }
        )
    }

    fun getEventActivityInfo(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<Pair<LocalDate, Long>> {
        val countQuery = EventTable.id.count()
        val dayQuery = EventTable.createdAt.castTo(KotlinLocalDateColumnType())

        return getAvg(
            set = EventTable.innerJoin(FunctionTable),
            countQuery = countQuery,
            dateQuery = dayQuery,
            where = {
                FunctionTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(start, end))
                    .and(FunctionTable.deleted.eq(false))
            }
        )
    }

    fun getErrorActivityInfo(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<Pair<LocalDate, Long>> {
        val countQuery = ErrorTable.id.count()
        val dayQuery = ErrorTable.createdAt.castTo(KotlinLocalDateColumnType())

        return getAvg(
            set = ErrorTable.innerJoin(UserTable),
            countQuery = countQuery,
            dateQuery = dayQuery,
            where = {
                UserTable.project.eq(projectId)
                    .and(ErrorTable.createdAt.between(start, end))
            }
        )
    }

    private fun getAvg(
        set: ColumnSet,
        countQuery: Count,
        dateQuery: ExpressionWithColumnType<LocalDate>,
        where: SqlExpressionBuilder.() -> Op<Boolean>
    ): List<Pair<LocalDate, Long>> {
        // TODO: join to single db query
        val avg = set
            .select(countQuery)
            .where(where)
            .groupBy(dateQuery)
            .toList()
            .let { rows ->
                rows.sumOf { it[countQuery] } / rows.size.toDouble()
            }

        return set
            .select(dateQuery, countQuery)
            .where(where)
            .having {
                countQuery.castTo(DoubleColumnType()).greaterEq(avg)
            }
            .groupBy(dateQuery)
            .toList()
            .map {
                it[dateQuery] to it[countQuery]
            }
    }
}