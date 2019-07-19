package com.afedaxo.presentation.ui.foodlist

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.afedaxo.helper.SingleLiveEvent
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.repository.SessionsRepository

class FoodListViewModel(val filesRepository: FilesRepository,
                        val sessionsRepository: SessionsRepository) : ViewModel() {

    private val _onProcessClick = SingleLiveEvent<Any>()

    val onProcessClick : LiveData<Any>
        get() = _onProcessClick

    private val _onAddPhotoClick = SingleLiveEvent<Any>()

    val onAddPhotoClick : LiveData<Any>
        get() = _onAddPhotoClick

    val dishesBitmaps: LiveData<List<Bitmap>> = liveData {
        val data = getDishesBitmaps()
        data?.let {
            emit(data as List<Bitmap>)
        }
    }

    private suspend fun getDishesBitmaps(): List<Bitmap>? {
        val dishesBitmaps = sessionsRepository.retrieveLastSession()?.sessionId?.let {
            sessionsRepository.getAllDishesForSessionId(it).map {
                filesRepository.getBitmapOfFile(
                    it.croppedFilename)
            }
        }

        return dishesBitmaps
    }

    fun onProcessClick() {
        _onProcessClick.call()
    }

    fun onAddPhotoClick() {
        _onProcessClick.call()
    }

}