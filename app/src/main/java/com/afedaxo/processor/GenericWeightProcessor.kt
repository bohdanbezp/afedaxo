package com.afedaxo.processor

import com.afedaxo.util.GeneralUtils
import java.util.*


abstract class GenericWeightProcessor<T>(val itemList: List<T>) {

    private val weights: Map<T, Double> by lazy {
        processWeights(itemList)
    }

    private val random: Random by lazy {
        provideRandom()
    }

    private fun reverseWeights(inpt: Map<T, Double>) = inpt.mapValues { 1.0/it.value }

    private fun weightsSum(inpt: Map<T, Double>) = inpt.map { it.value }.sum()

    private fun convToProb(sum: Double, inpt: Map<T, Double>) = inpt.mapValues { it.value/sum }

    fun getWeightedRandom(): T? {
        var result: T? = null
        var bestValue = java.lang.Double.MAX_VALUE

        for (element in weights.keys) {
            val value = -Math.log(random.nextDouble()) / weights[element]!!

            if (value < bestValue) {
                bestValue = value
                result = element
            }
        }

        return result
    }

    private fun processWeights(itemList: List<T>): Map<T, Double> {
        val processedMap = assignWeights(itemList)
        return reverseWeights(
            convToProb(weightsSum(processedMap), processedMap))
    }

    protected abstract fun assignWeights(dishes: List<T>): Map<T, Double>

    fun getProbMap(): Map<T, Double> = convToProb(weightsSum(weights), weights)

    override fun toString(): String =
        getProbMap().toString()


    private fun provideRandom() = Random(GeneralUtils.bytesToLong(GeneralUtils.rand(
        (MAGIC+toString()).toByteArray(), 8
    )))

    companion object {
        const val MAGIC = "Afedaxo"
    }
}

