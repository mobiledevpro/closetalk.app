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
package com.mobiledevpro.peoplelist.view.component

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobiledevpro.domain.model.PeopleProfile
import com.mobiledevpro.domain.model.fakePeopleProfileList
import com.mobiledevpro.ui.component.CardItem
import com.mobiledevpro.ui.component.ProfileContent
import com.mobiledevpro.ui.component.ProfilePicture
import com.mobiledevpro.ui.component.ProfilePictureSize
import com.mobiledevpro.ui.theme.AppTheme

/**
 * For People list
 *
 * Created on Feb 05, 2023.
 *
 */
@Composable
internal fun ProfileCard(modifier: Modifier = Modifier, item: PeopleProfile, onClick: () -> Unit) {
    CardItem(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick.invoke() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(
                item.photo ?: Uri.EMPTY,
                item.status,
                size = ProfilePictureSize.MEDIUM,
                modifier = Modifier.padding(16.dp)
            )
            ProfileContent(
                userName = item.name,
                subName = null,
                isOnline = item.status,
                alignment = Alignment.Start,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
@Preview
internal fun ProfileCardPreview() {
    AppTheme {
        ProfileCard(
            item = fakePeopleProfileList[0],
            onClick = {}
        )
    }
}
