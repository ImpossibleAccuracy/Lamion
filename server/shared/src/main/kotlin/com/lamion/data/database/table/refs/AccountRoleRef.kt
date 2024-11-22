package com.lamion.data.database.table.refs

import com.lamion.data.database.table.AccountTable
import com.lamion.data.database.table.RoleTable
import org.jetbrains.exposed.sql.Table

object AccountRoleRef : Table("role_account_ref") {
    val account = reference("account_id", AccountTable)
    val role = reference("role_id", RoleTable)
}