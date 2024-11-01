package com.application.lamion.data.database.table.refs

import com.application.lamion.data.database.table.AccountTable
import com.application.lamion.data.database.table.RoleTable
import org.jetbrains.exposed.sql.Table

object AccountRoleRef : Table("Role_Account") {
    val account = reference("account_id", AccountTable)
    val role = reference("role_id", RoleTable)
}