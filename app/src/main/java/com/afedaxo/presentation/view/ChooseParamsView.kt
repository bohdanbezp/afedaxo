package com.afedaxo.presentation.view

import com.arellomobile.mvp.MvpView

interface ChooseParamsView : MvpView {
    fun navigateToResultActivity(peopleNum: Int, moneyWeightId: Int)
    fun setMinMaxPeople(min: Int, max: Int)
}
