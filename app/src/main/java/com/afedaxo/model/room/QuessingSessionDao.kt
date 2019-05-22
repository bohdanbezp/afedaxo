package com.afedaxo.model.room

import androidx.room.*


@Dao
interface QuessingSessionDao {

    @Query("SELECT * FROM QuessingSession")
    fun getAllQuessingSessions(): List<QuessingSession>

    @Query("SELECT * FROM Dish WHERE sessionId = :sessionId")
    fun getAllDishesById(sessionId: Int): List<Dish>

    @Query("SELECT * FROM QuessingSession ORDER BY session_timestamp desc LIMIT 1")
    fun getLastSession(): QuessingSession

    @Insert
    fun insert(session: QuessingSession)

    @Insert
    fun insert(dish: Dish)

    @Delete
    fun delete(session: QuessingSession)

    @Delete
    fun delete(dish: Dish)

    @Query("SELECT f.uid, f.full_filename, f.cropped_filename, f.sessionId, q.session_timestamp FROM" +
            " Dish f LEFT JOIN QuessingSession q ON f.sessionId = q.sessionId WHERE f.sessionId = :sessionId")
    fun getSessionWithFilesBy(sessionId: Int): SessionWithFiles

    @Transaction
    fun insert(sessionWithFiles: SessionWithFiles) {
        insert(sessionWithFiles.session)
        for (file in sessionWithFiles.files!!) {
            insert(file)
        }
    }
}