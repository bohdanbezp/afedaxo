package com.afedaxo.domain.usecase

import com.afedaxo.data.repository.SessionsRepository
import com.afedaxo.data.room.QuessingSession
import com.afedaxo.domain.Either
import com.afedaxo.domain.Failure
import com.afedaxo.domain.Success
import java.util.*

class CreateSessionAndSaveUseCase(val sessionsRepository: SessionsRepository):
    UseCase<QuessingSession, Any>() {

    override suspend fun run(params: Any): Either<Exception, QuessingSession> {
        return try {
            sessionsRepository.deleteSession(sessionsRepository.retrieveLastSession())
            val quessingSession = QuessingSession(Math.abs(Random().nextInt()),
                System.currentTimeMillis())

            sessionsRepository.insert(quessingSession)

            Success(quessingSession)
        }
        catch (e: java.lang.Exception) {
            Failure(e)
        }
    }
}