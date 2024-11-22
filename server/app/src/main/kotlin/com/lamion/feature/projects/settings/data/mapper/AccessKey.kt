package com.lamion.feature.projects.settings.data.mapper

import com.lamion.data.database.table.ProjectAccessKeyTable
import com.lamion.feature.projects.settings.domain.model.AccessKeyDomain
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toAccessKeyDomain() = AccessKeyDomain(
    id = this[ProjectAccessKeyTable.id].value,
    title = this[ProjectAccessKeyTable.title],
    createdAt = this[ProjectAccessKeyTable.createdAt].date,
)