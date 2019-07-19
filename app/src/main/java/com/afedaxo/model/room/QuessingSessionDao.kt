package com.afedaxo.model.room

import androidx.room.*


@Dao
interface QuessingSessionDao {

    @Query("SELECT * FROM QuessingSession")
    suspend fun getAllQuessingSessions(): List<QuessingSession>

    @Query("SELECT * FROM DishEntity WHERE sessionId = :sessionId")
    suspend fun getAllDishesById(sessionId: Int): List<DishEntity>

    @Query("SELECT * FROM QuessingSession ORDER BY session_timestamp desc LIMIT 1")
    suspend fun getLastSession(): QuessingSession

    @Insert
    suspend fun insert(session: QuessingSession)

    @Insert
    suspend fun insert(dish: DishEntity)

    @Delete
    suspend fun delete(session: QuessingSession)

    @Delete
    suspend fun delete(dish: DishEntity)

    @Query("SELECT f.uid, f.full_filename, f.cropped_filename, f.sessionId, q.session_timestamp FROM" +
            " DishEntity f LEFT JOIN QuessingSession q ON f.sessionId = q.sessionId WHERE f.sessionId = :sessionId")
    suspend fun getSessionWithFilesBy(sessionId: Int): SessionWithFiles

    @Transaction
    suspend fun insert(sessionWithFiles: SessionWithFiles) {
        insert(sessionWithFiles.session)
        for (file in sessionWithFiles.files!!) {
            insert(file)
        }
    }
}