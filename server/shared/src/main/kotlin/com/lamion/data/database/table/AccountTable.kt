package com.lamion.data.database.table

import com.lamion.data.database.base.BaseTable

object AccountTable : BaseTable("account") {
    val email = varchar("email", 255)
    val username = varchar("username", 255)
    val password = varchar("password", 255).nullable()
    val avatar = optReference("avatar_id", FileTable)
}