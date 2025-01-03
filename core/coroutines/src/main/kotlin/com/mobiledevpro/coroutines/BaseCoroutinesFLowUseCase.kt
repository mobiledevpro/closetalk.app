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
package com.mobiledevpro.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Base UseCase for Coroutines Flow result
 *
 * Created on Sep 12, 2022.
 *
 */
abstract class BaseCoroutinesFLowUseCase<in Params, Results>(
    executionDispatcher: CoroutineDispatcher
) : BaseUseCase(executionDispatcher) {

    abstract suspend fun buildUseCaseFlow(params: Params? = null): Flow<Results>

    suspend fun execute(params: Params? = null): Flow<Result<Results>> =
        withContext(dispatcher) {
            try {
                if (dispatcher == Dispatchers.Main)
                    throw RuntimeException("Use case '${this::class.simpleName}' cannot be executed in $dispatcher")

                if (Thread.currentThread().name.contains("main"))
                    throw RuntimeException("Use case '${this::class.simpleName}' cannot be executes in the ${Thread.currentThread().name} thread")

                this@BaseCoroutinesFLowUseCase.buildUseCaseFlow(params)
                    .flowOn(dispatcher)
                    .map {
                        resultOf { it }
                    }
            } catch (e: Exception) {
                logException(e.localizedMessage ?: e.cause?.message ?: e.toString())
                flowOf(Result.failure(Throwable(e.localizedMessage)))
            } catch (t: Throwable) {
                logException(t.message ?: t.cause?.message ?: t.toString())
                flowOf(Result.failure(t))
            }
        }

    override fun logException(errMessage: String) {
        Log.e(this::class.simpleName, "${this::class.simpleName} : $errMessage")
    }
}