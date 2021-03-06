package com.homework.coursework.data.frameworks.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.TEXT

@Entity(
    tableName = "topic_table",
    foreignKeys = [ForeignKey(
        entity = StreamEntity::class,
        parentColumns = arrayOf("stream_back_id"),
        childColumns = arrayOf("stream_id"),
        onDelete = CASCADE
    )],
    indices = [Index(value = ["stream_id"])]
)
data class TopicEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "back_id")
    val backId: Long,
    @ColumnInfo(name = "topic_name", typeAffinity = TEXT)
    val topicName: String,
    @ColumnInfo(name = "stream_id")
    val streamId: Long,
    @ColumnInfo(name = "last_message")
    val numberOfMessage: Int
)
