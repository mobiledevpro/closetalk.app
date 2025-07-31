package com.mobiledevpro.domain.model

import android.net.Uri

/**
 * Chat
 *
 * Created on May 06, 2023.
 *
 */
data class Chat(
    val user: UserProfile,
    val peopleList: List<PeopleProfile>,
    val unreadMsgCount : Int = 0
)

val fakeUser = UserProfile(
    name = "Joe Black",
    nickname = "@nickname",
    status = true,
    photo = Uri.parse("https://media.istockphoto.com/id/1090878494/photo/close-up-portrait-of-young-smiling-handsome-man-in-blue-polo-shirt-isolated-on-gray-background.jpg?b=1&s=170667a&w=0&k=20&c=c3TaqVe9-0EcHl7mjO-9YChSvGBDhvzUai6obs1Ibz4=")
)

val fakeChatList = arrayListOf(
    Chat(
        user = fakeUser,
        peopleList = fakePeopleProfileList.take(5).sortedByDescending { !it.online },
        unreadMsgCount = 100
    ),
    Chat(
        user = fakeUser,
        peopleList = fakePeopleProfileList.takeLast(3).sortedByDescending { !it.online },
        unreadMsgCount = 0
    ),

    Chat(
        user = fakeUser,
        peopleList = listOf(fakePeopleProfileList[6]),
        unreadMsgCount = 3
    )
)

fun Chat.name() : String =
    this.peopleList.toChatName()
