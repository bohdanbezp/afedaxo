package com.afedaxo.model.repository

import android.content.Context
import com.afedaxo.model.sharedprefs.SharedPreferencesDao

class UserRepository(val context: Context) {
    val sharedPreferencesDao = SharedPreferencesDao(context)

    fun isUserPassedTutorial(): Boolean = true

    fun setUserPassedTutorial() = sharedPreferencesDao.setUserPassedTutorial(true)
}