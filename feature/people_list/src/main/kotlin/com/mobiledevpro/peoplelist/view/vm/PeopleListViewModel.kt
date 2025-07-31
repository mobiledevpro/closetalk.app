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
package com.mobiledevpro.peoplelist.view.vm

import com.mobiledevpro.peoplelist.domain.usecase.GetPeopleListUseCase
import com.mobiledevpro.peoplelist.view.state.PeopleProfileUIState
import com.mobiledevpro.ui.vm.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PeopleListViewModel(
    private val getPeopleListUseCase: GetPeopleListUseCase
) : BaseViewModel<PeopleProfileUIState>() {

    override val initialState: PeopleProfileUIState
        get() = PeopleProfileUIState.Loading

    override fun observeState(): Flow<PeopleProfileUIState> =
        getPeopleListUseCase.execute()
            .map { result ->

                try {
                    PeopleProfileUIState.Success(result.getOrThrow())
                } catch (t: Throwable) {
                    PeopleProfileUIState.Fail(t)
                }
            }
}