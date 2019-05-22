package com.afedaxo.model.room

import androidx.room.Embedded
import androidx.room.Relation

data class SessionWithFiles(
    @Embedded var session: QuessingSession,
    @Relation(parentColumn = "sessionId", entity = Dish::class, entityColumn = "sessionId")
    var files: List<Dish>?
)