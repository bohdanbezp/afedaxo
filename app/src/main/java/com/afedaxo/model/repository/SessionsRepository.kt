package com.afedaxo.model.repository

import com.afedaxo.model.room.AppDatabase
import com.afedaxo.model.room.DishEntity
import com.afedaxo.model.room.QuessingSession
import com.afedaxo.model.room.SessionWithFiles

class SessionsRepository (val appDatabase: AppDatabase, val filesRepository: FilesRepository) {
    suspend fun retrieveLastSession(): QuessingSession? {
        return appDatabase.quessingSessionDao.getLastSession()
    }

    suspend fun deleteSession(quessingSession: QuessingSession?) {
        if (quessingSession != null) {
            deleteSession(
                appDatabase.quessingSessionDao.getSessionWithFilesBy(quessingSession.sessionId)
            )
        }
    }

    suspend fun deleteSession(sessionWithFiles: SessionWithFiles?) {
        if (sessionWithFiles != null) {
            appDatabase.quessingSessionDao.delete(sessionWithFiles.session)
            for (item in sessionWithFiles.files!!) {
                filesRepository.deleteIfExists(item.fullFilename)
                filesRepository.deleteIfExists(item.croppedFilename)
                appDatabase.quessingSessionDao.delete(item)
            }
        }
    }

    suspend fun insert(quessingSession: QuessingSession) {
        appDatabase.quessingSessionDao.insert(quessingSession)
    }

    suspend fun insert(dish: DishEntity) {
        appDatabase.quessingSessionDao.insert(dish)
    }

    suspend fun getAllDishesForSessionId(sessionId: Int): List<DishEntity> = appDatabase.quessingSessionDao.getAllDishesById(sessionId)
}