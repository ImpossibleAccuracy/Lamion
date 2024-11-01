package com.application.lamion.data.database.table.refs

import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.database.table.project.FunctionTable
import org.jetbrains.exposed.sql.Table

object FeatureFunctionRef : Table("Function_Feature") {
    val feature = reference("feature_id", FeatureTable)
    val function = reference("function_id", FunctionTable)
}