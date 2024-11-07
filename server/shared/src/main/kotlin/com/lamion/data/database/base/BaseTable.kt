package com.lamion.data.database.base

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

abstract class BaseTable(name: String) : LongIdTable(name) {
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}