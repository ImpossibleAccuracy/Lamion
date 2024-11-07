package com.lamion.feature.project.data.mapper

import com.lamion.data.database.table.ProjectTable
import com.lamion.domain.model.ProjectDomain
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toProjectDomain() = ProjectDomain(
    id = this[ProjectTable.id].value,
    title = this[ProjectTable.title],
    description = this[ProjectTable.description],
)
