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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobiledevpro.ui.component.ScreenBackground
import com.mobiledevpro.ui.theme.AppTheme


@Composable
fun PeopleListScreen(onNavigateToProfile: (profileId: Int) -> Unit) {
    val viewModel: PeopleListViewModel = viewModel()

    ScreenBackground(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Color(0x80EF6C00))
        ) {
            Text(
                text = "People list",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge

            )

            Button(
                onClick = { onNavigateToProfile(2) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .defaultMinSize(minWidth = 144.dp, minHeight = 48.dp)
            ) {
                Text(
                    text = "Person profile"
                )
            }
        }
    }
}

@Preview
@Composable
fun PeopleListPreview() {
    AppTheme {
        PeopleListScreen(
            onNavigateToProfile = { }
        )
    }
}