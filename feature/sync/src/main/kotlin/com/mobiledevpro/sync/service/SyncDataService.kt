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
package com.mobiledevpro.sync.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.mobiledevpro.sync.di.featureSyncData
import com.mobiledevpro.sync.domain.usecase.SyncPeopleUseCase
import com.mobiledevpro.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.context.loadKoinModules
import org.koin.core.scope.Scope

/**
 *  Service to get data from Firestore and save into local database.
 *
 * Created on Jul 30, 2025.
 *
 */
private const val NOTIFICATION_CHANNEL_ID = "SyncDataChannel"
private const val NOTIFICATION_CHANNEL_NAME = "Sync Data Channel"

class SyncDataService() : Service(), KoinScopeComponent {

    override val scope: Scope by getOrCreateScope()
    private val syncPeopleUseCase by scope.inject<SyncPeopleUseCase>()

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val wakeLock: PowerManager.WakeLock by lazy {
        this.getSystemService(PowerManager::class.java).newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "SyncDataService::WakeLock"
        )
    }

    init {
        loadKoinModules(featureSyncData)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)
        createNotificationChannel()

        // Sync People list
        serviceScope.launch {
            syncPeopleUseCase.execute()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(Constant.LOG_TAG_DEBUG, "SyncDataService.onStartCommand()")
        startForeground()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        scope.close()
        try {
            wakeLock.release()
        } catch (e: RuntimeException) {
            //ignore
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.d(Constant.LOG_TAG_DEBUG, "SyncDataService.onDestroy()")
    }


    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun startForeground() {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Close Talk")
            .setContentText("Online sync")
            //.setSmallIcon(R.drawable.ic_notification)
            .build()

        startForeground(1, notification)
    }
}