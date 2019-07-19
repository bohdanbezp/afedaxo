package com.afedaxo.processor

import com.afedaxo.model.room.DishEntity

class DishWeightProcessor(dishes: List<DishEntity>) : GenericWeightProcessor<DishEntity>(dishes) {

    override fun assignWeights(dishes: List<DishEntity>) = dishes.map { it to (regex.find(
        it.priceVal)!!.value.toDouble()) }.toMap()

    val regex = "[0-9]+[.,]?[0-9]?[0-9]?".toRegex()

}