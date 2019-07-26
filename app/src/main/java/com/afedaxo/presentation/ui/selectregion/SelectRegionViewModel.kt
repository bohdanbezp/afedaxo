package com.afedaxo.presentation.ui.selectregion

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afedaxo.data.repository.FilesRepository
import com.afedaxo.data.repository.SessionsRepository
import com.afedaxo.data.room.DishEntity
import com.afedaxo.domain.usecase.DetectDishPriceUseCase
import com.afedaxo.helper.SingleLiveEvent
import kotlinx.coroutines.launch
import java.util.*

class SelectRegionViewModel(val filesRepository: FilesRepository,
                            val sessionsRepository: SessionsRepository,
                            val detectDishPriceUseCase: DetectDishPriceUseCase
                            ) : ViewModel() {

    val dishDetectionResult = MutableLiveData<DishDetectionResult>()

    private val _photoInit = SingleLiveEvent<Uri>()

    val photoInit : LiveData<Uri>
        get() = _photoInit

    fun initCropView(filename: String?) {
        checkNotNull(filename)

        val fullfile = filesRepository.getOutputJPEGFile(filename)
        if (fullfile.exists()) {
            _photoInit.value = Uri.fromFile(fullfile)
        }
        else {
            throw RuntimeException("Fullsize file doesn't exist")
        }
    }

    fun onSelectRegion(croppedImage: Bitmap, fullFilename: String,
                       sessionId: Int) {
        viewModelScope.launch {
            detectDishPriceUseCase.invoke(croppedImage,
                {
                    viewModelScope.launch {
                        val filenameCropped = filesRepository.saveBitmapToFile(croppedImage)

                        val dish = DishEntity(
                            Math.abs(Random().nextInt()),
                            fullFilename, it.price.toPlainString(), filenameCropped, sessionId,
                            System.currentTimeMillis()
                        )
                        dishDetectionResult.postValue(
                            DishDetectionResult(
                                DishDetectionResult.Status.SUCCESS,
                                dish
                            )
                        )
                    }
                },
                {
                    dishDetectionResult.postValue(
                        DishDetectionResult(
                            DishDetectionResult.Status.FAILURE,
                            null
                        )
                    )
                })
        }
    }


    suspend fun dishConfirmed(dish: DishEntity) {
        sessionsRepository.insert(dish)
    }

    suspend fun disposeDish(dish: DishEntity) {
        filesRepository.deleteIfExists(dish.croppedFilename)
    }

    suspend fun onRetakePhoto(string: String) {
        filesRepository.deleteIfExists(string)
    }
}