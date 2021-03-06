package com.homework.coursework.presentation.ui.authorization.elm

import android.os.Parcelable
import com.homework.coursework.domain.entity.UserData
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class State(
    val apiToken: @RawValue Any = Any(),
    val error: Throwable? = null,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
) : Parcelable


sealed class Event {

    sealed class Ui : Event() {
        class PressButton(val login: String, val password: String) : Ui()
        object GetMe : Ui()
        object CheckDatabase : Ui()
    }

    sealed class Internal : Event() {
        class SuccessGetMe(val user: UserData) : Internal()
        class SuccessAuth(val apiToken: String) : Internal()
        class ErrorAuth(val throwable: Throwable) : Internal()
    }
}

sealed class Effect {
    object SuccessAuth : Effect()
    object SuccessGetApiToken : Effect()
    class ErrorAuth(val error: Throwable) : Effect()
}

sealed class Command {
    class AuthUser(val login: String, val password: String) : Command()
    object CheckIsAuth : Command()
    object GetMe : Command()
}
