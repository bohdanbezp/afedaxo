package com.afedaxo.presentation.ui.selectregion

import com.afedaxo.data.room.DishEntity

data class DishDetectionResult(val status: Status, val detectedDish: DishEntity?) {
    enum class Status {
        SUCCESS, FAILURE
    }
}