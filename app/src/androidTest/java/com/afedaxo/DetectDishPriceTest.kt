package com.afedaxo


import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.afedaxo.domain.usecase.DetectDishPriceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test

class DetectDishPriceTest {

    val correctDishPrices = listOf(
        "2.90",
        "5.00",
        "164",
        "164"
    )

    @Test
    fun testPriceRecognition() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val assets = context.getAssets()
        val detectDishPriceUseCase = DetectDishPriceUseCase(context)

        var index = 0
        assets.list("cropped")?.forEach { croppedFn ->
            val bmp = BitmapFactory.decodeStream(assets.open("cropped/"+croppedFn))
            GlobalScope.launch(Dispatchers.Unconfined) {
                 detectDishPriceUseCase.invoke(bmp,
                    {
                        Assert.assertEquals(correctDishPrices[index++],
                        it.price.toString())
                    },
                    {
                        Assert.fail()
                    })
            }


        }
    }
}
