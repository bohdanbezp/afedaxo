package com.afedaxo.presentation.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afedaxo.domain.usecase.CreateSessionAndSaveUseCase
import com.afedaxo.helper.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StartViewModel(val createSessionAndSaveUseCase: CreateSessionAndSaveUseCase) : ViewModel() {

    private val _newSessionCreated = SingleLiveEvent<Int>()

    val newSessionCreated : LiveData<Int>
        get() = _newSessionCreated

    fun onStartProcessClick() {
        viewModelScope.launch(Dispatchers.Main) {
            createSessionAndSaveUseCase.invoke(Any(),
                {
                    _newSessionCreated.value = it.sessionId
                },
                {})
        }
    }
}