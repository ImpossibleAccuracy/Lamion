package com.lamion.data.database.table.project

import com.lamion.data.database.base.BaseTable
import com.lamion.data.database.table.ProjectTable

object FeatureTable : BaseTable("project_feature") {
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
    val project = reference("project_id", ProjectTable)
    val deleted = bool("deleted").default(false)
}