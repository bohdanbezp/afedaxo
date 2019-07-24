package com.afedaxo.presentation.ui.chooseparams

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afedaxo.data.repository.SessionsRepository
import com.afedaxo.helper.SingleLiveEvent
import kotlinx.coroutines.launch

class ChooseParamsViewModel(val sessionsRepository: SessionsRepository) : ViewModel() {

    private val _calculatedMinMaxValues = SingleLiveEvent<Pair<Int, Int>>()

    val calculatedMinMaxValues : LiveData<Pair<Int, Int>>
        get() = _calculatedMinMaxValues

    private val _proceedClicked = SingleLiveEvent<Pair<Int, Int>>()

    val proceedClicked : LiveData<Pair<Int, Int>>
        get() = _proceedClicked

    fun putMinMaxVals(sessionId: Int) {
        viewModelScope.launch {
            val min = 1
            val max =
                sessionsRepository.getAllDishesForSessionId(sessionId).count()-1
            _calculatedMinMaxValues.value = Pair(min, max)
        }
    }

    fun onProceedBtnClick(peopleNum: Int, radioButtonID: Int) {
        _proceedClicked.value = Pair(peopleNum, radioButtonID)
    }
}