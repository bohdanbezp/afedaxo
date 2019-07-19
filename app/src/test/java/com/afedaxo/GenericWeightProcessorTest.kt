package com.afedaxo

import com.afedaxo.model.room.DishEntity
import com.afedaxo.processor.CompositeWeightProcessor
import com.afedaxo.processor.DishWeightProcessor
import org.junit.Test
import java.util.*

class GenericWeightProcessorTest {

    @Test
    fun weightTest() {
        var d1: Int = 0
        var d2: Int = 0
        var d3: Int = 0
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

        val weightProcessor = DishWeightProcessor(inptList)

        println(weightProcessor.toString())
            for (a in 0..10000) {
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
            println(d1.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble()))
            println(d2.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble()))
            println(d3.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble()))
            println()

        d1 = 0
        d2 = 0
        d3 = 0

        for (a in 0..10000) {
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

        println(d1.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble()))
        println(d2.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble()))
        println(d3.toDouble()/(d1.toDouble()+d2.toDouble()+d3.toDouble()))


        val preproc= CompositeWeightProcessor.preprocessValues(2, inptList)
        println(preproc)
        println(preproc!!.size)

        val compositeWeightProcessor = CompositeWeightProcessor(preproc)
        val resultingDish = compositeWeightProcessor.getWeightedRandom()
        println(compositeWeightProcessor.toString())

    }
}