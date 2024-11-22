package com.lamion.feature.projects.feature.domain.service

import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.feature.FeatureService
import com.lamion.feature.projects.feature.controller.payload.request.DefaultSort
import com.lamion.feature.projects.feature.domain.model.FeatureDomain

interface ExtendedFeatureService : FeatureService {
    suspend fun create(
        project: ProjectDomain,
        account: AccountDomain,
        title: String,
        description: String,
        functions: List<Id>,
    ): FeatureDomain.Partial

    suspend fun count(project: ProjectDomain): Long

    suspend fun list(
        project: ProjectDomain,
        page: Long,
        sort: DefaultSort,
    ): List<FeatureDomain.Detailed>

    suspend fun getFeatureTags(feature: FeatureDomain): List<String>

    suspend fun update(
        feature: FeatureDomain,
        account: AccountDomain,
        title: String,
        description: String,
    ): FeatureDomain.Partial

    suspend fun delete(feature: FeatureDomain, account: AccountDomain)
}