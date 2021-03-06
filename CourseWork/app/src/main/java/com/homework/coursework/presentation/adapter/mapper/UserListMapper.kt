package com.homework.coursework.presentation.adapter.mapper

import com.homework.coursework.domain.entity.UserData
import com.homework.coursework.presentation.adapter.data.UserItem
import dagger.Reusable
import javax.inject.Inject

@Reusable
class UserListMapper @Inject constructor() : (List<UserData>) -> (List<UserItem>) {

    @Inject
    internal lateinit var userItemMapper: UserItemMapper

    override fun invoke(userDataList: List<UserData>): List<UserItem> {
        return userDataList.mapIndexed { idx, item -> userItemMapper(item, idx) }
    }
}
