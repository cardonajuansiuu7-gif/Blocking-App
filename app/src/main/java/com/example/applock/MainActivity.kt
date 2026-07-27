package com.example.applock

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    data class AppInfo(val label: String, val packageName: String)

    private lateinit var listView: ListView
    private lateinit var apps: List<AppInfo>
    private lateinit var prefs: PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = PrefsHelper(this)
        listView = findViewById(R.id.listApps)
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        apps = getInstalledApps()
        val labels = apps.map { it.label }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, labels)
        listView.adapter = adapter

        val locked = prefs.getLockedApps()
        apps.forEachIndexed { index, app ->
            if (locked.contains(app.packageName)) {
                listView.setItemChecked(index, true)
            }
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val selected = mutableSetOf<String>()
            for (i in apps.indices) {
                if (listView.isItemChecked(i)) selected.add(apps[i].packageName)
            }
            prefs.setLockedApps(selected)
            Toast.makeText(this, "Guardado: ${selected.size} apps bloqueadas", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnAccessibility).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            Toast.makeText(
                this,
                "Busca 'BlockingApping' en la lista y actívalo",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getInstalledApps(): List<AppInfo> {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos = pm.queryIntentActivities(intent, 0)
        return resolveInfos
            .filter { it.activityInfo.packageName != packageName }
            .map { AppInfo(it.loadLabel(pm).toString(), it.activityInfo.packageName) }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }
}
