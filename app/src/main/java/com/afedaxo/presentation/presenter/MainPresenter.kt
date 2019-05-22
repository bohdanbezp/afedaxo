package com.afedaxo.presentation.presenter

import com.afedaxo.AfedaxoApp
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.model.room.QuessingSession
import com.afedaxo.presentation.view.MainView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    init {
        AfedaxoApp.component.inject(this)
    }

    @Inject
    lateinit var sessionsRepository: SessionsRepository

    fun onStartProcessClick() {
        GlobalScope.launch {
            sessionsRepository.deleteSession(sessionsRepository.retrieveLastSession())
            val quessingSession = QuessingSession(Math.abs(Random().nextInt()),
                System.currentTimeMillis())

            sessionsRepository.insert(quessingSession)
//            val quessingSession = sessionsRepository.retrieveLastSession()
//
            if (quessingSession != null) {
                viewState.navigateToFoodListActivity(quessingSession.sessionId)
            }
        }
    }


}
