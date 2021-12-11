package com.homework.coursework.presentation.ui.stream.main

import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import com.homework.coursework.di.AllStreams
import com.homework.coursework.presentation.App
import com.homework.coursework.presentation.ui.stream.StreamFragment
import com.homework.coursework.presentation.ui.stream.StreamItemBaseFragment
import com.homework.coursework.presentation.ui.stream.elm.Effect
import com.homework.coursework.presentation.ui.stream.elm.Event
import com.homework.coursework.presentation.ui.stream.elm.State
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class StreamAllFragment : StreamItemBaseFragment() {
    override val tabState: Int
        get() = 1

    @Inject
    @AllStreams
    internal lateinit var allStreamsStore: Store<Event, Effect, State>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.allStreamsComponent().inject(this)
    }

    override fun initErrorRepeat() {
        binding.errorContent.tvRepeat.setOnClickListener {
            store.accept(Event.Ui.LoadAllStreams)
        }
    }

    override fun setFragmentResultListener(savedInstanceState: Bundle?) {
        setFragmentResultListener(
            "${StreamFragment.KEY_QUERY_REQUEST}$tabState"
        ) { _, bundle ->
            currQuery = bundle.getString(StreamFragment.KEY_QUERY_DATA) ?: ""
            store.accept(getNeedEvent(currQuery))
        }
    }

    override val initEvent: Event
        get() = Event.Ui.LoadAllStreams


    private fun getNeedEvent(query: String? = null) = if (query != null) {
        Event.Ui.SearchStreams(query)
    } else {
        Event.Ui.LoadAllStreams
    }

    override fun createStore(): Store<Event, Effect, State> = allStreamsStore
}