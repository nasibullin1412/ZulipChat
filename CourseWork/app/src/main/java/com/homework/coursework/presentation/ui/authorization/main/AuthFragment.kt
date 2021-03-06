package com.homework.coursework.presentation.ui.authorization.main

import android.os.Bundle
import com.homework.coursework.presentation.App
import com.homework.coursework.presentation.ui.authorization.AuthBaseFragment
import com.homework.coursework.presentation.ui.authorization.elm.Effect
import com.homework.coursework.presentation.ui.authorization.elm.Event
import com.homework.coursework.presentation.ui.authorization.elm.State
import com.homework.coursework.presentation.utils.CustomFragmentFactory
import com.homework.coursework.presentation.utils.FragmentTag
import com.homework.coursework.presentation.utils.showToast
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class AuthFragment : AuthBaseFragment() {

    @Inject
    internal lateinit var authStore: Store<Event, Effect, State>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.authComponent().inject(this)
    }

    override val initEvent: Event
        get() = Event.Ui.CheckDatabase

    override fun authAction() {
        val login = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        store.accept(Event.Ui.PressButton(login = login, password = password))
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ErrorAuth -> {
            showToast(effect.error.message)
        }
        is Effect.SuccessAuth -> {
            navigateController?.navigateFragment(
                customFragmentFactory = CustomFragmentFactory.create(FragmentTag.STREAM_FRAGMENT_TAG)
            )
        }
        is Effect.SuccessGetApiToken -> {
            store.accept(Event.Ui.GetMe)
        }
    }

    override fun render(state: State) {}

    override fun createStore(): Store<Event, Effect, State> = authStore
}
