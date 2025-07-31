/*
 * Copyright 2025 | Dmitri Chernysh | https://github.com/dmitriy-chernysh
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mobiledevpro.peoplelist.domain.usecase

import com.mobiledevpro.analytics.Crashlytics
import com.mobiledevpro.coroutines.BaseCoroutinesFLowUseCase
import com.mobiledevpro.coroutines.None
import com.mobiledevpro.database.AppDatabase
import com.mobiledevpro.database.entity.PeopleEntity
import com.mobiledevpro.domain.model.PeopleProfile
import com.mobiledevpro.people.core.mapping.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetPeopleListUseCase(
    private val database: AppDatabase
) : BaseCoroutinesFLowUseCase<None, List<PeopleProfile>>(Dispatchers.IO) {

    @OptIn(FlowPreview::class)
    override fun buildUseCaseFlow(params: None?): Flow<List<PeopleProfile>> =
        database.peopleDao().selectAll()
            .map(List<PeopleEntity>::toDomain)


    override fun logException(errMessage: String) {
        Crashlytics.logException(Throwable("[GetPeopleListUseCase] $errMessage"))
    }
}