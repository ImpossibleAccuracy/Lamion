package com.lamion.feature.account.data.mapper

import com.lamion.data.database.table.AccountTable
import com.lamion.domain.model.AccountDomain
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toAccountDomain() = AccountDomain.Total(
    id = this[AccountTable.id].value,
    username = this[AccountTable.username],
    email = this[AccountTable.email],
    avatar = this[AccountTable.avatar]?.value,
)