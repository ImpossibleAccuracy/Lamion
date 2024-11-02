package com.application.lamion.data.database.table.project

import com.application.lamion.data.database.base.BaseTable
import com.application.lamion.data.database.table.ProjectTable

object UserTable : BaseTable("project_user") {
    val clientKey = varchar("client_key", 255).nullable()
    val identifyKey = varchar("identify_key", 255).nullable()
    val project = reference("project_id", ProjectTable)
}