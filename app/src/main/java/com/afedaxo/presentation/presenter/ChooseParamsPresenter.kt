package com.afedaxo.presentation.presenter

import com.afedaxo.AfedaxoApp
import com.afedaxo.model.repository.SessionsRepository
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.afedaxo.presentation.view.ChooseParamsView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class ChooseParamsPresenter : MvpPresenter<ChooseParamsView>() {

    init {
        AfedaxoApp.component.inject(this)
    }

    @Inject
    lateinit var sessionsRepository: SessionsRepository


    fun putMinMaxVals(sessionId: Int) {
        GlobalScope.launch {
            val min = 1
            val max =
                sessionsRepository.getAllDishesForSessionId(sessionId).count()-1
            viewState.setMinMaxPeople(min, max)
        }
    }

    fun onProceedBtnClick(peopleNum: Int, radioButtonID: Int) {
        viewState.navigateToResultActivity(peopleNum, radioButtonID)
    }

}
