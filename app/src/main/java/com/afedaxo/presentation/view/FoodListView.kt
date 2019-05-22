package com.afedaxo.presentation.view

import com.arellomobile.mvp.MvpView

interface FoodListView : MvpView {
    fun navigateToChooseParamsActivity()

    fun enableProcessButton()
}
