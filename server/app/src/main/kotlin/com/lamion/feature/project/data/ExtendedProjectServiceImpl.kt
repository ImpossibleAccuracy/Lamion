package com.lamion.feature.project.data

import com.lamion.data.database.table.ProjectTable
import com.lamion.data.database.utils.new
import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.project.data.mapper.toProjectDomain
import com.lamion.feature.project.domain.ExtendedProjectService
import com.lamion.feature.shared.utils.require
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service

@Service
class ExtendedProjectServiceImpl : ExtendedProjectService {
    override suspend fun require(id: Id, account: AccountDomain): ProjectDomain =
        get(id, account).require { "Project not found" }

    override suspend fun get(id: Id, account: AccountDomain): ProjectDomain? = dbQuery {
        ProjectTable
            .selectAll()
            .where(
                ProjectTable.id.eq(id)
                    .and(ProjectTable.deleted.eq(false))
            )
            .firstOrNull()
            ?.toProjectDomain()
    }

    override suspend fun create(
        owner: AccountDomain,
        title: String,
        description: String?
    ): ProjectDomain = dbQuery {
        ProjectTable
            .new {
                it[ProjectTable.title] = title
                it[ProjectTable.description] = description
                it[ProjectTable.owner] = owner.id
            }!!
            .toProjectDomain()
    }

    override suspend fun list(account: AccountDomain): List<ProjectDomain> = dbQuery {
        ProjectTable
            .selectAll()
            .where(
                ProjectTable.owner.eq(account.id)
                    .and(ProjectTable.deleted.eq(false))
            )
            .orderBy(ProjectTable.id, SortOrder.ASC)
            .toList()
            .map { it.toProjectDomain() }
    }

    override suspend fun update(
        project: ProjectDomain,
        account: AccountDomain,
        title: String?,
        description: String?
    ): ProjectDomain = dbQuery {
        ProjectTable
            .updateReturning(
                where = {
                    ProjectTable.id.eq(project.id)
                }
            ) {
                if (title != null) {
                    it[ProjectTable.title] = title
                }

                if (description != null) {
                    it[ProjectTable.description] = description
                }
            }
            .first()
            .toProjectDomain()
    }

    override suspend fun delete(project: ProjectDomain, account: AccountDomain): Unit = dbQuery {
        ProjectTable.update(where = { ProjectTable.id eq project.id }) {
            it[deleted] = true
        }
    }
}

