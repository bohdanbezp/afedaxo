package com.afedaxo.presentation.ui.result

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afedaxo.domain.model.CalculationParams
import com.afedaxo.domain.usecase.CalcDishForPeopleUseCase
import com.afedaxo.helper.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(val calcDishForPeopleUseCase: CalcDishForPeopleUseCase) : ViewModel() {

    private val _resultAvailable = SingleLiveEvent<List<Pair<Int, Bitmap>>>()

    val resultAvailable : LiveData<List<Pair<Int, Bitmap>>>
        get() = _resultAvailable

    private val _newSessionClick = SingleLiveEvent<Any>()

    val newSessionClick : LiveData<Any>
        get() = _newSessionClick

    fun think(sessionId: Int, priceMode: Int, numPeople: Int) {
        viewModelScope.launch {
           calcDishForPeopleUseCase.invoke(CalculationParams(sessionId, numPeople, priceMode),
               Dispatchers.Default, {
                   _resultAvailable.value = it
               },
               {

               })

        }
    }

    fun onNewSession() {
        _newSessionClick.call()
    }
}