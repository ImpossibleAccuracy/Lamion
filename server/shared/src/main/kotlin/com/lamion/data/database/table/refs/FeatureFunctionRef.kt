package com.lamion.data.database.table.refs

import org.jetbrains.exposed.sql.Table

object FeatureFunctionRef : Table("function_feature_ref") {
    val feature = reference("feature_id", com.lamion.data.database.table.project.FeatureTable)
    val function = reference("function_id", com.lamion.data.database.table.project.FunctionTable)
}