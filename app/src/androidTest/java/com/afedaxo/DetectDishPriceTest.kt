package com.afedaxo


import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.afedaxo.domain.usecase.DetectDishPriceUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import timber.log.Timber


class DetectDishPriceTest {

    val correctDishPrices = listOf(
        "2.90",
        "5.00",
        "164",
        "164"
    )

    @Test
    fun testPriceRecognition() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val assets = InstrumentationRegistry.getInstrumentation().context.getAssets()
        val detectDishPriceUseCase = DetectDishPriceUseCase(context)

        var index = 0
        assets.list("cropped")?.forEach { croppedFn ->
            val bmp = BitmapFactory.decodeStream(assets.open("cropped/"+croppedFn))
            runBlocking {
                detectDishPriceUseCase.invoke(bmp,
                    {
                        Timber.d("Detected price: " + it.price)
                        Assert.assertEquals(
                            correctDishPrices[index++],
                            it.price.toString()
                        )
                    },
                    {
                        it.printStackTrace()
                        Assert.fail(it.message)
                    })
            }

        }
    }
}
