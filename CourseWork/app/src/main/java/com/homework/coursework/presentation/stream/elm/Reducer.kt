package com.homework.coursework.presentation.stream.elm

import com.homework.coursework.presentation.interfaces.TwoSourceHandleReducer
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class Reducer : DslReducer<Event, State, Effect, Command>(),
    TwoSourceHandleReducer<Event.Internal.StreamLoaded, State> {

    override var isSecondError = false

    override fun Result.reduce(event: Event) = when (event) {
        is Event.Internal.ErrorLoading -> {
            state { copy(error = event.error, isLoading = false, isSecondError = true) }
            effects { +Effect.StreamLoadError(event.error) }
        }
        is Event.Internal.StreamLoaded -> {
            if (isWithError(event)) {
                handleResult(event)?.let { state { it } }
                effects { +Effect.StreamLoadError(event.itemList.first().errorHandle.error) }
            } else {
                state {
                    copy(
                        itemList = event.itemList,
                        isLoading = false,
                        error = null,
                        isSecondError = false
                    )
                }
            }
        }
        Event.Ui.LoadAllStreams -> {
            state { copy(isLoading = true, error = null) }
            commands { +Command.LoadAllStreams }
        }
        Event.Ui.LoadSubscribedStreams -> {
            state { copy(isLoading = true, error = null) }
            commands { +Command.LoadSubscribedStreams }
        }
        is Event.Ui.SearchStreams -> {
            state { copy(isLoading = true, error = null) }
            commands { +Command.SearchStreams(event.query) }
        }
        is Event.Ui.SearchSubscribedStreams -> {
            state { copy(isLoading = true, error = null) }
            commands { +Command.SearchSubscribedStreams(event.query) }
        }
        Event.Ui.OnStop -> {
            isSecondError = false
        }
    }

    override fun handleResult(event: Event.Internal.StreamLoaded): State? {
        return if (isSecondError) {
            State(
                itemList = event.itemList,
                isLoading = false,
                isSecondError = isSecondErrorChange(),
                error = event.itemList.first().errorHandle.error
            )
        } else {
            isSecondErrorChange()
            null
        }
    }

    override fun isWithError(event: Event.Internal.StreamLoaded): Boolean {
        return event.itemList.isNotEmpty() && event.itemList.first().errorHandle.isError
    }
}
