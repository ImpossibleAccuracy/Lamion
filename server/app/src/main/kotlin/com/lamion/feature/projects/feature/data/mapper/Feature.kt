package com.lamion.feature.projects.feature.data.mapper

import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toFeatureDomainPartial() = FeatureDomain.Partial(
    id = this[com.lamion.data.database.table.project.FeatureTable.id].value,
    title = this[com.lamion.data.database.table.project.FeatureTable.title],
    description = this[com.lamion.data.database.table.project.FeatureTable.description],
)