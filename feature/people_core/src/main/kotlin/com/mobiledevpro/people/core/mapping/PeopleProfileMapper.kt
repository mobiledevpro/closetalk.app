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
package com.mobiledevpro.people.core.mapping

import android.net.Uri
import androidx.core.net.toUri
import com.mobiledevpro.database.entity.PeopleEntity
import com.mobiledevpro.domain.model.PeopleProfile

/**
 *
 * Created on Jul 31, 2025.
 *
 */

fun PeopleEntity.toDomain(): PeopleProfile =
    PeopleProfile(
        uuid = uuid,
        name = name,
        surname = surname,
        online = online,
        photo = photoUrl?.toUri() ?: Uri.EMPTY
    )


fun List<PeopleEntity>.toDomain(): List<PeopleProfile> =
    mapTo(ArrayList<PeopleProfile>(), PeopleEntity::toDomain)