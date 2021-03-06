package com.homework.coursework.domain.usecase.message

import com.homework.coursework.domain.entity.StreamData
import com.homework.coursework.domain.entity.TopicData
import com.homework.coursework.domain.repository.MessageRepository
import io.reactivex.Observable
import javax.inject.Inject

interface AddMessageUseCase : (StreamData, TopicData, String) -> Observable<Int> {
    override fun invoke(
        streamData: StreamData,
        topicData: TopicData,
        content: String
    ): Observable<Int>
}

class AddMessageUseCaseImpl @Inject constructor(
    private val repositoryMessage: MessageRepository
) : AddMessageUseCase {

    override fun invoke(
        streamData: StreamData,
        topicData: TopicData,
        content: String
    ): Observable<Int> {
        return repositoryMessage.addMessages(
            streamData = streamData,
            topicData = topicData,
            content = content
        )
    }
}
