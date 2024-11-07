package com.lamion.data.database.table.refs

import com.lamion.data.database.table.project.FunctionTagTable
import org.jetbrains.exposed.sql.Table

object FunctionTagRef : Table("function_tag_ref") {
    val tag = reference("tag_id", FunctionTagTable)
    val function = reference("function_id", com.lamion.data.database.table.project.FunctionTable)
}