package com.afedaxo.presentation.ui.foodlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.afedaxo.data.repository.FilesRepository
import com.afedaxo.data.repository.SessionsRepository
import com.afedaxo.helper.SingleLiveEvent

class FoodListViewModel(val filesRepository: FilesRepository,
                        val sessionsRepository: SessionsRepository) : ViewModel() {

    private val _onProcessClick = SingleLiveEvent<Any>()

    val onProcessClick : LiveData<Any>
        get() = _onProcessClick

    private val _onAddPhotoClick = SingleLiveEvent<Any>()

    val onAddPhotoClick : LiveData<Any>
        get() = _onAddPhotoClick

    val dishesBitmaps =
        Transformations.map(sessionsRepository.getAllDishesForLastSessionLiveData()) { dishEntities ->
            dishEntities.map{filesRepository.getBitmapOfFile(it.croppedFilename)}
        }

    fun onProcessClick() {
        _onProcessClick.call()
    }

    fun onAddPhotoClick() {
        _onAddPhotoClick.call()
    }

}