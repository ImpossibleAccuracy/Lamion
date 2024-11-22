package com.lamion.domain.service.feature

import com.lamion.domain.model.*
import com.lamion.feature.projects.feature.domain.model.FeatureDomain

interface FeatureService {
    suspend fun get(id: Id, project: ProjectDomain): FeatureDomain.Partial

    suspend fun exists(project: ProjectDomain, featuresIds: List<Id>): Boolean

    suspend fun getTopFeaturesList(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int,
    ): List<FeatureWithEvents>

    suspend fun getTopFeaturesWithEventsCount(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int,
    ): ChartDomain<FeatureDomain.Partial, Long>
}
