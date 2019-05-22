package com.afedaxo.presentation.presenter

import android.graphics.Bitmap
import com.afedaxo.AfedaxoApp
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.presentation.view.FoodListView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class FoodListPresenter : MvpPresenter<FoodListView>() {

    init {
        AfedaxoApp.component.inject(this)
    }

    @Inject
    lateinit var filesRepository: FilesRepository

    @Inject
    lateinit var sessionsRepository: SessionsRepository

    fun retrieveDishesBitmaps(): List<Bitmap>? {
        val dishesBitmaps = sessionsRepository.retrieveLastSession()?.sessionId?.let {
            sessionsRepository.getAllDishesForSessionId(it).map {
                filesRepository.getBitmapOfFile(
                    it.croppedFilename)
            }
        }

        if (dishesBitmaps != null) {
            if (dishesBitmaps.size >= 2) {
                viewState.enableProcessButton()
            }
        }

        return dishesBitmaps
    }

    fun onProcessClick() {
        viewState.navigateToChooseParamsActivity()
    }

}
