package com.afedaxo.presentation.presenter

import android.graphics.Bitmap
import android.graphics.Matrix
import com.afedaxo.AfedaxoApp
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.model.room.Dish
import com.afedaxo.presentation.view.PhotoTakingView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.fotoapparat.result.PhotoResult
import kotlinx.coroutines.*
import javax.inject.Inject

@InjectViewState
class PhotoTakingPresenter : MvpPresenter<PhotoTakingView>() {

    init {
        AfedaxoApp.component.inject(this)
    }

    @Inject
    lateinit var filesRepository: FilesRepository

    @Inject
    lateinit var sessionsRepository: SessionsRepository

    var lastDish: Dish? = null

    fun onPhotoTaken(photoResult: PhotoResult): Deferred<String> {
        return GlobalScope.async {
            val bitmapPhoto = photoResult
                .toBitmap().await()

            val bitm = bitmapPhoto.bitmap

            val matrix = Matrix()

            matrix.postRotate(90f)

            val rotatedBitmap =
                Bitmap.createBitmap(bitm, 0, 0,
                    bitm.width, bitm.height, matrix, true)

            val filename = filesRepository.saveBitmapToFile(rotatedBitmap)
            return@async filename
        }

    }

    fun init(sessionId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            lastDish = sessionsRepository.getAllDishesForSessionId(sessionId)
                .maxBy { it.addedTimestamp}
            if (lastDish != null) {
                viewState.showReusePhotoButton()
            }
        }
    }

    fun onReuseBtnClick() {
        if (lastDish != null) {
            viewState.navigateToSelectRegionActivity(lastDish!!.fullFilename)
        }
    }

}
