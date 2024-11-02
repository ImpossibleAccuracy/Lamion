package com.application.lamion.data.database.table.project

import com.application.lamion.data.database.base.BaseTable

object DeviceTable : BaseTable("project_device") {
    val title = varchar("title", 255)
    val platform = reference("platform_id", DevicePlatformTable)
}