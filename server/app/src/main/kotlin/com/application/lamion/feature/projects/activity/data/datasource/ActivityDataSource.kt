package com.application.lamion.feature.projects.activity.data.datasource

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.data.database.table.refs.FeatureFunctionRef
import com.application.lamion.domain.model.Id
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

object ActivityDataSource {
    fun getFeatureWithTotalEventsCount(
        eventsCountQuery: Expression<Long>,
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime
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
        )
        .groupBy(*FeatureTable.columns.toTypedArray())
        .orderBy(eventsCountQuery, SortOrder.DESC)
        .toList()
}