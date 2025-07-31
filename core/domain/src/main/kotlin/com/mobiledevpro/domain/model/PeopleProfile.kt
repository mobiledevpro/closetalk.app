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
package com.mobiledevpro.domain.model

import android.net.Uri
import java.util.UUID
/**
 * Profile
 *
 * Created on Feb 04, 2023.
 *
 */

data class PeopleProfile(
    val uuid: String,
    val name: String,
    val surname: String,
    val online: Boolean,
    val photo: Uri? = null,
    val instagram: Uri? = Uri.parse("https://instagram.com/mobiledevpro"),
    val linkedin: Uri? = Uri.parse("https://www.linkedin.com/in/dmitriychernysh/"),
    val youtube: Uri? = Uri.parse("https://www.youtube.com/@mobiledevpro"),
    val twitter: Uri? = Uri.parse("https://twitter.com/mobiledev_pro")
) {
    fun listKey(): String = "${uuid}_${name.replace("\\s+".toRegex(), "")}"

    fun fullName(): String = if (surname.isNotEmpty())
        "$name $surname"
    else
        name
}


val fakePeopleProfileList = arrayListOf(
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Michaela",
        surname = "Runnings",
        online = true,
        photo = Uri.parse("https://images.unsplash.com/photo-1485290334039-a3c69043e517?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80")

    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "John",
        surname = "Pestridge",
        online = false,
        photo = Uri.parse("https://images.unsplash.com/photo-1542178243-bc20204b769f?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MTB8fHBvcnRyYWl0fGVufDB8MnwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Manilla",
        surname = "Andrews",
        online = true,
        photo = Uri.parse("https://images.unsplash.com/photo-1543123820-ac4a5f77da38?ixid=MXwxMjA3fDB8MHxzZWFyY2h8NDh8fHBvcnRyYWl0fGVufDB8MnwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Dan",
        surname = "Spicer",
        online = false,
        photo = Uri.parse("https://images.unsplash.com/photo-1595152772835-219674b2a8a6?ixid=MXwxMjA3fDB8MHxzZWFyY2h8NDd8fHBvcnRyYWl0fGVufDB8MnwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Keanu",
        surname = "Dester",
        online = false,
        photo = Uri.parse("https://images.unsplash.com/photo-1597528380214-aa94bde3fc32?ixid=MXwxMjA3fDB8MHxzZWFyY2h8NTZ8fHBvcnRyYWl0fGVufDB8MnwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Anichu Patel",
        surname = "Patel",
        online = false,
        photo = Uri.parse("https://images.unsplash.com/photo-1598641795816-a84ac9eac40c?ixid=MXwxMjA3fDB8MHxzZWFyY2h8NjJ8fHBvcnRyYWl0fGVufDB8MnwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Kienla",
        surname = "Onso",
        online = true,
        photo = Uri.parse("https://images.unsplash.com/photo-1566895733044-d2bdda8b6234?ixid=MXwxMjA3fDB8MHxzZWFyY2h8ODh8fHBvcnRyYWl0fGVufDB8MnwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Andra",
        surname = "Matthews",
        online = false,
        photo = Uri.parse("https://images.unsplash.com/photo-1530577197743-7adf14294584?ixid=MXwxMjA3fDB8MHxzZWFyY2h8NTV8fHBvcnRyYWl0fGVufDB8MnwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Georgia",
        surname = "S.",
        online = false,
        photo = Uri.parse("https://images.unsplash.com/photo-1547212371-eb5e6a4b590c?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MTA3fHxwb3J0cmFpdHxlbnwwfDJ8MHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Matt",
        surname = "Dengo",
        online = false,
        photo = Uri.parse("https://images.unsplash.com/photo-1578176603894-57973e38890f?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MTE0fHxwb3J0cmFpdHxlbnwwfDJ8MHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Marsha",
        surname = "T.",
        online = true,
        photo = Uri.parse("https://images.unsplash.com/photo-1605087880595-8cc6db61f3c6?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MTI0fHxwb3J0cmFpdHxlbnwwfDJ8MHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    ),
    PeopleProfile(
        uuid = UUID.randomUUID().toString(),
        name = "Invshu",
        surname = "Patel",
        online = true,
        photo = Uri.parse("https://images.unsplash.com/photo-1561820009-8bef03ebf8e5?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MTM3fHxwb3J0cmFpdHxlbnwwfDJ8MHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
    )
)

fun List<PeopleProfile>.toChatName(): String =
    mapTo(ArrayList<String>()) { profile -> profile.name }.let { names ->
        val stringBuilder = StringBuilder()
        names.onEachIndexed { index, s ->
            if (index > 0)
                stringBuilder.append(", ")
            stringBuilder.append(s)
        }
        stringBuilder.toString()
    }