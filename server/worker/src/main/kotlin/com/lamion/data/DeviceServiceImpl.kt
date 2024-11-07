package com.lamion.data

import com.lamion.data.database.table.project.DevicePlatformTable
import com.lamion.data.database.table.project.DeviceTable
import com.lamion.data.database.utils.new
import com.lamion.domain.DeviceService
import com.lamion.domain.model.Id
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Service

@Service
class DeviceServiceImpl : DeviceService {
    override suspend fun findOrCreateDevice(name: String, platform: String): Id = dbQuery {
        findDevice(name, platform) ?: createDevice(name, platform)
    }

    private fun findDevice(name: String, platform: String): Id? =
        DeviceTable
            .innerJoin(DevicePlatformTable)
            .select(DeviceTable.id)
            .where(
                DeviceTable.title.eq(name)
                    .and(DevicePlatformTable.title.eq(platform))
            )
            .firstOrNull()
            ?.let { it[DeviceTable.id].value }

    private fun createDevice(name: String, platform: String): Id {
        val platformId = findPlatform(platform) ?: createPlatform(platform)

        return DeviceTable
            .new {
                it[title] = name
                it[DeviceTable.platform] = platformId
            }!!
            .let { it[DeviceTable.id].value }
    }

    private fun findPlatform(title: String) =
        DevicePlatformTable
            .select(DevicePlatformTable.id)
            .where(DevicePlatformTable.title.eq(title))
            .firstOrNull()
            ?.get(DevicePlatformTable.id)?.value

    private fun createPlatform(title: String) =
        DevicePlatformTable
            .new {
                it[DevicePlatformTable.title] = title
            }!!
            .let { it[DevicePlatformTable.id].value }
}