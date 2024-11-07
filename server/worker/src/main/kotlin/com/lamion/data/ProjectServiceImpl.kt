package com.lamion.data

import com.lamion.data.database.table.ProjectAccessKeyTable
import com.lamion.domain.ProjectService
import com.lamion.domain.exception.ResourceNotFoundException
import com.lamion.domain.model.Id
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl : ProjectService {
    override suspend fun findProjectByAccessKey(key: String): Id = dbQuery {
        ProjectAccessKeyTable
            .select(ProjectAccessKeyTable.project)
            .where(ProjectAccessKeyTable.value.eq(key))
            .firstOrNull()
            ?.let { it[ProjectAccessKeyTable.project].value }
            ?: throw ResourceNotFoundException("Project not found")
    }
}