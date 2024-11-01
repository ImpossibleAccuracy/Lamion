package com.application.lamion.feature.projects.dashboard.data

import com.application.lamion.data.database.table.ProjectTable
import com.application.lamion.data.database.table.project.*
import com.application.lamion.data.database.table.refs.FeatureFunctionRef
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.dashboard.domain.model.ProjectScaling
import com.application.lamion.feature.projects.dashboard.domain.service.DashboardService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DashboardServiceImpl : DashboardService {
    companion object {
        const val ACTIVE_USERS_MIN_EVENTS = 5L
    }

    override suspend fun getScaling(project: ProjectDomain): ProjectScaling = coroutineScope {
        val prevMonthStartDate = now().toLocalDateTime(TimeZone.UTC)

        val totalUsers = async {
            UserTable
                .innerJoin(ProjectTable)
                .select(UserTable.id)
                .where(ProjectTable.id eq project.id)
                .count()
        }

        val activeUsers = async {
            val eventsCount = EventTable.id.count()

            UserTable
                .innerJoin(ProjectTable)
                .innerJoin(EventTable)
                .select(UserTable.id)
                .where(
                    (ProjectTable.id eq project.id) and
                            (EventTable.createdAt greaterEq prevMonthStartDate) and
                            (eventsCount greaterEq ACTIVE_USERS_MIN_EVENTS)
                )
                .count()
        }

        val totalErrors = async {
            ErrorTable
                .innerJoin(UserTable)
                .innerJoin(ProjectTable)
                .select(ErrorTable.id)
                .where(
                    (ProjectTable.id eq project.id) and
                            (ErrorTable.createdAt greaterEq prevMonthStartDate)
                )
                .count()
        }

        val totalEvents = async {
            EventTable
                .innerJoin(ProjectTable)
                .select(ErrorTable.id)
                .where(
                    (ProjectTable.id eq project.id) and
                            (EventTable.createdAt greaterEq prevMonthStartDate)
                )
                .count()
        }

        /*
        Total users
        --------------------
        see UsersService
        */
        /*
        Active users
        --------------------
        see UsersService
        */

        /*
        Total crashes.
        --------------------
        Just count lmao
        */

        /*
        Triggered events.
        --------------------
        Just count and filter by date
        */

        TODO()
        /*ProjectScaling(
            totalUsers = totalUsers.await(),
            activeUsers = activeUsers.await(),
            totalCrashes = totalErrors.await(),
            triggeredEvents = totalEvents.await()
        )*/
    }


    override suspend fun getTopFeatures(project: ProjectDomain): List<FeatureWithEvents> {
        val prevMonthStartDate = now().toLocalDateTime(TimeZone.UTC)

        val totalEventsCount = EventTable
            .innerJoin(FunctionTable)
            .select(EventTable.id)
            .where(
                (FunctionTable.project eq project.id) and
                        (EventTable.createdAt greaterEq prevMonthStartDate)
            )
            .count()

        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        return FeatureTable
            .innerJoin(FeatureFunctionRef)
            .innerJoin(FeatureTable)
            .innerJoin(EventTable)
            .select(
                eventsCountQuery,
                *FeatureTable.columns.toTypedArray(),
            )
            .where(
                (EventTable.createdAt greaterEq prevMonthStartDate) and
                        (FeatureTable.project eq project.id)
            )
            .groupBy(*FeatureTable.columns.toTypedArray())
            .orderBy(eventsCountQuery, SortOrder.DESC)
            .toList()
            .map {
                val totalEventsResult = it[eventsCountQuery]

                FeatureWithEvents(
                    id = it[FeatureTable.id].value,
                    title = it[FeatureTable.title],
                    description = it[FeatureTable.description],
                    totalEvents = totalEventsResult,
                    totalEventsPercent = totalEventsResult * 100 / totalEventsCount
                )
            }

        // Just replace date (prev month) and project id
        /*
        select p.*, R.events_count
        from (
            select ft.id, count(e.id) as events_count
            from projectfeature ft
            inner join function_feature ff on ff.feature_id = ft.id
            inner join projectfunction f ON ff.function_id = f.id
            inner join "event" e on e.function_id = f.id
            where e.created_at >= '2024-09-01'
            group by ft.id
        ) as R
        inner join projectfeature p on p.id = R.id
        order by R.events_count desc
         */
    }
}