package com.afedaxo.interactor

import android.content.Context
import android.graphics.Bitmap
import com.afedaxo.util.GeneralUtils
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import java.lang.StringBuilder
import java.util.ArrayList

class OcrInteractor (val context: Context) {
    val textRecognizer = TextRecognizer.Builder(context).build()

    fun detectDishPrice(bitmap: Bitmap) : String? {
        val imageFrame = Frame.Builder()
            .setBitmap(bitmap)
            .build()

        val textBlocks = textRecognizer.detect(imageFrame)

        val textConcat = StringBuilder()

        val regex = "[0-9]+[.,]?[0-9]?[0-9]?".toRegex()

        val textblocklist = GeneralUtils.asList(textBlocks)

        val matchedVals = textblocklist
            .filter { regex.findAll(it.value).count() > 0 }

        textblocklist.forEach { textConcat.append(it.value) }

        val maximumResult = matchedVals.maxBy { it.cornerPoints.get(1).x }

        if (maximumResult != null) {
            var currencyName: String? = null

            if (textConcat.contains("€")
                || textConcat.contains("EUR [0-9]".toRegex())) {
                currencyName = "EUR "
            }
            else if (textConcat.contains("$")
                || textConcat.contains("USD [0-9]".toRegex())) {
                currencyName = "USD "
            }
            else if (textConcat.contains("¥")
                || textConcat.contains("JPY [0-9]".toRegex())) {
                currencyName = "JPY "
            }
            else if (textConcat.contains("£")) {
                currencyName = "£"
            }

            if (currencyName != null) {
                return currencyName + maximumResult.value.replace(',', '.');
            }
            else {
                return maximumResult.value.replace(',', '.')
            }
        }
        return null;
    }

    fun getTextualComponents(bitmapList: List<Bitmap>): List<String> {
        val resultList = ArrayList<String>()
        for (btmp in bitmapList) {
            val imageFrame = Frame.Builder()
                .setBitmap(btmp)
                .build()

            val textBlocks = textRecognizer.detect(imageFrame)
            val textblocklist = GeneralUtils.asList(textBlocks)

            resultList.addAll(textblocklist.flatMap { it.components }
                .flatMap { it.components }.map { it.value }.toList())
        }
        return resultList
    }
}