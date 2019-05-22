package com.afedaxo.model.sharedprefs

import android.content.Context
import android.preference.PreferenceManager


class SharedPreferencesDao(val context: Context) {
    companion object {
        const val USER_PASSED_TUTORIAL = "USER_PASSED_TUTORIAL"
    }


    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    @Synchronized
    fun isUserPassedTutorial(): Boolean {
        return prefs.getBoolean(USER_PASSED_TUTORIAL, false)
    }

    @Synchronized
    fun setUserPassedTutorial(isUserPassedTutorial: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(USER_PASSED_TUTORIAL, isUserPassedTutorial)
        editor.apply()
    }
}