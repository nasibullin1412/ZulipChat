package com.homework.coursework.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    @SerialName("sender_id")
    val senderId: Int,
    val content: String,
    val timestamp: Long,
    val reactions: List<EmojiDto>,
    @SerialName("sender_full_name")
    val senderFullName: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("is_me_message")
    val isCurrentUserMessage: Boolean
)
