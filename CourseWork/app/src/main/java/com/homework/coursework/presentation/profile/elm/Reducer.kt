package com.homework.coursework.presentation.profile.elm

import com.homework.coursework.presentation.interfaces.TwoSourceHandleReducer
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class Reducer : DslReducer<Event, State, Effect, Command>(),
    TwoSourceHandleReducer<Event.Internal.UserLoaded, State> {

    override var isSecondError = false

    override fun Result.reduce(event: Event) = when (event) {
        is Event.Internal.ErrorLoading -> {
            effects { +Effect.UserLoadError(event.error) }
            state {
                copy(
                    error = event.error,
                    isError = true,
                    isLoading = false,
                    isUpdate = false
                )
            }
        }
        is Event.Internal.UserLoaded -> {
            if (isWithError(event)) {
                handleResult(event)?.let { state { it } }
                effects { +Effect.UserLoadError(event.item.errorHandle.error) }
            } else {
                state {
                    copy(
                        item = event.item,
                        isLoading = false,
                        isError = false,
                        isUpdate = true
                    )
                }
            }
        }
        Event.Ui.LoadMe -> {
            state { copy(isLoading = true, isError = false, isUpdate = false) }
            commands { +Command.LoadMe }
        }
        is Event.Ui.LoadUser -> {
            state { copy(isLoading = true, isError = false, isUpdate = false) }
            commands { +Command.LoadUser(event.userId) }
        }
        Event.Ui.OnStop -> {
            isSecondError = false
        }
    }

    override fun handleResult(event: Event.Internal.UserLoaded): State? {
        return if (isSecondError) {
            State(
                isError = isSecondErrorChange(),
                error = event.item.errorHandle.error
            )
        } else {
            isSecondErrorChange()
            null
        }
    }

    override fun isWithError(event: Event.Internal.UserLoaded): Boolean {
        return event.item.errorHandle.isError
    }
}