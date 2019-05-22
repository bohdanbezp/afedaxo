package com.afedaxo.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuessingSession(
    @PrimaryKey var sessionId: Int,
    @ColumnInfo(name = "session_timestamp") var sessionTimestamp: Long
)