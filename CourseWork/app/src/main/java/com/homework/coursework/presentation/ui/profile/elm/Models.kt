package com.homework.coursework.presentation.ui.profile.elm

import android.os.Parcelable
import com.homework.coursework.presentation.adapter.data.UserItem
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class State(
    val item: @RawValue Any = Any(),
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isUpdate: Boolean = false
) : Parcelable

sealed class Event {

    sealed class Ui : Event() {
        object LoadMe : Ui()
        object OnStop : Ui()
        object LogoutUser : Ui()
        class LoadUser(val userId: Int) : Ui()
    }

    sealed class Internal : Event() {
        class UserLoaded(val item: UserItem) : Internal()
        object ErrorLogout : Internal()
        object SuccessLogout : Internal()
        class ErrorLoading(val error: Throwable) : Internal()
    }
}

sealed class Effect {
    class UserLoadError(val error: Throwable) : Effect()
    object SuccessLogout : Effect()
    object ErrorLogout : Effect()
}

sealed class Command {
    object LoadMe : Command()
    class LoadUser(val id: Int) : Command()
    object LogoutUser : Command()
}
