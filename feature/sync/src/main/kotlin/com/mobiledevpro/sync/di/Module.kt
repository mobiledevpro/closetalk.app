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
package com.mobiledevpro.sync.di

import com.google.firebase.firestore.FirebaseFirestore
import com.mobiledevpro.firestore.FirestoreHelper
import com.mobiledevpro.sync.domain.usecase.SyncPeopleUseCase
import com.mobiledevpro.sync.service.SyncDataService
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

/**
 *
 * Created on Jul 30, 2025.
 *
 */

val featureSyncData = module {
    scope<SyncDataService> {
        scopedOf(::SyncPeopleUseCase)
        scoped<FirebaseFirestore> {
            FirestoreHelper.getInstance()
        }
    }
}