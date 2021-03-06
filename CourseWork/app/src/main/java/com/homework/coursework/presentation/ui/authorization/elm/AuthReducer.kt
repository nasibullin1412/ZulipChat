package com.homework.coursework.presentation.ui.authorization.elm

import com.homework.coursework.presentation.interfaces.TwoSourceHandleReducer
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

class AuthReducer @Inject constructor() : DslReducer<Event, State, Effect, Command>(),
    TwoSourceHandleReducer<Event.Internal.SuccessGetMe, State> {

    override fun Result.reduce(event: Event) = when (event) {
        is Event.Internal.SuccessAuth -> successAuth(event.apiToken)
        is Event.Internal.ErrorAuth -> effects { +Effect.ErrorAuth(event.throwable) }
        is Event.Internal.SuccessGetMe -> successGetMe(event)
        Event.Ui.CheckDatabase -> commands { +Command.CheckIsAuth }
        is Event.Ui.PressButton -> with(event) {
            commands { +Command.AuthUser(login = login, password = password) }
        }
        Event.Ui.GetMe -> {
            commands { +Command.GetMe }
        }
    }

    override var isSecondError: Boolean = false

    override fun handleResult(event: Event.Internal.SuccessGetMe): State? {
        return if (isSecondError) {
            State(
                isError = isSecondErrorChange(),
                error = event.user.errorHandle.error
            )
        } else {
            isSecondErrorChange()
            null
        }
    }

    override fun isWithError(event: Event.Internal.SuccessGetMe): Boolean {
        return event.user.errorHandle.isError
    }

    private fun Result.successAuth(apiToken: String) {
        if (apiToken.isNotEmpty()) {
            effects { +Effect.SuccessGetApiToken }
        } else {
            state { copy(isSuccess = false, isError = true, error = UnknownError()) }
        }
    }

    private fun Result.successGetMe(event: Event.Internal.SuccessGetMe) {
        if (isWithError(event)) {
            handleResult(event)?.let { state { it } }
        } else {
            effects { +Effect.SuccessAuth }
        }
    }
}
