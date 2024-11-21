package com.lamion.data.service.error

import com.lamion.data.database.table.project.ErrorTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.data.database.table.project.UserTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.domain.model.Id
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.castTo
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.kotlin.datetime.KotlinLocalDateColumnType

object ErrorDataSource {
    fun countErrorsByProjectId(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Long = ErrorTable
        .innerJoin(UserTable)
        .select(ErrorTable.id)
        .where(
            UserTable.project.eq(projectId)
                .and(ErrorTable.createdAt.between(start, end))
        )
        .count()

    fun countErrorsByFeatureId(
        featureId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ) = ErrorTable
        .innerJoin(FunctionTable)
        .innerJoin(FeatureFunctionRef)
        .select(ErrorTable.id)
        .where(
            FeatureFunctionRef.feature.eq(featureId)
                .and(ErrorTable.createdAt.between(start, end))
        )
        .count()

    fun getErrorsByFeatureGroupByDate(
        featureId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Map<LocalDate, Long> {
        val dateQuery = ErrorTable.createdAt.castTo(KotlinLocalDateColumnType())
        val countQuery = ErrorTable.id.count()

        return ErrorTable
            .innerJoin(FunctionTable)
            .innerJoin(FeatureFunctionRef)
            .select(dateQuery, countQuery)
            .where(
                FeatureFunctionRef.feature.eq(featureId)
                    .and(ErrorTable.createdAt.between(start, end))
                    .and(FunctionTable.deleted.eq(false))
            )
            .groupBy(dateQuery)
            .orderBy(dateQuery)
            .toList()
            .associate {
                it[dateQuery] to it[countQuery]
            }
    }
}