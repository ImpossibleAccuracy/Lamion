package com.application.lamion.feature.project.data

import com.application.lamion.data.database.table.ProjectTable
import com.application.lamion.data.database.utils.new
import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.project.domain.ProjectFeatureService
import com.application.lamion.feature.shared.utils.require
import com.application.lamion.utils.ioCall
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjectFeatureServiceImpl : ProjectFeatureService {
    override suspend fun require(id: Id, account: AccountDomain): ProjectDomain =
        get(id, account).require { "Project not found" }

    override suspend fun get(id: Id, account: AccountDomain): ProjectDomain? = ioCall {
        ProjectTable
            .selectAll()
            .where { ProjectTable.id eq id }
            .firstOrNull()
            ?.toDomain()
    }

    override suspend fun create(owner: AccountDomain, title: String, description: String?): ProjectDomain = ioCall {
        ProjectTable
            .new {
                it[ProjectTable.title] = title
                it[ProjectTable.description] = description
                it[ProjectTable.owner] = owner.id
            }!!
            .toDomain()
    }

    override suspend fun list(account: AccountDomain): List<ProjectDomain> = ioCall {
        ProjectTable
            .selectAll()
            .toList()
            .map { it.toDomain() }
    }

    override suspend fun delete(project: ProjectDomain, account: AccountDomain): Unit = ioCall {
        ProjectTable.deleteWhere { ProjectTable.id eq project.id }
    }
}

private fun ResultRow.toDomain() = ProjectDomain(
    id = this[ProjectTable.id].value,
    title = this[ProjectTable.title],
    description = this[ProjectTable.description],
)
