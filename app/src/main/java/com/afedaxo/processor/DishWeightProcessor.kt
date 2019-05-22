package com.afedaxo.processor

import com.afedaxo.model.room.Dish

class DishWeightProcessor(dishes: List<Dish>) : GenericWeightProcessor<Dish>(dishes) {

    override fun assignWeights(dishes: List<Dish>) = dishes.map { it to (regex.find(
        it.priceVal)!!.value.toDouble()) }.toMap()

    val regex = "[0-9]+[.,]?[0-9]?[0-9]?".toRegex()

}