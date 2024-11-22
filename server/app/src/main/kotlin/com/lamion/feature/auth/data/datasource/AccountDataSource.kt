package com.lamion.feature.auth.data.datasource

import com.lamion.data.database.table.AccountTable
import com.lamion.data.database.table.RoleTable
import com.lamion.data.database.table.refs.AccountRoleRef
import com.lamion.data.database.utils.exists
import com.lamion.data.database.utils.new
import com.lamion.domain.model.Id
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll

object AccountDataSource {
    fun findAccount(id: Id) = AccountTable
        .selectAll()
        .where { AccountTable.id eq id }
        .firstOrNull()

    fun findAccountByEmail(email: String) = AccountTable
        .selectAll()
        .where { AccountTable.email eq email }
        .firstOrNull()

    fun findAccountRoles(accountId: Id) = RoleTable
        .join(AccountRoleRef, JoinType.INNER)
        .selectAll()
        .where(AccountRoleRef.account eq accountId)
        .toList()

    fun existsAccountByEmail(email: String) = AccountTable
        .selectAll()
        .where { AccountTable.email eq email }
        .exists()

    fun createAccount(
        username: String,
        email: String,
        password: String?
    ) = AccountTable
        .new {
            it[AccountTable.username] = username
            it[AccountTable.email] = email
            it[AccountTable.password] = password
        }!!
}