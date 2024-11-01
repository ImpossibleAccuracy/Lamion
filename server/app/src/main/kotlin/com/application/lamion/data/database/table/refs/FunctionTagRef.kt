package com.application.lamion.data.database.table.refs

import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.data.database.table.project.FunctionTagTable
import org.jetbrains.exposed.sql.Table

object FunctionTagRef : Table("Function_Tag") {
    val tag = reference("tag_id", FunctionTagTable)
    val function = reference("function_id", FunctionTable)
}