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
package com.mobiledevpro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobiledevpro.database.dao.PeopleDao
import com.mobiledevpro.database.entity.PeopleEntity
import com.mobiledevpro.database.entity.PeopleSocialEntity

/**
 *
 * Created on Jul 31, 2025.
 *
 */
@Database(
    entities = [
        PeopleEntity::class,
        PeopleSocialEntity::class
    ],
    version = BuildConfig.DB_VERSION
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun peopleDao(): PeopleDao

    companion object {
        fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                BuildConfig.DB_NAME
            )
                .fallbackToDestructiveMigration(true)
                .build()
    }
}