package com.example.applock

import android.content.Context

class PrefsHelper(context: Context) {
    private val prefs = context.getSharedPreferences("applock_prefs", Context.MODE_PRIVATE)

    fun getLockedApps(): Set<String> =
        prefs.getStringSet("locked_apps", emptySet()) ?: emptySet()

    fun setLockedApps(apps: Set<String>) {
        prefs.edit().putStringSet("locked_apps", apps).apply()
    }
}
