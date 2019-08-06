package com.afedaxo.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import com.afedaxo.domain.Either
import com.afedaxo.domain.Failure
import com.afedaxo.domain.Success
import com.afedaxo.domain.model.Dish
import com.afedaxo.util.GeneralUtils
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import java.math.BigDecimal

class DetectDishPriceUseCase(val context: Context) : UseCase<Dish, Bitmap>() {
    val textRecognizer by lazy {
        TextRecognizer.Builder(context).build()
    }

    override suspend fun run(param: Bitmap): Either<Exception, Dish> {
        return try {
            val imageFrame = Frame.Builder()
                .setBitmap(param)
                .build()

            val textBlocks = textRecognizer.detect(imageFrame)

            val textConcat = StringBuilder()

            val regex = "[0-9]+[.,]?[0-9]?[0-9]?".toRegex()

            val textblocklist = GeneralUtils.asList(textBlocks)

            val matchedVals = textblocklist
                .filter { regex.findAll(it.value).count() > 0 }

            textblocklist.forEach { textConcat.append(it.value) }

            val maxVal = matchedVals.maxBy { it.cornerPoints.get(1).x }?.value
            val maximumResult = maxVal?.let { regex.find(it) }?.groupValues?.get(0)

            if (maximumResult != null) {
                var currencyName: String? = null

                if (textConcat.contains("€")
                    || textConcat.contains("EUR [0-9]".toRegex())
                ) {
                    currencyName = "EUR"
                } else if (textConcat.contains("$")
                    || textConcat.contains("USD [0-9]".toRegex())
                ) {
                    currencyName = "USD"
                } else if (textConcat.contains("¥")
                    || textConcat.contains("JPY [0-9]".toRegex())
                ) {
                    currencyName = "JPY"
                } else if (textConcat.contains("£")) {
                    currencyName = "£"
                }

                if (currencyName != null) {
                    Success(Dish("", BigDecimal(maximumResult.replace(',', '.')),
                        currencyName))
                } else {
                    Success(Dish("", BigDecimal(maximumResult.replace(',', '.')),
                        ""))
                }

            }
            else {
                Failure(java.lang.Exception("No price found!"))
            }
        } catch (e: Exception) {
            Failure(e)
        }
    }
}