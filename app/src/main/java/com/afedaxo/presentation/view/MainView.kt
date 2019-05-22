package com.afedaxo.presentation.view

import com.arellomobile.mvp.MvpView

interface MainView : MvpView {
    fun navigateToFoodListActivity(sessionId: Int);
    fun navigateToChooseParamsActivity(sessionId: Int);
}
