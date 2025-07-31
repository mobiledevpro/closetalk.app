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
package com.mobiledevpro.people.profile.view

import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobiledevpro.domain.model.PeopleProfile
import com.mobiledevpro.people.profile.R
import com.mobiledevpro.people.profile.view.state.PeopleProfileUIState
import com.mobiledevpro.ui.component.ProfileContent
import com.mobiledevpro.ui.component.ProfilePicture
import com.mobiledevpro.ui.component.ProfilePictureSize
import com.mobiledevpro.ui.component.ScreenBackground
import kotlinx.coroutines.flow.StateFlow
import com.mobiledevpro.ui.R as RApp

/**
 * Profile screen for selected person from People list
 *
 * Created on Feb 03, 2023.
 *
 */

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PeopleProfileScreen(
    state: StateFlow<PeopleProfileUIState>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackPressed: () -> Unit,
    onOpenChatWith: (profile: PeopleProfile) -> Unit,
    onOpenSocialLink: (Uri) -> Unit
) {
    val context = LocalContext.current

    val uiState by state.collectAsStateWithLifecycle()
    val backgroundBoxTopOffset = remember { mutableIntStateOf(0) }

    var peopleProfile by remember { mutableStateOf<PeopleProfile?>(null) }

    if (uiState is PeopleProfileUIState.Success) {
        (uiState as PeopleProfileUIState.Success).also { st ->
            peopleProfile = st.profile

        }
    } else if (uiState is PeopleProfileUIState.Fail) {
        LaunchedEffect(Unit) {
            Toast.makeText(
                context,
                (uiState as PeopleProfileUIState.Fail).throwable.localizedMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    ScreenBackground(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        //Background with rounded top-corners
        Box(
            modifier = Modifier
                .offset { IntOffset(0, backgroundBoxTopOffset.value) }
                .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp))
                .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            IconButton(
                onClick = { onBackPressed() },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = RApp.drawable.ic_arrow_back_white_24dp),
                    contentDescription = ""
                )
            }

            peopleProfile?.let { profile ->
                ProfilePicture(
                    photoUri = profile.photo ?: Uri.EMPTY,
                    onlineStatus = profile.online,
                    size = ProfilePictureSize.LARGE,
                    modifier = Modifier
                        .padding(paddingValues = PaddingValues(16.dp, 16.dp, 16.dp, 16.dp))
                        .align(Alignment.CenterHorizontally)
                        .onGloballyPositioned {
                            val rect = it.boundsInParent()
                            backgroundBoxTopOffset.value =
                                rect.topCenter.y.toInt() + (rect.bottomCenter.y - rect.topCenter.y).toInt() / 2
                        }
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "image-${profile.photo}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                )

                ProfileContent(
                    userName = profile.fullName(),
                    isOnline = profile.online,
                    alignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                )

                ProfileSocialIcons(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    profile = profile,
                    onOpenSocialLink = onOpenSocialLink
                )

                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        onClick = {
                            profile.also(onOpenChatWith)
                        },
                        modifier = Modifier
                            .padding(bottom = 48.dp, top = 16.dp, start = 16.dp, end = 16.dp)
                            .defaultMinSize(minHeight = 48.dp, minWidth = 144.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    ) {
                        Text(text = "Say Hi \uD83D\uDC4B")
                    }
                }
            }
        }

    }
}

@Composable
fun ProfileSocialIcons(
    profile: PeopleProfile,
    modifier: Modifier,
    onOpenSocialLink: (Uri) -> Unit
) {
    Row(
        modifier = modifier
    ) {

        profile.instagram?.also { uri ->
            SocialButton(
                iconResId = R.drawable.ic_instagram_white_48dp,
                link = uri,
                onClick = onOpenSocialLink
            )
        }

        profile.twitter?.also { uri ->
            SocialButton(
                iconResId = R.drawable.ic_twitter_white_48dp,
                link = uri,
                onClick = onOpenSocialLink
            )
        }

        profile.youtube?.also { uri ->
            SocialButton(
                iconResId = R.drawable.ic_youtube_white_48dp,
                link = uri,
                onClick = onOpenSocialLink
            )
        }

        profile.linkedin?.also { uri ->
            SocialButton(
                iconResId = R.drawable.ic_linkedin_white_48dp,
                link = uri,
                onClick = onOpenSocialLink
            )
        }
    }
}

@Composable
fun SocialButton(@DrawableRes iconResId: Int, link: Uri, onClick: (Uri) -> Unit) {
    IconButton(
        onClick = { onClick(link) }
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "",
            modifier = Modifier.padding(4.dp),
        )
    }
}

/*
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun SharedTransitionScope.PeopleProfilePreview() {
    AppTheme(darkTheme = true) {
        fakePeopleProfileList.find { it.id == 2 }?.let {
            PeopleProfileScreen(
                it,
                animatedVisibilityScope = null,
                onBackPressed = {},
                onOpenChatWith = {}
            )
        }
    }
}

 */