package com.zacharee1.modcontrolredesign.fragments

import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.support.v7.app.AppCompatActivity
import com.zacharee1.modcontrolredesign.R
import com.zacharee1.modcontrolredesign.util.Stuff
import com.zacharee1.modcontrolredesign.util.SuUtils

public class MainFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.pref_main)
        setListeners()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setListeners() {
        findPreference("device").summary = resources.getString(R.string.current_device) + " " + if (Stuff.isV20) Stuff.V20 else Stuff.G5

        val openLog = findPreference("view_log")
        openLog.setOnPreferenceClickListener {
            activity.fragmentManager.beginTransaction().replace(R.id.content_main, LogFragment()).commit()
            true
        }

        val restartSystemUI = findPreference("restart_sysui")
        restartSystemUI.setOnPreferenceClickListener {
            SuUtils.sudo(Array(1) {"killall com.android.systemui"})
            true
        }

        val hotReboot = findPreference("hot_reboot")
        hotReboot.setOnPreferenceClickListener {
            SuUtils.sudo(Array(1) {"killall system_server"})
            true
        }

        val openColors = findPreference("color_mods")
        openColors.setOnPreferenceClickListener {
            activity.fragmentManager.beginTransaction().replace(R.id.content_main, ColorsFragment()).commit()
            true
        }

        val openOthers = findPreference("other_mods")
        openOthers.setOnPreferenceClickListener {
            activity.fragmentManager.beginTransaction().replace(R.id.content_main, OthersFragment()).commit()
            true
        }

        val darkMode = findPreference("dark_mode") as SwitchPreference
        darkMode.setOnPreferenceChangeListener { _, _ ->
            activity.recreate()
            true
        }
    }
}