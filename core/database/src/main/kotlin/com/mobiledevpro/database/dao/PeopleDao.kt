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
package com.mobiledevpro.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.mobiledevpro.database.entity.PeopleEntity
import kotlinx.coroutines.flow.Flow

/**
 *
 * Created on Jul 31, 2025.
 *
 */

@Dao
interface PeopleDao : BaseDao<PeopleEntity> {

    @Query("SELECT * FROM people")
    fun selectAll(): Flow<List<PeopleEntity>>

    @Query("SELECT * FROM people WHERE uuid = :uuid")
    fun selectByUuid(uuid: String): Flow<PeopleEntity>

    @Query("DELETE FROM people")
    suspend fun deleteAll()

    @Transaction
    suspend fun updateAll(list: List<PeopleEntity>) {
        deleteAll()
        insert(list)
    }
}