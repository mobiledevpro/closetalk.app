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
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.mobiledevpro.settings.AppSettings
import java.io.InputStream
import java.io.OutputStream


/**
 * Serializer for Proto DataStore
 *
 */
object SettingsSerializer : Serializer<AppSettings> {
    override val defaultValue: AppSettings =
        AppSettings.getDefaultInstance()
            .toBuilder()
            .setDarkMode(true)
            .build()

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            AppSettings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            Log.e("SETTINGS", "Cannot read proto. Create default. ${e.localizedMessage}")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: AppSettings,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.appSettingsDataStore: DataStore<AppSettings> by dataStore(
    fileName = "app_settings.pb",
    serializer = SettingsSerializer
)