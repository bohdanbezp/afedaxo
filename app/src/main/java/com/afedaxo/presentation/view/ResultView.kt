package com.afedaxo.presentation.view

import android.graphics.Bitmap
import com.arellomobile.mvp.MvpView

interface ResultView : MvpView {
    fun showResult(dishesImgs: List<Pair<Int, Bitmap>>)

}
