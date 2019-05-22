package com.afedaxo.model.repository

import com.afedaxo.model.room.AppDatabase
import com.afedaxo.model.room.Dish
import com.afedaxo.model.room.QuessingSession
import com.afedaxo.model.room.SessionWithFiles

class SessionsRepository (val appDatabase: AppDatabase, val filesRepository: FilesRepository) {
    fun retrieveLastSession(): QuessingSession? {
        return appDatabase.quessingSessionDao.getLastSession()
    }

    fun deleteSession(quessingSession: QuessingSession?) {
        if (quessingSession != null) {
            deleteSession(
                appDatabase.quessingSessionDao.getSessionWithFilesBy(quessingSession.sessionId)
            )
        }
    }

    fun deleteSession(sessionWithFiles: SessionWithFiles?) {
        if (sessionWithFiles != null) {
            appDatabase.quessingSessionDao.delete(sessionWithFiles.session)
            for (item in sessionWithFiles.files!!) {
                filesRepository.deleteIfExists(item.fullFilename)
                filesRepository.deleteIfExists(item.croppedFilename)
                appDatabase.quessingSessionDao.delete(item)
            }
        }
    }

    fun insert(quessingSession: QuessingSession) {
        appDatabase.quessingSessionDao.insert(quessingSession)
    }

    fun insert(dish: Dish) {
        appDatabase.quessingSessionDao.insert(dish)
    }

    fun getAllDishesForSessionId(sessionId: Int): List<Dish> = appDatabase.quessingSessionDao.getAllDishesById(sessionId)
}