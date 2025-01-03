/*
 * Copyright 2023 | Dmitri Chernysh | https://mobile-dev.pro
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
package com.mobiledevpro.user.profile.view.state

import com.mobiledevpro.domain.model.UserProfile
import com.mobiledevpro.settings.core.model.Settings
import com.mobiledevpro.ui.state.UIState

/**
 * UI state for [com.mobiledevpro.user.profile.view.ProfileScreen]
 *
 * Created on May 09, 2023.
 *
 */
sealed interface UserProfileUIState : UIState {

    data object Empty : UserProfileUIState

    data class Success(val userProfile: UserProfile? = null, val settings: Settings? = null) :
        UserProfileUIState

    data class Fail(val throwable: Throwable) : UserProfileUIState
}