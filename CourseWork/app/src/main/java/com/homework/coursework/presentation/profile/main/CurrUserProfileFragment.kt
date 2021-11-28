package com.homework.coursework.presentation.profile.main

import android.os.Bundle
import com.homework.coursework.di.CurrUserStore
import com.homework.coursework.presentation.App
import com.homework.coursework.presentation.profile.BaseProfileFragment
import com.homework.coursework.presentation.profile.elm.Effect
import com.homework.coursework.presentation.profile.elm.Event
import com.homework.coursework.presentation.profile.elm.State
import kotlinx.serialization.ExperimentalSerializationApi
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

@ExperimentalSerializationApi
class CurrUserProfileFragment : BaseProfileFragment() {

    @Inject
    @CurrUserStore
    internal lateinit var currUserProfileStore: Store<Event, Effect, State>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.currUserProfileComponent().inject(this)
    }

    override val initEvent: Event
        get() = Event.Ui.LoadMe

    override fun createStore(): Store<Event, Effect, State> = currUserProfileStore

    override fun initErrorRepeat() {
        binding.errorContent.tvRepeat.setOnClickListener {
            store.accept(Event.Ui.LoadMe)
        }
    }
}
