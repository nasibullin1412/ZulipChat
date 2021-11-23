package com.homework.coursework.presentation.stream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.homework.coursework.databinding.StreamViewPageFragmentBinding
import com.homework.coursework.presentation.adapter.StreamNameAdapter
import com.homework.coursework.presentation.adapter.data.StreamItem
import com.homework.coursework.presentation.adapter.data.TopicItem
import com.homework.coursework.presentation.interfaces.TopicItemCallback
import com.homework.coursework.presentation.stream.elm.Effect
import com.homework.coursework.presentation.stream.elm.Event
import com.homework.coursework.presentation.stream.elm.State
import com.homework.coursework.presentation.utils.off
import vivid.money.elmslie.android.base.ElmFragment

abstract class StreamItemBaseFragment : ElmFragment<Event, Effect, State>(), TopicItemCallback {
    private lateinit var streamAdapter: StreamNameAdapter
    protected var currQuery: String = ""
    private var _binding: StreamViewPageFragmentBinding? = null
    protected val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StreamViewPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(savedInstanceState)
        initErrorRepeat()
        initRecycler()
    }

    abstract val tabState: Int

    abstract fun initErrorRepeat()

    abstract fun setFragmentResultListener(savedInstanceState: Bundle?)

    override fun onTopicItemClick(topic: TopicItem, stream: StreamItem) {
        val bundle = Bundle().apply {
            putParcelable(STREAM_KEY, stream)
            putParcelable(TOPIC_KEY, topic)
        }
        setFragmentResult(REQUEST_KEY_CHOICE, bundle)
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
            dataStreamUpdate(state.itemList)
        }
    }

    override fun onStop() {
        super.onStop()
        store.accept(Event.Ui.OnStop)
    }

    /**
     * show error layout
     */
    private fun showErrorScreen() {
        with(binding) {
            rvStreams.visibility = View.GONE
            shStreams.off()
            nsvErrorConnection.visibility = View.VISIBLE
        }
    }

    /**
     * show loading layout
     */
    private fun showLoadingScreen() {
        with(binding) {
            nsvErrorConnection.visibility = View.GONE
            shStreams.startShimmer()
        }
    }

    /**
     * show result screen
     */
    private fun showResultScreen() {
        with(binding) {
            nsvErrorConnection.isVisible = false
            shStreams.off()
            rvStreams.visibility = View.VISIBLE
        }
    }

    /**
     * update channels recycle view
     * @param listStreams is new list of channels for recycle
     */
    private fun dataStreamUpdate(listStreams: List<StreamItem>) {
        binding.rvStreams.visibility = View.VISIBLE
        streamAdapter.submitList(listStreams)
    }

    private fun initRecycler() {
        streamAdapter = StreamNameAdapter().apply {
            setTopicListener(this@StreamItemBaseFragment)
        }
        with(binding.rvStreams) {
            val itemDecorator = getDividerItemDecoration()
            addItemDecoration(itemDecorator)
            adapter = streamAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    companion object {
        const val REQUEST_KEY_CHOICE = "requestKeyChoice"
        const val STREAM_KEY = "stream"
        const val TOPIC_KEY = "topic"
    }
}