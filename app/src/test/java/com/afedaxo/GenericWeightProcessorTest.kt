package com.afedaxo

import com.afedaxo.data.room.DishEntity
import com.afedaxo.processor.CompositeWeightProcessor
import com.afedaxo.processor.DishWeightProcessor
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class GenericWeightProcessorTest {

    val dish1 = DishEntity(Math.abs(Random().nextInt()),
        "", "5.50", "", 0,
        System.currentTimeMillis())
    val dish2 = DishEntity(Math.abs(Random().nextInt()),
        "", "7.50", "", 0,
        System.currentTimeMillis())
    val dish3 = DishEntity(Math.abs(Random().nextInt()),
        "", "8.00", "", 0,
        System.currentTimeMillis())

    val inptList = listOf(dish1, dish2, dish3)

    val roundingFormatter by lazy {
        val df = DecimalFormat("#.##")
        df.setRoundingMode(RoundingMode.CEILING)
        df
    }

    @Test
    fun weightPriceMattersMuchTest() {
        var d1 = 0
        var d2 = 0
        var d3 = 0

        val weightProcessor = DishWeightProcessor(inptList)
        for (a in 0..100000) {
            val resultingDish = weightProcessor.getWeightedRandom()
            if (resultingDish == dish1) {
                d1 = d1+1
            }
            else if (resultingDish == dish2) {
                d2 = d2+1
            }
            else if (resultingDish == dish3) {
                d3 = d3+1
            }
        }

        val actualPercentageD1 = d1.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble())
        val actualPercentageD2 = d2.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble())
        val actualPercentageD3 = d3.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble())

        val probMap = weightProcessor.getProbMap()

        with(probMap) {
            val percentageD1 = getValue(dish1)
            val percentageD2 = getValue(dish2)
            val percentageD3 = getValue(dish3)

            assertEquals(roundingFormatter.format(percentageD1),
                roundingFormatter.format(actualPercentageD1))
            assertEquals (roundingFormatter.format(percentageD2),
                roundingFormatter.format(actualPercentageD2))
            assertEquals(roundingFormatter.format(percentageD3),
                roundingFormatter.format(actualPercentageD3))

        }

    }

    @Test
    fun weightMattersLittleTest() {
        var d1 = 0
        var d2 = 0
        var d3 = 0

        val weightProcessor = DishWeightProcessor(inptList)
        for (a in 0..100000) {
            val fullyRandom = inptList.random()
            val weightedRandom = weightProcessor.getWeightedRandom()
            val resultingDish = listOf(fullyRandom, weightedRandom).random()
            if (resultingDish == dish1) {
                d1 = d1+1
            }
            else if (resultingDish == dish2) {
                d2 = d2+1
            }
            else if (resultingDish == dish3) {
                d3 = d3+1
            }
        }

        val actualPercentageD1 = d1.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble())
        val actualPercentageD2 = d2.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble())
        val actualPercentageD3 = d3.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble())

        val probMap = weightProcessor.getProbMap()

        with(probMap) {
            val percentageD1 = getValue(dish1)
            val percentageD2 = getValue(dish2)
            val percentageD3 = getValue(dish3)

            val biggestOf = listOf(percentageD1, percentageD2, percentageD3).max()

            assertTrue(roundingFormatter.format(biggestOf).toDouble() >
                roundingFormatter.format(actualPercentageD1).toDouble())
            assertTrue (roundingFormatter.format(biggestOf).toDouble() >
                roundingFormatter.format(actualPercentageD2).toDouble())
            assertTrue(roundingFormatter.format(biggestOf).toDouble() >
                roundingFormatter.format(actualPercentageD3).toDouble())

        }
    }

    @Test
    fun testCompositeWeightProcessor() {
        val preproc = CompositeWeightProcessor.preprocessValues(2, inptList)
        val compositeWeightProcessor = CompositeWeightProcessor(preproc)
        val d = MutableList(preproc.size) { 0 }

        for (a in 0..1000000) {
            val resultingDish = compositeWeightProcessor.getWeightedRandom()
            d[preproc.indexOf(resultingDish)]++
        }

        val probMap = compositeWeightProcessor.getProbMap()

        with(probMap) {

            val sum = d.sum()

            d.forEachIndexed { index, element ->
                val percentage = roundingFormatter.format(getValue(preproc[index]))
                val actualPercentage = roundingFormatter.format(element.toDouble()/sum.toDouble())
                assertEquals(actualPercentage, percentage)
            }

        }
    }
}