package com.homework.coursework.data.mappers

import com.homework.coursework.data.dto.MessagesResponse
import com.homework.coursework.domain.entity.MessageData
import com.homework.coursework.domain.entity.UserData
import com.homework.coursework.presentation.frameworks.network.utils.NetworkConstants.USER_ID
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MessageDtoMapper : (MessagesResponse) -> (List<MessageData>) {

    private val emojiDtoMapper: EmojiDtoMapper = EmojiDtoMapper()

    override fun invoke(messagesDto: MessagesResponse): List<MessageData> {
        return messagesDto.data?.map { messageDto ->
            with(messageDto) {
                MessageData(
                    messageId = id,
                    userData = UserData(
                        id = senderId,
                        name = senderFullName,
                        avatarUrl = avatarUrl,
                        userMail = senderEmail
                    ),
                    messageContent = content.parseHtmlContent(),
                    emojis = emojiDtoMapper(reactions),
                    date = timestamp,
                    isCurrentUserMessage = senderId == USER_ID
                )
            }
        } ?: throw IllegalArgumentException(messagesDto.msg)
    }
}