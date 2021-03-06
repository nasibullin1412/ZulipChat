package com.homework.coursework.domain.repository

import com.homework.coursework.domain.entity.StreamData
import io.reactivex.Observable

interface StreamRepository {
    fun loadAllStreams(): Observable<List<StreamData>>
    fun loadSubscribedStreams(): Observable<List<StreamData>>
}
