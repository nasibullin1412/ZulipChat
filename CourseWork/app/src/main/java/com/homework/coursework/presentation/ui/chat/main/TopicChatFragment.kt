package com.homework.coursework.presentation.ui.chat.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.homework.coursework.presentation.App
import com.homework.coursework.presentation.adapter.data.*
import com.homework.coursework.presentation.ui.chat.ChatBaseFragment
import com.homework.coursework.presentation.ui.chat.elm.Effect
import com.homework.coursework.presentation.ui.chat.elm.Event
import com.homework.coursework.presentation.ui.chat.elm.State
import com.homework.coursework.presentation.ui.chat.initRecycleViewImpl
import com.homework.coursework.presentation.ui.chat.onEmojiClickedImpl
import com.homework.coursework.presentation.ui.stream.StreamItemBaseFragment
import com.homework.coursework.presentation.utils.getValueByCondition
import com.homework.coursework.presentation.utils.showToast
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class TopicChatFragment : ChatBaseFragment() {

    internal lateinit var currentTopic: TopicItem

    internal lateinit var currentStream: StreamItem

    internal var updateMessage: Int = 0

    @Inject
    internal lateinit var topicChatStore: Store<Event, Effect, State>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.topicChatComponent().inject(this)
    }

    override fun initArguments() {
        currentTopic = requireArguments().getParcelable(StreamItemBaseFragment.TOPIC_KEY)
            ?: throw IllegalArgumentException("topic required")
        currentStream = requireArguments().getParcelable(StreamItemBaseFragment.STREAM_KEY)
            ?: throw IllegalArgumentException("stream required")
    }

    override fun initErrorRepeat() {
        binding.errorContent.tvRepeat.setOnClickListener {
            store.accept(
                Event.Ui.LoadFirstPage(
                    streamItem = currentStream,
                    topicItem = currentTopic,
                    currId = currId
                )
            )
        }
    }

    /**
     * update recycler view with new message
     * @param newList is list with new MessageData
     */
    private fun updateMessage(newList: List<ChatItem>) {
        chatAdapter.submitList(newList)
    }

    override fun onEmojiClicked(emojiIdx: Int) {
        onEmojiClickedImpl(emojiIdx)
    }

    override fun onEmojiClicked(emojiItem: EmojiItem) {
        onEmojiClickedImpl(emojiItem)
    }

    override fun initRecycleView() {
        initRecycleViewImpl()
    }

    @SuppressLint("SetTextI18n")
    override fun initNames() {
        binding.tvStreamNameBar.text = currentStream.streamName
        binding.tvTopicName.text = "Topic: ${currentTopic.topicName}"
    }

    override fun initButtonListener() {
        binding.imgSend.setOnClickListener {
            store.accept(
                Event.Ui.SendMessage(
                    streamItem = currentStream,
                    topicItem = currentTopic,
                    content = binding.etMessage.text.toString()
                )
            )
            binding.etMessage.setText("")
        }
    }

    override fun createStore(): Store<Event, Effect, State> = topicChatStore

    override fun onStop() {
        super.onStop()
        val numberOfMessage = chatAdapter.currentList.filterIsInstance<MessageItem>().size
        val last = DATABASE_MESSAGE_THRESHOLD.getValueByCondition(
            condition = DATABASE_MESSAGE_THRESHOLD < numberOfMessage,
            second = numberOfMessage
        )
        store.accept(
            Event.Ui.SaveMessage(
                streamItem = currentStream,
                topicItem = currentTopic,
                messages = chatAdapter.currentList.takeLast(last),
                currId = currId
            )
        )
        bottomNavigationController?.visibleBottomNavigation()
    }

    override fun render(state: State) {
        if (state.isLoading) {
            showLoadingScreen()
            return
        }
        if (state.isError) {
            showErrorScreen()
            return
        }
        if (state.isUpdate) {
            showResultScreen()
            updateMessage(state.itemList)
        }
    }

    override fun handleEffect(effect: Effect) {
        when (effect) {
            is Effect.ErrorCommands -> {
                showToast(effect.error.message)
            }
            is Effect.MessageAdded -> {
                chatAdapter.submitList(emptyList())
                store.accept(
                    Event.Ui.LoadNextPage(
                        streamItem = currentStream,
                        topicItem = currentTopic.copy(numberOfMess = effect.id),
                        currId = currId
                    )
                )
            }
            Effect.MessagesSaved -> {
                Log.d("SaveLog", "Success save all messages")
            }
            is Effect.NextPageLoadError -> {
                showToast(effect.error.message)
            }
            is Effect.PageLoaded -> {
                store.accept(
                    Event.Ui.MergeOldList(
                        oldList = chatAdapter.currentList,
                        newList = effect.itemList
                    )
                )
            }
            Effect.ReactionChanged -> {
                store.accept(
                    Event.Ui.UpdateMessage(
                        streamItem = currentStream,
                        topicItem = currentTopic.copy(numberOfMess = updateMessage),
                        currId = currId
                    )
                )
            }
            is Effect.SuccessGetId -> {
                currId = effect.currId
                store.accept(
                    Event.Ui.LoadFirstPage(
                        streamItem = currentStream,
                        topicItem = currentTopic,
                        currId = currId
                    )
                )
            }
        }
    }

    companion object {
        fun newInstance(topic: TopicItem, stream: StreamItem): ChatBaseFragment {
            val args = Bundle().apply {
                putParcelable(StreamItemBaseFragment.TOPIC_KEY, topic)
                putParcelable(StreamItemBaseFragment.STREAM_KEY, stream)
            }
            val fragment = TopicChatFragment()
            fragment.arguments = args
            return fragment
        }
    }
}