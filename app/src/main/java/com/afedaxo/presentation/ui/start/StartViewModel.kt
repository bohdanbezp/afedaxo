package com.afedaxo.presentation.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afedaxo.helper.SingleLiveEvent
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.model.room.QuessingSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class StartViewModel(val sessionsRepository: SessionsRepository) : ViewModel() {

    private val _newSessionCreated = SingleLiveEvent<Int>()

    val newSessionCreated : LiveData<Int>
        get() = _newSessionCreated

    fun onStartProcessClick() {
        viewModelScope.launch(Dispatchers.Main) {
            sessionsRepository.deleteSession(sessionsRepository.retrieveLastSession())
            val quessingSession = QuessingSession(Math.abs(Random().nextInt()),
                System.currentTimeMillis())

            sessionsRepository.insert(quessingSession)
//            val quessingSession = sessionsRepository.retrieveLastSession()
//
            _newSessionCreated.value = quessingSession.sessionId
        }
    }
}