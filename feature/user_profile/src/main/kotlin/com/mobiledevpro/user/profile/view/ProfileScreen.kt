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
package com.mobiledevpro.user.profile.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mobiledevpro.domain.model.UserProfile
import com.mobiledevpro.domain.model.fakeUser
import com.mobiledevpro.ui.component.LabeledDarkModeSwitch
import com.mobiledevpro.ui.component.ProfileContent
import com.mobiledevpro.ui.component.ProfilePicture
import com.mobiledevpro.ui.component.ProfilePictureSize
import com.mobiledevpro.ui.component.ScreenBackground
import com.mobiledevpro.ui.component.SettingsButton
import com.mobiledevpro.ui.theme.AppTheme
import com.mobiledevpro.user.profile.view.state.UserProfileUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.mobiledevpro.ui.R as RUi

@Composable
fun ProfileScreen(
    state: StateFlow<UserProfileUIState>,
    onDarkModeSwitched: (Boolean) -> Unit,
    onNavigateToSubscription: () -> Unit
) {
    Log.d("navigation", "ProfileScreen:")

    val uiState by state.collectAsStateWithLifecycle()

    var backgroundBoxTopOffset by remember { mutableIntStateOf(0) }
    var darkModeOn by remember { mutableStateOf(true) }
    var userProfile by remember { mutableStateOf<UserProfile>(UserProfile()) }

    if (uiState is UserProfileUIState.Success) {
        (uiState as UserProfileUIState.Success).userProfile?.let {
            userProfile = it
        }

        (uiState as UserProfileUIState.Success).settings?.let {
            darkModeOn = it.darkMode
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
                .offset { IntOffset(0, backgroundBoxTopOffset) }
                .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp))
                .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
        )

        Box(modifier = Modifier.fillMaxSize()) {

            BeLikeAProButton(modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(56.dp)
                .clickable { onNavigateToSubscription() })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.Top
            ) {

                ProfilePicture(
                    photoUri = userProfile.photo,
                    onlineStatus = true,
                    size = ProfilePictureSize.LARGE,
                    modifier = Modifier
                        .padding(paddingValues = PaddingValues(16.dp, 16.dp, 16.dp, 16.dp))
                        .align(Alignment.CenterHorizontally)
                        .onGloballyPositioned {
                            val rect = it.boundsInParent()
                            backgroundBoxTopOffset =
                                rect.topCenter.y.toInt() + (rect.bottomCenter.y - rect.topCenter.y).toInt() / 2
                        }
                )

                ProfileContent(
                    userName = userProfile.name,
                    subName = userProfile.nickname,
                    isOnline = userProfile.status,
                    alignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.Bottom
                ) {

                    LabeledDarkModeSwitch(
                        label = "Dark mode",
                        checked = darkModeOn,
                        onCheckedChanged = { isDark ->
                            Log.d("main", "ProfileScreen: dark $isDark")
                            darkModeOn = isDark
                            onDarkModeSwitched(isDark)
                        })

                    Divider()

                    SettingsButton(
                        label = "Log Out",
                        icon = Icons.Rounded.ExitToApp,
                        onClick = {

                        }
                    )
                }
            }


        }
    }
}

@Composable
fun BeLikeAProButton(modifier: Modifier) {
    //Here is how to use Lottie animation with Compose https://github.com/airbnb/lottie/blob/master/android-compose.md

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(RUi.raw.ic_crowd))

    LottieAnimation(composition = composition, modifier = modifier)
}

@Preview
@Composable
fun ProfileScreenPreview() {
    AppTheme(darkTheme = true) {
        ProfileScreen(
            state =  MutableStateFlow(UserProfileUIState.Success(fakeUser)),
            onDarkModeSwitched = {},
            onNavigateToSubscription = {}
        )
    }
}