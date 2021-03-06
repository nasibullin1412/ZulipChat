package com.homework.coursework.presentation.ui.stream

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.homework.coursework.R
import com.homework.coursework.databinding.FragmentStreamBinding
import com.homework.coursework.presentation.App
import com.homework.coursework.presentation.adapter.StreamAdapter
import com.homework.coursework.presentation.adapter.data.StreamItem
import com.homework.coursework.presentation.adapter.data.TopicItem
import com.homework.coursework.presentation.interfaces.BottomNavigationController
import com.homework.coursework.presentation.interfaces.NavigateController
import com.homework.coursework.presentation.ui.chat.ChatBaseFragment.Companion.STREAM_KEY
import com.homework.coursework.presentation.ui.chat.main.StreamChatFragment
import com.homework.coursework.presentation.ui.chat.main.TopicChatFragment
import com.homework.coursework.presentation.ui.chat.main.TopicChatFragment.Companion.TOPIC_KEY
import com.homework.coursework.presentation.ui.stream.StreamItemBaseFragment.Companion.REQUEST_KEY_CHOICE
import com.homework.coursework.presentation.utils.CustomFragmentFactory
import com.homework.coursework.presentation.utils.FragmentTag
import com.homework.coursework.presentation.utils.SearchListener
import com.homework.coursework.presentation.utils.showToast
import javax.inject.Inject

class StreamFragment : Fragment() {

    @Inject
    internal lateinit var searchListener: SearchListener

    private var _binding: FragmentStreamBinding? = null
    private val binding get() = _binding!!
    private lateinit var streamAdapter: StreamAdapter
    private lateinit var viewPager: ViewPager2
    private var navigateController: NavigateController? = null
    private var bottomNavigationController: BottomNavigationController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigateController) {
            navigateController = context
        }
        if (context is BottomNavigationController) {
            bottomNavigationController = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentListener()
        App.appComponent.streamComponent().inject(this)
        searchListener.subscribeToSearchSubject({ setQuery(it) }, { showToast(it) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        streamAdapter = StreamAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = streamAdapter
        val itemTextList = resources.getStringArray(R.array.view_pager_items)
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = itemTextList[position]
        }.attach()
        initSearchEditText()
        initCreateButton()
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationController?.visibleBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        navigateController = null
        bottomNavigationController = null
    }

    private fun initFragmentListener() {
        childFragmentManager.setFragmentResultListener(
            REQUEST_KEY_CHOICE,
            this
        ) { _, bundle ->
            val stream = bundle.getParcelable<StreamItem>(STREAM_KEY)
                ?: throw IllegalArgumentException("Stream required")
            val topic = bundle.getParcelable<TopicItem>(TOPIC_KEY)
                ?: throw IllegalArgumentException("Topic required")
            navigateToChatFragment(stream, topic)
        }
    }

    private fun navigateToChatFragment(stream: StreamItem, topic: TopicItem) {
        if (topic.id == TopicItem.TOPIC_UNKNOWN_ID) {
            navigateController?.navigateFragment(
                customFragmentFactory = CustomFragmentFactory.create(
                    FragmentTag.STREAM_CHAT_FRAGMENT_TAG,
                    bundle = StreamChatFragment.createBundle(stream = stream)
                )
            )
            return
        }
        navigateController?.navigateFragment(
            customFragmentFactory = CustomFragmentFactory.create(
                FragmentTag.TOPIC_CHAT_FRAGMENT_TAG,
                bundle = TopicChatFragment.createBundle(topic = topic, stream = stream)
            )
        )
    }

    private fun initSearchEditText() {
        binding.etSearch.doAfterTextChanged {
            searchListener.searchTopics(it.toString())
        }
    }

    private fun setQuery(query: String) {
        val position = binding.pager.currentItem
        childFragmentManager.setFragmentResult(
            "$KEY_QUERY_REQUEST$position",
            bundleOf(KEY_QUERY_DATA to query)
        )
    }

    private fun initCreateButton() {
        binding.fabNewStream.setOnClickListener {
            navigateController?.navigateFragment(
                customFragmentFactory = CustomFragmentFactory.create(
                    FragmentTag.CREATE_STREAM_FRAGMENT_TAG
                )
            )
            childFragmentManager.setFragmentResult(
                CREATE_STREAM_REQUEST,
                bundleOf("" to true)
            )
        }
    }

    companion object {
        const val KEY_QUERY_REQUEST = "queryRequest"
        const val KEY_QUERY_DATA = "queryData"
        const val CREATE_STREAM_REQUEST = "requestCreateStream"
    }
}
