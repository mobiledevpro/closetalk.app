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
package com.mobiledevpro.peoplelist

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.mobiledevpro.database.AppDatabase
import com.mobiledevpro.peoplelist.domain.usecase.GetPeopleListUseCase
import com.mobiledevpro.peoplelist.view.state.PeopleProfileUIState
import com.mobiledevpro.peoplelist.view.vm.PeopleListViewModel
import com.mobiledevpro.ui.state.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class PeopleListViewModelTest : KoinTest {

    private lateinit var vm: PeopleListViewModel
    private val database: AppDatabase by inject()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        startKoin {
            modules(
                module {
                    single {
                        AppDatabase.buildDatabase(context)
                    }
                }
            )
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun stateTest() = runTest {
        // Use the TestScope's scheduler so all test coroutines, VM and Turbine share it.
        val scheduler = this.testScheduler
        Dispatchers.setMain(StandardTestDispatcher(scheduler))

        try {
            val useCase = GetPeopleListUseCase(database)
            vm = PeopleListViewModel(getPeopleListUseCase = useCase)
            assertTrue(
                "Initial state is incorrect: ${vm.uiState.value}",
                (vm.uiState.value as UIState) == PeopleProfileUIState.Loading
            )

            vm.uiState.test {
                testScheduler.advanceUntilIdle()

                // Increase await timeout to 5s to avoid flaky timeouts
                val item = withTimeout(5.seconds) { awaitItem() }

                assertEquals(PeopleProfileUIState.Loading, item)
                assertTrue(
                    "People list success state expected, but was ${vm.uiState.value}",
                    (awaitItem() is PeopleProfileUIState.Success)
                )

                cancelAndIgnoreRemainingEvents()
            }

        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        database.close()
        stopKoin()
    }
}