package com.lamion.feature.projects.settings.data

import com.lamion.data.database.table.ProjectAccessKeyTable
import com.lamion.data.database.utils.new
import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.settings.data.mapper.toAccessKeyDomain
import com.lamion.feature.projects.settings.domain.model.AccessKeyDomain
import com.lamion.feature.projects.settings.domain.model.AccessKeyWithValueDomain
import com.lamion.feature.projects.settings.domain.service.ProjectAccessKeyService
import com.lamion.feature.shared.utils.require
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service

@Service
class ProjectAccessKeyServiceImpl : ProjectAccessKeyService {
    override suspend fun create(
        account: AccountDomain,
        project: ProjectDomain,
        title: String
    ): AccessKeyWithValueDomain {
        val accessKey = AccessKeyUtils.generateAccessKey()

        return dbQuery {
            ProjectAccessKeyTable
                .new {
                    it[ProjectAccessKeyTable.project] = project.id
                    it[ProjectAccessKeyTable.title] = title
                    it[value] = accessKey
                }!!
                .let {
                    AccessKeyWithValueDomain(
                        id = it[ProjectAccessKeyTable.id].value,
                        title = it[ProjectAccessKeyTable.title],
                        value = it[ProjectAccessKeyTable.value],
                        createdAt = it[ProjectAccessKeyTable.createdAt].date,
                    )
                }
        }
    }

    override suspend fun get(
        account: AccountDomain,
        project: ProjectDomain,
        keyId: Id
    ): AccessKeyDomain = dbQuery {
        ProjectAccessKeyTable
            .selectAll()
            .where(ProjectAccessKeyTable.id.eq(keyId))
            .firstOrNull()
            .require { "Access key not found" }
            .toAccessKeyDomain()
    }

    override suspend fun getAccessKeys(
        account: AccountDomain,
        project: ProjectDomain
    ): List<AccessKeyDomain> = dbQuery {
        ProjectAccessKeyTable
            .selectAll()
            .where(
                ProjectAccessKeyTable.project.eq(project.id)
            )
            .toList()
            .map {
                it.toAccessKeyDomain()
            }
    }

    override suspend fun delete(
        account: AccountDomain,
        target: AccessKeyDomain
    ): Unit = dbQuery {
        ProjectAccessKeyTable.deleteWhere {
            ProjectAccessKeyTable.id.eq(target.id)
        }
    }

    override suspend fun deleteAll(
        account: AccountDomain,
        project: ProjectDomain
    ): Unit = dbQuery {
        ProjectAccessKeyTable.deleteWhere {
            ProjectAccessKeyTable.project.eq(project.id)
        }
    }
}
