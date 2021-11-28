package com.homework.coursework.di.subcomponents

import com.homework.coursework.di.StreamFragmentScope
import com.homework.coursework.di.modules.StreamModule
import com.homework.coursework.presentation.stream.main.StreamAllFragment
import dagger.Subcomponent
import kotlinx.serialization.ExperimentalSerializationApi

@StreamFragmentScope
@Subcomponent(modules = [StreamModule::class])
@ExperimentalSerializationApi
interface AllStreamComponent {
    fun inject(streamFragment: StreamAllFragment)
}