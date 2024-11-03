package com.application.lamion.feature.projects.settings.data

import com.application.lamion.data.database.table.ProjectAccessKeyTable
import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.settings.domain.model.AccessKeyDomain
import com.application.lamion.feature.projects.settings.domain.service.ProjectAccessKeyService
import com.application.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service

@Service
class ProjectAccessKeyServiceImpl : ProjectAccessKeyService {
    override suspend fun getAccessKeys(account: AccountDomain, project: ProjectDomain): List<AccessKeyDomain> = dbQuery {
        ProjectAccessKeyTable
            .selectAll()
            .where { ProjectAccessKeyTable.project eq project.id }
            .toList()
            .map {
                AccessKeyDomain(
                    title = it[ProjectAccessKeyTable.title],
                    createdAt = it[ProjectAccessKeyTable.createdAt].date,
                )
            }
    }
}