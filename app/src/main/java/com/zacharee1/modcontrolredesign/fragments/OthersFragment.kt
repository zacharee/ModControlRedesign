package com.zacharee1.modcontrolredesign.fragments

import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.zacharee1.modcontrolredesign.R
import com.zacharee1.modcontrolredesign.util.Stuff
import com.zacharee1.modcontrolredesign.util.BuildProp
import com.zacharee1.modcontrolredesign.util.SuUtils
import java.lang.Exception

class OthersFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.pref_others)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setSwitchListeners()
        setSoundListeners()
    }

    private fun setSwitchListeners() {
        val minbatstat = Settings.Global.getInt(activity.contentResolver, Stuff.BAT_STAT, 0)
        val minclockstat = Settings.Global.getInt(activity.contentResolver, Stuff.CLOCK_STAT, 0)
        val aospsignal = Settings.Global.getInt(activity.contentResolver, Stuff.STOCK_SIGNAL, 1)

        val minbatimm = Settings.Global.getInt(activity.contentResolver, Stuff.BAT_IMM, 0)
        val minclockimm = Settings.Global.getInt(activity.contentResolver, Stuff.CLOCK_IMM, 0)

        val minbataod = Settings.Global.getInt(activity.contentResolver, Stuff.BAT_AOD, 0)
        val minclockaod = Settings.Global.getInt(activity.contentResolver, Stuff.CLOCK_AOD, 0)

        val wakeonplug = Settings.Global.getInt(activity.contentResolver, Stuff.WAKE_ON_PLUG, 1)
        val slowchargewarn = Settings.Global.getInt(activity.contentResolver, Stuff.CHARGE_WARN, 1)
        val ss_screenshot = BuildProp.readValueOfKey(Stuff.SS_CAPTURE)

        if (Stuff.isV20) {
            val minbatimmSwitch = findPreference("minbatimm") as SwitchPreference
            val minclockimmSwitch = findPreference("minclockimm") as SwitchPreference
            val ss_screenshotSwitch = findPreference("sbscreen") as SwitchPreference

            minbatimmSwitch.isChecked = minbatimm == 1
            minbatimmSwitch.setOnPreferenceChangeListener { _, any ->
                val newVal = java.lang.Boolean.valueOf(any.toString())
                Settings.Global.putInt(activity.contentResolver, Stuff.BAT_IMM, if (newVal) 1 else 0)
                true
            }

            minclockimmSwitch.isChecked = minclockimm == 1
            minclockimmSwitch.setOnPreferenceChangeListener { _, any ->
                val newVal = java.lang.Boolean.valueOf(any.toString())
                Settings.Global.putInt(activity.contentResolver, Stuff.CLOCK_IMM, if (newVal) 1 else 0)
                true
            }

            ss_screenshotSwitch.isChecked = ss_screenshot == "true"
            ss_screenshotSwitch.setOnPreferenceChangeListener { _, any ->
                val newVal = any.toString()
                BuildProp.setValueForKey(Stuff.SS_CAPTURE, newVal)
                true
            }
        } else {
            val immCategory = findPreference("immersive") as PreferenceCategory
            immCategory.isEnabled = false

            findPreference("sbscreen").isEnabled = false
        }

        val minbatstatSwitch = findPreference("minbatstat") as SwitchPreference
        val minclockstatSwitch = findPreference("minclockstat") as SwitchPreference
        val aospsignalSwitch = findPreference("aospsignal") as SwitchPreference

        minbatstatSwitch.isChecked = minbatstat == 1
        minbatstatSwitch.setOnPreferenceChangeListener { _, any ->
            val newVal = java.lang.Boolean.valueOf(any.toString())
            Settings.Global.putInt(activity.contentResolver, Stuff.BAT_STAT, if (newVal) 1 else 0)
            true
        }

        minclockstatSwitch.isChecked = minclockstat == 1
        minclockstatSwitch.setOnPreferenceChangeListener { _, any ->
            val newVal = java.lang.Boolean.valueOf(any.toString())
            Settings.Global.putInt(activity.contentResolver, Stuff.CLOCK_STAT, if (newVal) 1 else 0)
            true
        }

        aospsignalSwitch.isChecked = aospsignal != 1
        aospsignalSwitch.setOnPreferenceChangeListener { _, any ->
            val newVal = java.lang.Boolean.valueOf(any.toString())
            Settings.Global.putInt(activity.contentResolver, Stuff.STOCK_SIGNAL, if (newVal) 0 else 1)
            SuUtils.sudo(Array(1) {"killall com.android.systemui"})
            true
        }

        val minbataodSwitch = findPreference("minbataod") as SwitchPreference
        val minclockaodSwitch = findPreference("minclockaod") as SwitchPreference

        minbataodSwitch.isChecked = minbataod == 1
        minbataodSwitch.setOnPreferenceChangeListener { _, any ->
            val newVal = java.lang.Boolean.valueOf(any.toString())
            Settings.Global.putInt(activity.contentResolver, Stuff.BAT_AOD, if (newVal) 1 else 0)
            true
        }

        minclockaodSwitch.isChecked = minclockaod == 1
        minclockaodSwitch.setOnPreferenceChangeListener { _, any ->
            val newVal = java.lang.Boolean.valueOf(any.toString())
            Settings.Global.putInt(activity.contentResolver, Stuff.CLOCK_AOD, if (newVal) 1 else 0)
            true
        }

        val slowchargewarnSwitch = findPreference("chargewarn") as SwitchPreference

        slowchargewarnSwitch.isChecked = slowchargewarn == 1
        slowchargewarnSwitch.setOnPreferenceChangeListener { _, any ->
            val newVal = java.lang.Boolean.valueOf(any.toString())
            Settings.Global.putInt(activity.contentResolver, Stuff.CHARGE_WARN, if (newVal) 1 else 0)
            true
        }

        val wakeonplugSwitch = findPreference("wakeplug") as SwitchPreference

        wakeonplugSwitch.isChecked = wakeonplug == 1
        wakeonplugSwitch.setOnPreferenceChangeListener { _, any ->
            val newVal = java.lang.Boolean.valueOf(any.toString())
            Settings.Global.putInt(activity.contentResolver, Stuff.WAKE_ON_PLUG, if (newVal) 1 else 0)
            true
        }
    }

    private fun setSoundListeners() {
        val volStepSize = findPreference("volume_step_size") as EditTextPreference

        val currentStepSize = Settings.Global.getInt(activity.contentResolver, volStepSize.key, 2)

        volStepSize.summary = Integer.toString(currentStepSize)

        volStepSize.setOnPreferenceChangeListener { preference, newValue ->
            try {
                val size = Integer.valueOf(newValue.toString())

                Settings.Global.putInt(activity.contentResolver, preference.key, size)

                preference.summary = Integer.toString(size)
            } catch (e: Exception) {

            }
            true
        }
    }
}