package com.homework.coursework.presentation.ui.profile.elm

import com.homework.coursework.domain.usecase.users.GetMeUseCase
import com.homework.coursework.domain.usecase.users.GetUserProfileUseCase
import com.homework.coursework.domain.usecase.users.LogoutUserUseCase
import com.homework.coursework.presentation.adapter.mapper.UserItemMapper
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ProfileActor(
    private val getMe: GetMeUseCase,
    private val getUser: GetUserProfileUseCase,
    private val logoutUser: LogoutUserUseCase,
    private val userItemMapper: UserItemMapper
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {
        Command.LoadMe -> {
            getMe()
                .map { userItemMapper(it, 0) }
                .mapEvents(
                    { item -> Event.Internal.UserLoaded(item = item) },
                    { error -> Event.Internal.ErrorLoading(error = error) }
                )
        }
        is Command.LoadUser -> {
            getUser(command.id)
                .map { userItemMapper(it, 0) }
                .mapEvents(
                    { item -> Event.Internal.UserLoaded(item = item) },
                    { error -> Event.Internal.ErrorLoading(error = error) }
                )
        }
        Command.LogoutUser -> {
            logoutUser()
                .mapEvents(Event.Internal.SuccessLogout, Event.Internal.ErrorLogout)
        }
    }
}
