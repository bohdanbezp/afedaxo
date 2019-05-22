package com.afedaxo


import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.afedaxo.interactor.OcrInteractor
import org.junit.Assert
import org.junit.Test

class ProcessorsInstrumentationTests {

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
        val ocrInteractor = OcrInteractor(InstrumentationRegistry.getInstrumentation().targetContext)

        var index = 0
        for (croppedFn in assets.list("cropped")) {
            val bmp = BitmapFactory.decodeStream(assets.open("cropped/"+croppedFn))
            val price = ocrInteractor.detectDishPrice(bmp)
            Assert.assertEquals(correctDishPrices[index++],
                price)
        }
    }
}
