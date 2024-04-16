package roctik.app.health.di

import android.app.Application
import android.content.Context
import roctik.app.health.BuildConfig
import roctik.app.health.utils.Const.APP_PREFS_IS_PERMISSION_DENIED
import roctik.app.health.utils.Const.APP_PREFS_START_DATE

class PreferencesImpl(application: Application) : Preferences {

    val pref =
        application.getSharedPreferences(BuildConfig.SECURE_PREFS_FILE_KEY, Context.MODE_PRIVATE)

    override fun getStartDate(): Long {
        return pref.getLong(APP_PREFS_START_DATE, 0L)
    }

    override fun setStartDate(startDate: Long) {
        pref.edit().putLong(APP_PREFS_START_DATE, startDate).commit()
    }

    override fun isPermissionDenied(): Boolean {
        return pref.getBoolean(APP_PREFS_IS_PERMISSION_DENIED, false)
    }

    override fun setPermissionDenied(state: Boolean) {
        pref.edit().putBoolean(APP_PREFS_IS_PERMISSION_DENIED, state).commit()
    }


}