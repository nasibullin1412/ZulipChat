package com.homework.coursework.presentation.adapter.mapper

import com.homework.coursework.domain.entity.EmojiData
import com.homework.coursework.presentation.adapter.data.EmojiItem
import javax.inject.Inject

class EmojiDataListMapper @Inject constructor() : (List<EmojiItem?>) -> (List<EmojiData>) {
    @Inject
    internal lateinit var emojiDataMapper: EmojiDataMapper

    override fun invoke(emojiList: List<EmojiItem?>): List<EmojiData> {
        return emojiList.map { emojiDataMapper(it) }
    }
}
