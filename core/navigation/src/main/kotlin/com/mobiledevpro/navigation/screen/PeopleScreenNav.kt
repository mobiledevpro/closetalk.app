/*
 * Copyright 2024 | Dmitri Chernysh | http://mobile-dev.pro
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
package com.mobiledevpro.navigation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobiledevpro.navigation.Screen
import com.mobiledevpro.navigation.graph.PeopleNavGraph
import com.mobiledevpro.people.view.PeopleScreen
import com.mobiledevpro.ui.compositionlocal.LocalAnalytics
import com.mobiledevpro.ui.ext.findActivity

fun NavGraphBuilder.peopleNavGraph() {
    composable(
        route = Screen.People.route
    ) {
        val context = LocalContext.current
        val analytics = LocalAnalytics.current

        LaunchedEffect(Unit) {
            analytics.trackScreen(
                "PeopleScreen",
                context.findActivity()
            )
        }

        val navController = rememberNavController()

        val nestedNavGraph: @Composable () -> Unit = {
            PeopleNavGraph(
                navController = navController
            )
        }

        PeopleScreen(nestedNavGraph)
    }
}
