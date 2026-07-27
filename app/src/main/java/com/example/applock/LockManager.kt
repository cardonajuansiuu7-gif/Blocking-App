package com.example.applock

object LockManager {
    private val unlockedApps = mutableSetOf<String>()

    fun isUnlocked(pkg: String): Boolean = unlockedApps.contains(pkg)

    fun setUnlocked(pkg: String) {
        unlockedApps.add(pkg)
    }

    fun clearUnlocked(pkg: String) {
        unlockedApps.remove(pkg)
    }
}
