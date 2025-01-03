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
package com.mobiledevpro.user.profile.di

import com.mobiledevpro.settings.core.usecase.GetAppSettingsUseCase
import com.mobiledevpro.settings.core.usecase.UpdateAppSettingsUseCase
import com.mobiledevpro.user.profile.domain.usecase.GetUserProfileUseCase
import com.mobiledevpro.user.profile.view.vm.ProfileViewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * User Profile screen module
 *
 * Created on Jul 22, 2023.
 *
 */

val featureUserProfileModule = module {

    scope<ProfileViewModel> {
        viewModelOf(::ProfileViewModel)
        scopedOf(::GetUserProfileUseCase)
        scopedOf(::GetAppSettingsUseCase)
        scopedOf(::UpdateAppSettingsUseCase)
    }
}