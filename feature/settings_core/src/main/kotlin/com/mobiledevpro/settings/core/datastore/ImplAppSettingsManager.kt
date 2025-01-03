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
package com.mobiledevpro.settings.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import com.mobiledevpro.settings.AppSettings
import kotlinx.coroutines.flow.Flow

class ImplAppSettingsManager(
    appContext: Context
) : AppSettingsManager {

    private val appSettings: DataStore<AppSettings> = appContext.appSettingsDataStore

    override fun get(): Flow<AppSettings> = appSettings.data

    override suspend fun update(settings: AppSettings) {
        appSettings.updateData {
            it.toBuilder()
                .setDarkMode(settings.darkMode)
                .build()
        }
    }
}