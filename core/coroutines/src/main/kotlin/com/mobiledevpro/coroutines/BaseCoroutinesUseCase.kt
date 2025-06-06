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
import kotlinx.coroutines.withContext

/**
 * Base UseCase with Coroutines
 *
 * Created on Sep 12, 2022.
 *
 */
abstract class BaseCoroutinesUseCase<in Params, Results>(
    executionDispatcher: CoroutineDispatcher
) : BaseUseCase(executionDispatcher) {

    abstract suspend fun buildUseCase(params: Params? = null): Results

    suspend fun execute(params: Params? = null): Result<Results> =
        withContext(dispatcher) {
            try {
                if (dispatcher == Dispatchers.Main)
                    throw RuntimeException("Use case '${this::class.simpleName}' cannot be executed in $dispatcher")

                if (Thread.currentThread().name.contains("main"))
                    throw RuntimeException("Use case '${this::class.simpleName}' cannot be executes in the ${Thread.currentThread().name} thread")

                resultOf {
                    this@BaseCoroutinesUseCase.buildUseCase(params)
                }
            } catch (t: Throwable) {
                logException(t.message ?: t.cause?.message ?: t.toString())
                Result.failure(t)
            } catch (e: Exception) {
                logException(e.localizedMessage ?: e.cause?.message ?: e.toString())
                Result.failure(Throwable(e.localizedMessage))
            }
        }


    override fun logException(errMessage: String) {
        Log.e(this::class.simpleName, "${this::class.simpleName} : $errMessage")

    }
}