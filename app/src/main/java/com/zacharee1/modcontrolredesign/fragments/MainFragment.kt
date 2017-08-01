package com.zacharee1.modcontrolredesign.fragments

import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.support.v7.app.AppCompatActivity
import com.zacharee1.modcontrolredesign.R
import com.zacharee1.modcontrolredesign.Stuff
import com.zacharee1.modcontrolredesign.util.SuUtils

public class MainFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.pref_main)
        setListeners()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setListeners() {
        val chooseDevice = findPreference("device") as ListPreference
        chooseDevice.summary = setDeviceChosen(chooseDevice.value)
        chooseDevice.setOnPreferenceChangeListener { preference, any ->
            preference.summary = setDeviceChosen(any.toString())
            true
        }

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
        darkMode.setOnPreferenceChangeListener { preference, any ->
            //change to dark theme
            true
        }
    }

    private fun setDeviceChosen(value: String?): String {
        if (value != null) {
            findPreference("mods").isEnabled = true

            when (Integer.valueOf(value)) {
                Stuff.V20 -> {
                    return "V20"
                }
                Stuff.G5 -> {
                    return "G5"
                }
            }
        } else {
            findPreference("mods").isEnabled = false
        }

        return resources.getString(R.string.choose_device)
    }
}