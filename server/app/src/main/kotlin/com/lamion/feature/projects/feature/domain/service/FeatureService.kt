package com.lamion.feature.projects.feature.domain.service

import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.feature.controller.payload.request.FeaturesSort
import com.lamion.feature.projects.feature.domain.model.FeatureDomain

interface FeatureService {
    suspend fun create(
        project: ProjectDomain,
        account: AccountDomain,
        title: String,
        description: String,
        functions: List<Id>,
    ): FeatureDomain.Partial

    suspend fun exists(project: ProjectDomain, featuresIds: List<Id>): Boolean

    suspend fun count(project: ProjectDomain): Long

    suspend fun get(id: Id, project: ProjectDomain): FeatureDomain.Partial

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