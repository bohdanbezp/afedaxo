package com.afedaxo.presentation.ui.photo

import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.View.*
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afedaxo.BR
import com.afedaxo.data.repository.FilesRepository
import com.afedaxo.data.repository.SessionsRepository
import com.afedaxo.data.room.DishEntity
import com.afedaxo.helper.SingleLiveEvent
import io.fotoapparat.result.PhotoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoTakingViewModel(val filesRepository: FilesRepository,
                           val sessionsRepository: SessionsRepository) : ViewModel(), Observable {

    var onPropertyChangedCallbackList = ArrayList<Observable.OnPropertyChangedCallback>()

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        onPropertyChangedCallbackList.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        if (callback != null) {
            onPropertyChangedCallbackList.add(callback)
        }
    }

    private val _photoProcessed = SingleLiveEvent<String>()

    val photoProcessed : LiveData<String>
        get() = _photoProcessed

    var lastDish: DishEntity? = null

    var processingState: Boolean = false

    suspend fun getAndProcessBitmap(photoResult: PhotoResult) : Bitmap = withContext(Dispatchers.Default){
        val bitmapPhoto = photoResult
            .toBitmap().await()

        val bitm = bitmapPhoto.bitmap

        val matrix = Matrix()

        matrix.postRotate(90f)

        val rotatedBitmap =
            Bitmap.createBitmap(bitm, 0, 0,
                bitm.width, bitm.height, matrix, true)

        rotatedBitmap
    }

    fun onPhotoTaken(photoResult: PhotoResult) {
        processingState = true
        onPropertyChangedCallbackList.forEach {
            it.onPropertyChanged(this, BR.progressBarVisibility)
            it.onPropertyChanged(this, BR.photoOverlayVisibility)
        }
         viewModelScope.launch(Dispatchers.Main) {
            val rotatedBitmap =
                getAndProcessBitmap(photoResult)

            val filename = filesRepository.saveBitmapToFile(rotatedBitmap)
             processingState = false
            _photoProcessed.value = filename
        }

    }

    fun init(sessionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            lastDish = sessionsRepository.getAllDishesForSessionId(sessionId)
                .maxBy { it.addedTimestamp}
            if (lastDish != null) {
                onPropertyChangedCallbackList.forEach {
                    it.onPropertyChanged(this@PhotoTakingViewModel, BR.reuseBtnVisibility)
                }
            }
        }
    }

    fun onReuseBtnClick() {
        _photoProcessed.value = lastDish?.fullFilename
    }

    @Bindable
    fun getReuseBtnVisibility(): Int {
        return if (lastDish != null) VISIBLE else INVISIBLE
    }

    @Bindable
    fun getPhotoOverlayVisibility(): Int {
        return if (!processingState) VISIBLE else GONE
    }

    @Bindable
    fun getProgressBarVisibility(): Int {
        return if (processingState) VISIBLE else GONE
    }
}