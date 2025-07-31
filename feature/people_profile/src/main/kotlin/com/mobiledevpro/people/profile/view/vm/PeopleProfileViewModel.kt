/*
 * Copyright 2022 | Dmitri Chernysh | https://mobile-dev.pro
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
package com.mobiledevpro.people.profile.view.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mobiledevpro.people.profile.domain.usecase.GetPeopleProfileUseCase
import com.mobiledevpro.people.profile.view.args.PeopleProfileArgs
import com.mobiledevpro.people.profile.view.state.PeopleProfileUIState
import com.mobiledevpro.ui.vm.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Profile screen for selected person from People list
 *
 * Created on Feb 04, 2023.
 *
 */
class PeopleProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPeopleProfileUseCase: GetPeopleProfileUseCase
) : BaseViewModel<PeopleProfileUIState>() {

    override val initialState: PeopleProfileUIState
        get() = PeopleProfileUIState.Empty

    override val scope: CoroutineScope
        get() = viewModelScope

    private val profileUuid: String = PeopleProfileArgs(savedStateHandle).peopleProfileUuid

    override fun observeState(): Flow<PeopleProfileUIState> =
        getPeopleProfileUseCase.execute(profileUuid)
            .map { result ->

                try {
                    PeopleProfileUIState.Success(result.getOrThrow())
                } catch (t: Throwable) {
                    PeopleProfileUIState.Fail(t)
                }

            }
}