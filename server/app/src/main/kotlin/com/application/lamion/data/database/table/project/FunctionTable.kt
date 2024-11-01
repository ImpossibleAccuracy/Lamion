package com.application.lamion.data.database.table.project

import com.application.lamion.data.database.base.BaseTable
import com.application.lamion.data.database.table.ProjectTable

object FunctionTable : BaseTable("ProjectFunction") {
    val title = varchar("title", 255)
    val project = reference("project_id", ProjectTable)
}