package com.application.lamion.data.database.table.project

import com.application.lamion.data.database.base.BaseTable
import com.application.lamion.data.database.table.ProjectTable

object FeatureTable : BaseTable("ProjectFeature") {
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
    val project = reference("project_id", ProjectTable)
}