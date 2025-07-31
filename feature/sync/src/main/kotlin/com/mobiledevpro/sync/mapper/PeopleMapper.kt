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
package com.mobiledevpro.sync.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.mobiledevpro.database.entity.PeopleEntity


fun DocumentSnapshot.toDatabase(): PeopleEntity =
    PeopleEntity(
        uuid = this.id,
        name = getString("name") ?: "",
        surname = getString("surname") ?: "",
        online = getBoolean("online") ?: false,
        photoUrl = getString("photo_url")
    )


fun List<DocumentSnapshot>.toDatabase(): List<PeopleEntity> =
    mapTo(ArrayList<PeopleEntity>(), DocumentSnapshot::toDatabase)



