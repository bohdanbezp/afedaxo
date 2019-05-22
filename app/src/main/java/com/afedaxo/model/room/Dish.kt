package com.afedaxo.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = QuessingSession::class,
        parentColumns = ["sessionId"],
        childColumns = ["sessionId"],
        onDelete = ForeignKey.CASCADE
    )], primaryKeys = ["sessionId", "uid"]
)
data class Dish(
    var uid: Int,
    @ColumnInfo(name = "full_filename") var fullFilename: String,
    @ColumnInfo(name = "price_val") var priceVal: String,
    @ColumnInfo(name = "cropped_filename") var croppedFilename: String,
    @ColumnInfo(name = "sessionId") var sessionId: Int,
    @ColumnInfo(name = "addedTimestamp") var addedTimestamp: Long
)