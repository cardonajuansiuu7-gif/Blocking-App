package com.example.applock

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class AppLockAccessibilityService : AccessibilityService() {

    private var lastForegroundPackage: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pkg = event.packageName?.toString() ?: return
        if (pkg == packageName) return

        if (pkg != lastForegroundPackage) {
            lastForegroundPackage?.let { LockManager.clearUnlocked(it) }
            lastForegroundPackage = pkg
        }

        val lockedApps = PrefsHelper(this).getLockedApps()
        if (lockedApps.contains(pkg) && !LockManager.isUnlocked(pkg)) {
            val intent = Intent(this, LockActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("target_package", pkg)
            }
            startActivity(intent)
        }
    }

    override fun onInterrupt() {}
}
