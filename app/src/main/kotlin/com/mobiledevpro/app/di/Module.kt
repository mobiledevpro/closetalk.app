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
package com.mobiledevpro.app.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiledevpro.app.MainActivity
import com.mobiledevpro.database.AppDatabase
import com.mobiledevpro.main.view.vm.MainViewModel
import com.mobiledevpro.settings.core.datastore.AppSettingsManager
import com.mobiledevpro.settings.core.datastore.ImplAppSettingsManager
import com.mobiledevpro.settings.core.usecase.GetAppSettingsUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::ImplAppSettingsManager) { bind<AppSettingsManager>() }

    single {
        FirebaseAnalytics.getInstance(androidApplication().applicationContext)
    }

    single {
        AppDatabase.buildDatabase(androidApplication().applicationContext)
    }
}

val mainModule = module {
    scope<MainActivity> {
        viewModelOf(::MainViewModel)
        scopedOf(::GetAppSettingsUseCase)
    }
}
