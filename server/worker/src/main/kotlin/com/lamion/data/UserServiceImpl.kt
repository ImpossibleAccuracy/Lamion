package com.lamion.data

import com.lamion.data.database.table.project.ErrorTable
import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.UserTable
import com.lamion.data.database.utils.new
import com.lamion.domain.UserService
import com.lamion.domain.exception.InvalidArgumentsException
import com.lamion.domain.model.Id
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.AndOp
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {
    override suspend fun findOrCreateUser(
        projectId: Id,
        deviceKey: String?,
        clientKey: String?
    ): Id {
        if (deviceKey == null && clientKey == null) {
            throw InvalidArgumentsException("deviceKey and clientKey cannot be both null")
        }

        return dbQuery {
            UserTable
                .selectAll()
                .where(
                    AndOp(
                        listOfNotNull(
                            clientKey?.let { UserTable.clientKey.eq(it) },
                            deviceKey?.let { UserTable.identifyKey.eq(it) },
                        )
                    )
                )
                .toList()
                .let { result ->
                    if (result.isEmpty()) {
                        createUser(
                            projectId = projectId,
                            identifyKey = deviceKey,
                            clientKey = clientKey
                        )
                    } else if (result.size == 1) {
                        result.first()[UserTable.id].value
                    } else {
                        mergeUsers(
                            firstId = result[0][UserTable.id].value,
                            secondId = result[1][UserTable.id].value,
                            clientKey = clientKey,
                            identifyKey = deviceKey,
                        )
                    }
                }
        }
    }

    private suspend fun createUser(
        projectId: Id,
        identifyKey: String?,
        clientKey: String?
    ): Id = UserTable
        .new {
            it[project] = projectId
            it[UserTable.identifyKey] = identifyKey
            it[UserTable.clientKey] = clientKey
        }!!
        .let { it[UserTable.id].value }

    private suspend fun mergeUsers(
        firstId: Id,
        secondId: Id,
        identifyKey: String?,
        clientKey: String?
    ): Id {
        UserTable.update({ UserTable.id eq firstId }) {
            it[UserTable.identifyKey] = identifyKey
            it[UserTable.clientKey] = clientKey
        }

        EventTable.update({ EventTable.user eq secondId }) {
            it[user] = firstId
        }

        ErrorTable.update({ ErrorTable.user eq secondId }) {
            it[user] = firstId
        }

        UserTable.deleteWhere { UserTable.id eq secondId }

        return firstId
    }
}