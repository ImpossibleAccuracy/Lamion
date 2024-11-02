package com.application.lamion.data.database.table

import com.application.lamion.data.database.base.BaseTable

object ProjectTable : BaseTable("project") {
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
    val owner = reference("owner_id", AccountTable)
}