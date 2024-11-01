package com.application.lamion.data.database.utils

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.InsertStatement

fun Query.exists(): Boolean = count() > 0

fun <T : Table> T.new(body: T.(InsertStatement<Number>) -> Unit): ResultRow? =
    insert(body).resultedValues?.firstOrNull()
