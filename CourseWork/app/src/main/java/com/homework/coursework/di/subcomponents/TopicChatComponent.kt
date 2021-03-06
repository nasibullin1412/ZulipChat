package com.homework.coursework.di.subcomponents

import com.homework.coursework.di.ChatFragmentScope
import com.homework.coursework.di.modules.ChatModule
import com.homework.coursework.presentation.ui.chat.main.TopicChatFragment
import dagger.Subcomponent

@ChatFragmentScope
@Subcomponent(modules = [ChatModule::class])
interface TopicChatComponent {
    fun inject(topicChatFragment: TopicChatFragment)
}
