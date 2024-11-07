package com.application.lamion.feature.projects.feature.data.mapper

import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toFeatureDomainPartial() = FeatureDomain.Partial(
    id = this[FeatureTable.id].value,
    title = this[FeatureTable.title],
    description = this[FeatureTable.description],
)