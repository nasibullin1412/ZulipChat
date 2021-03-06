package com.homework.coursework.presentation.adapter.mapper

import com.homework.coursework.domain.entity.TopicData
import com.homework.coursework.presentation.adapter.data.TopicItem
import dagger.Reusable
import javax.inject.Inject

@Reusable
class TopicItemMapper @Inject constructor() : (List<TopicData>) -> (List<TopicItem>) {

    override fun invoke(topics: List<TopicData>): List<TopicItem> {
        return topics.mapIndexed { idx, topic ->
            topic.run {
                TopicItem(
                    id = id,
                    topicName = topicName,
                    numberOfMess = numberOfMess,
                    isEven = idx % 2 == 0
                )
            }
        }
    }
}
