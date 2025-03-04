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
package com.mobiledevpro.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.lifecycle.ViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.scope.rememberKoinScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope
import org.koin.ext.getFullName
import org.koin.java.KoinJavaComponent


inline fun <reified T> koinScope(): Scope {

    val scopeId = T::class.getFullName() + "@" + T::class.hashCode()
    val qualifier = TypeQualifier(T::class)

    return KoinJavaComponent.getKoin().getOrCreateScope(scopeId, qualifier)
}

@OptIn(KoinExperimentalAPI::class)
@Composable
inline fun <reified T : ViewModel> rememberNavViewModel(
    crossinline modules: @DisallowComposableCalls () -> List<Module>
): T {
    rememberKoinModules(
        modules = modules
    )

    val scope = rememberKoinScope(scope = koinScope<T>())

    // return remember { scope.get() }

    // It allows to use SavedStateHandle in ViewModel
    return koinNavViewModel<T>(scope = scope)
}