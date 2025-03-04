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
package com.mobiledevpro.chatlist.view

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mobiledevpro.chatlist.domain.usecase.GetChatListUseCase
import com.mobiledevpro.ui.vm.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatListViewModel(
    private val getChatListUseCase: GetChatListUseCase
) : BaseViewModel<ChatListUIState>() {

    override fun initUIState(): ChatListUIState = ChatListUIState.Empty

    init {
        Log.d("UI", "ChatListViewModel init")
        observeChatList()
    }

    private fun observeChatList() {
        viewModelScope.launch {
            getChatListUseCase.execute()
                .collectLatest { result ->
                    result.onSuccess { list ->
                        _uiState.update {
                            if (it is ChatListUIState.Success)
                                it.copy(chatList = list)
                            else
                                ChatListUIState.Success(list)
                        }
                    }.onFailure { err ->
                        _uiState.update {
                            ChatListUIState.Fail(err)
                        }
                    }
                }
        }
    }
}