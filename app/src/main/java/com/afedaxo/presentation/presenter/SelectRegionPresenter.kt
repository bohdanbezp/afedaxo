package com.afedaxo.presentation.presenter

import android.graphics.Bitmap
import android.net.Uri
import com.afedaxo.AfedaxoApp
import com.afedaxo.interactor.OcrInteractor
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.model.room.Dish
import com.afedaxo.presentation.view.SelectRegionView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@InjectViewState
class SelectRegionPresenter : MvpPresenter<SelectRegionView>() {

    init {
        AfedaxoApp.component.inject(this)
    }

    @Inject
    lateinit var filesRepository: FilesRepository

    @Inject
    lateinit var sessionsRepository: SessionsRepository

    @Inject
    lateinit var ocrInteractor: OcrInteractor

    fun initCropView(filename: String?) {
        checkNotNull(filename)

        val fullfile = filesRepository.getOutputJPEGFile(filename)
        if (fullfile.exists()) {
            viewState.initViewWithUri(Uri.fromFile(fullfile))
        }
        else {
            throw RuntimeException("Fullsize file doesn't exist")
        }
    }

    fun onSelectRegion(croppedImage: Bitmap, fullFilename: String,
                       sessionId: Int) {
        GlobalScope.launch {
            val priceVal = ocrInteractor.detectDishPrice(croppedImage)
            if (priceVal != null) {
                val filenameCropped = filesRepository.saveBitmapToFile(croppedImage)

                val dish = Dish(Math.abs(Random().nextInt()),
                    fullFilename, priceVal, filenameCropped, sessionId,
                    System.currentTimeMillis())

                viewState.dishDetected(dish)
            }
            else {
                viewState.showDishNotDetectedToast()
                viewState.showRetakePhotoButton()
            }
        }

    }

    fun dishConfirmed(dish: Dish) {
        GlobalScope.launch(IO) {
            sessionsRepository.insert(dish)
            viewState.navigateToFoodListActivity()
        }
    }

    fun disposeDish(dish: Dish) {
        GlobalScope.launch(IO) {
            filesRepository.deleteIfExists(dish.croppedFilename)
        }
    }

    fun onRetakePhoto(string: String?) {
        GlobalScope.launch(IO) {
            filesRepository.deleteIfExists(string)
        }
        viewState.pressBack()
    }

}
