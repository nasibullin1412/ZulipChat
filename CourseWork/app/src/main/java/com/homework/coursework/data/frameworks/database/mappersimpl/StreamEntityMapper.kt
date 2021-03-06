package com.homework.coursework.data.frameworks.database.mappersimpl

import com.homework.coursework.data.frameworks.database.entities.StreamWithTopicsEntity
import com.homework.coursework.domain.entity.StreamData
import com.homework.coursework.domain.entity.TopicData
import dagger.Reusable
import javax.inject.Inject

@Reusable
class StreamEntityMapper @Inject constructor() :
        (List<StreamWithTopicsEntity>) -> (List<StreamData>) {
    override fun invoke(streams: List<StreamWithTopicsEntity>): List<StreamData> {
        return streams.map { streamWithTopic ->
            with(streamWithTopic) {
                StreamData(
                    id = streamEntity.streamBackId.toInt(),
                    streamName = streamEntity.streamName,
                    description = streamEntity.description,
                    dateCreated = streamEntity.dateCreated,
                    topics = topicsEntity.map {
                        TopicData(
                            id = it.backId.toInt(),
                            topicName = it.topicName,
                            numberOfMess = it.numberOfMessage
                        )
                    }
                )
            }
        }
    }
}
