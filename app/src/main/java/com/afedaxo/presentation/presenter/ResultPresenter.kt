package com.afedaxo.presentation.presenter

import com.afedaxo.AfedaxoApp
import com.afedaxo.interactor.OcrInteractor
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.presentation.view.ResultView
import com.afedaxo.processor.CompositeWeightProcessor
import com.afedaxo.processor.DishWeightProcessor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class ResultPresenter : MvpPresenter<ResultView>() {
    init {
        AfedaxoApp.component.inject(this)
    }

    @Inject
    lateinit var filesRepository: FilesRepository

    @Inject
    lateinit var sessionsRepository: SessionsRepository

    fun think(sessionId: Int, priceMode: Int, numPeople: Int) {
        GlobalScope.launch {
            val dishes = sessionsRepository.getAllDishesForSessionId(sessionId)

            val preproc= CompositeWeightProcessor.preprocessValues(numPeople, dishes)

            val compositeWeightProcessor = CompositeWeightProcessor(preproc)

            if (priceMode == 0) {
                val resultingDish = compositeWeightProcessor.getWeightedRandom()
                if (resultingDish != null) {
                    viewState.showResult(resultingDish.map {
                        Pair(it.first, filesRepository.getBitmapOfFile(it.second.croppedFilename))
                    })
                }
            }
            else if (priceMode == 1) {
                val fullyRandom = preproc.random()
                val weightedRandom = compositeWeightProcessor.getWeightedRandom()
                val resultingDish = listOf(fullyRandom, weightedRandom).random()
                if (resultingDish != null) {
                    viewState.showResult(resultingDish.map {
                        Pair(it.first, filesRepository.getBitmapOfFile(it.second.croppedFilename))
                    })
                }
            }
            else {
                val fullyRandom = preproc.random()
                viewState.showResult(fullyRandom.map {
                    Pair(it.first, filesRepository.getBitmapOfFile(it.second.croppedFilename))
                })
            }
        }
    }
}
