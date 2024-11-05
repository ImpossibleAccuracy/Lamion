package com.application.lamion.data.database.table.project

import com.application.lamion.data.database.base.BaseTable
import com.application.lamion.data.database.table.ProjectTable

object FunctionTable : BaseTable("project_function") {
    val title = varchar("title", 255)
    val project = reference("project_id", ProjectTable)
    val deleted = bool("deleted").default(false)
}