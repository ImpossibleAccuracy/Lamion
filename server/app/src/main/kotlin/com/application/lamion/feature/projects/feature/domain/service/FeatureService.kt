package com.application.lamion.feature.projects.feature.domain.service

import com.application.lamion.domain.model.*
import com.application.lamion.feature.projects.feature.controller.payload.request.FeaturesSort
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import kotlinx.datetime.LocalDate

interface FeatureService {
    suspend fun create(
        project: ProjectDomain,
        account: AccountDomain,
        title: String,
        description: String,
        functions: List<Id>,
    ): FeatureDomain.Partial

    suspend fun checkFeaturesExists(project: ProjectDomain, featuresIds: List<Id>): Boolean

    suspend fun get(id: Id, project: ProjectDomain): FeatureDomain.Partial

    suspend fun getEventsGroupByDate(project: ProjectDomain, dateRange: DateRange): ChartDomain<LocalDate, Long>

    suspend fun getTotalFeaturesCount(project: ProjectDomain): Long

    suspend fun getTopFeatures(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int
    ): ChartDomain<FeatureDomain.Partial, Long>

    suspend fun list(
        project: ProjectDomain,
        page: Long,
        sort: FeaturesSort
    ): List<FeatureDomain.Detailed>

    suspend fun update(
        feature: FeatureDomain,
        account: AccountDomain,
        title: String,
        description: String,
    ): FeatureDomain.Partial

    suspend fun delete(feature: FeatureDomain, account: AccountDomain)
}