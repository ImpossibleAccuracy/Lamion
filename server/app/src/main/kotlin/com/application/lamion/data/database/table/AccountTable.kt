package com.application.lamion.data.database.table

import com.application.lamion.data.database.base.BaseTable

object AccountTable : BaseTable("Account") {
    val email = varchar("email", 255)
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val avatar = optReference("avatar_id", FileTable)
}