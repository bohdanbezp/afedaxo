package com.afedaxo.presentation.view

import android.net.Uri
import com.afedaxo.model.room.Dish
import com.arellomobile.mvp.MvpView

interface SelectRegionView : MvpView {
    fun initViewWithUri(fromFile: Uri)
    fun navigateToFoodListActivity()
    fun showDishNotDetectedToast()
    fun showRetakePhotoButton()
    fun dishDetected(dish: Dish)
    fun pressBack()

}
