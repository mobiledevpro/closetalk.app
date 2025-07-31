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
package com.mobiledevpro.sync.domain.usecase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.mobiledevpro.coroutines.BaseCoroutinesUseCase
import com.mobiledevpro.coroutines.None
import com.mobiledevpro.database.AppDatabase
import com.mobiledevpro.database.entity.PeopleEntity
import com.mobiledevpro.sync.mapper.toDatabase
import com.mobiledevpro.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest

/**
 * Use case to sync People list from Firestore to local database.
 *
 * Created on Jul 30, 2025.
 *
 */
class SyncPeopleUseCase(
    val firestore: FirebaseFirestore,
    val database: AppDatabase
) : BaseCoroutinesUseCase<None, None>(Dispatchers.IO) {

    override suspend fun buildUseCase(params: None?): None {
        getPeopleFromFirestore()
            .collectLatest { snapshot: QuerySnapshot ->
                for (document in snapshot.documents) {
                    Log.d(
                        Constant.LOG_TAG_DEBUG,
                        "SyncPeopleUseCase.buildUseCase:${document.id} => ${document.data}"
                    )
                }

                snapshot.documents
                    .toDatabase()
                    .updatePeopleLocal()
            }

        return None()
    }

    private fun getPeopleFromFirestore() = callbackFlow<QuerySnapshot> {
        // Implementation to fetch People data from Firestore
        val listenerRegistration =
            firestore.collection("people")
                .addSnapshotListener(Dispatchers.IO.asExecutor()) { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    snapshot?.also(::trySend)
                }


        awaitClose {
            listenerRegistration.remove()
        }
    }

    private suspend fun List<PeopleEntity>.updatePeopleLocal() {
        database.peopleDao()
            .updateAll(this)
    }
}