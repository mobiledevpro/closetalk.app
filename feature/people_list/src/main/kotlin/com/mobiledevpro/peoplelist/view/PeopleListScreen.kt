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
package com.mobiledevpro.peoplelist.view

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobiledevpro.domain.model.PeopleProfile
import com.mobiledevpro.peoplelist.view.component.ProfileCard
import com.mobiledevpro.peoplelist.view.state.PeopleProfileUIState
import com.mobiledevpro.ui.component.ScreenBackground
import com.mobiledevpro.ui.ext.dp
import com.mobiledevpro.ui.ext.statusBarHeight
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PeopleListScreen(
    stateFlow: StateFlow<PeopleProfileUIState>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateToProfile: (profileId: Int) -> Unit
) {

    val uiState by stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ScreenBackground(
        modifier = Modifier
            .fillMaxSize()
    ) {

        when (uiState) {
            is PeopleProfileUIState.Loading -> Loading()
            is PeopleProfileUIState.Empty -> NoPeopleFound()
            is PeopleProfileUIState.Success ->
                PeopleList(
                    list = (uiState as PeopleProfileUIState.Success).profileList,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onProfileClick = onNavigateToProfile
                )

            is PeopleProfileUIState.Fail -> {
                NoPeopleFound()
                LaunchedEffect(Unit) {
                    Toast.makeText(
                        context,
                        (uiState as PeopleProfileUIState.Fail).throwable.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

    }
}

@Composable
private fun NoPeopleFound() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "No people found",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Loading...", modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.PeopleList(
    list: List<PeopleProfile>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onProfileClick: (profileId: Int) -> Unit
) {

    //Cause content is drawn edge-to-edge, let's set the top-padding for the first element in the list
    val statusBarHeight: Dp = WindowInsets.statusBarHeight().dp()

    LazyColumn {
        itemsIndexed(
            items = list,
            key = { _, item -> item.listKey() }
        ) { index, profile ->
            ProfileCard(
                modifier = Modifier
                    .then(
                        if (index == 0)
                            Modifier.padding(top = statusBarHeight)
                        else
                            Modifier
                    )
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            key = "image-${profile.photo}"
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                item = profile,
                onClick = { onProfileClick(profile.id) })
        }
    }
}

/*
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun PeopleListPreview() {
    AppTheme {
        PeopleListScreen(
            stateFlow = MutableStateFlow(PeopleProfileUIState.Empty),
            onNavigateToProfile = { }
        )
    }
}

 */