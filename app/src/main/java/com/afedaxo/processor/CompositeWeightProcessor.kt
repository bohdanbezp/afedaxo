package com.afedaxo.processor

import com.afedaxo.data.room.DishEntity
import com.afedaxo.util.CombinationMaker

class CompositeWeightProcessor(itemList: List<List<Pair<Int, DishEntity>>>) :
    GenericWeightProcessor<List<Pair<Int, DishEntity>>>(itemList) {

    private val regex = "[0-9]+[.,]?[0-9]?[0-9]?".toRegex()

    override fun assignWeights(dishes: List<List<Pair<Int, DishEntity>>>): Map<List<Pair<Int, DishEntity>>, Double> =
        dishes.map { it to (it.sumByDouble { regex.find(it.second.priceVal)!!.value.toDouble() }) }.toMap()

    companion object {
        private fun <T, S> Collection<T>.cartesianProduct(other: Iterable<S>): List<Pair<T, S>> {
            return cartesianProduct(other, { first, second -> first to second })
        }

        private fun <T, S, V> Collection<T>.cartesianProduct(other: Iterable<S>, transformer: (first: T, second: S) -> V): List<V> {
            return this.flatMap { first -> other.map { second -> transformer.invoke(first, second) } }
        }

        fun preprocessValues(num: Int, dishes: List<DishEntity>): List<List<Pair<Int, DishEntity>>> {
            val peopleList= generateSequence(0) { it+1 }
                .take(num).toList()

            val pairList = peopleList.cartesianProduct(dishes)

            val combinationMaker = CombinationMaker<Pair<Int, DishEntity>>()

            val allCombinations
                    = combinationMaker.makeCombList(
                pairList.toTypedArray(), num
            )
            return allCombinations.filter { it.distinctBy { it.first }.size == num }
                .filter { it.distinctBy { it.second }.size == num }
        }
    }
}