package com.homework.coursework.data.frameworks.database.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mes_to_user",
    indices = [Index(value = ["message_id", "user_id"])]
)
data class MessageToUserCrossRef(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "message_id")
    val messageId: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int
)
