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
package com.mobiledevpro.user.profile.view.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mobiledevpro.settings.core.model.Settings
import com.mobiledevpro.settings.core.usecase.GetAppSettingsUseCase
import com.mobiledevpro.settings.core.usecase.UpdateAppSettingsUseCase
import com.mobiledevpro.ui.vm.BaseViewModel
import com.mobiledevpro.user.profile.domain.usecase.GetUserProfileUseCase
import com.mobiledevpro.user.profile.view.state.UserProfileUIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase
) : BaseViewModel<UserProfileUIState>() {

    override fun initUIState(): UserProfileUIState = UserProfileUIState.Empty

    init {
        Log.d("UI", "ProfileViewModel init")
        observeUserProfile()
        observeAppSettings()
    }

    fun onDarkModeSwitched(isDarkMode: Boolean) {
        viewModelScope.launch {
            val appSettings: Settings? = if (uiState.value is UserProfileUIState.Success) {
                (uiState.value as UserProfileUIState.Success).settings
            } else {
                null
            }

            appSettings?.let { setting ->
                updateAppSettingsUseCase.execute(setting.copy(darkMode = isDarkMode))
            }
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase.execute()
                .collectLatest { result ->
                    result.onSuccess { profile ->
                        _uiState.update {
                            if (it is UserProfileUIState.Success)
                                it.copy(userProfile = profile)
                            else
                                UserProfileUIState.Success(profile)
                        }
                    }.onFailure { err ->
                        _uiState.update {
                            UserProfileUIState.Fail(err)
                        }
                    }

                }
        }
    }

    private fun observeAppSettings() {
        viewModelScope.launch {
            getAppSettingsUseCase.execute()
                .collectLatest { result ->
                    result.onSuccess { settings ->
                        Log.d("settings", "observeAppSettings: dark mode = ${settings.darkMode}")
                        _uiState.update {
                            if (it is UserProfileUIState.Success)
                                it.copy(settings = settings)
                            else
                                UserProfileUIState.Success(settings = settings)
                        }
                    }
                }
        }
    }
}