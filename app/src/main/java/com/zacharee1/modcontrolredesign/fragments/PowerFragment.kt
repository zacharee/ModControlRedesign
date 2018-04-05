package com.zacharee1.modcontrolredesign.fragments

import android.content.Intent
import android.os.Bundle
import android.graphics.Color
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.jaredrummler.android.colorpicker.ColorPreference

import com.zacharee1.modcontrolredesign.R
import com.zacharee1.modcontrolredesign.activities.PowerMenuActivity
import com.zacharee1.modcontrolredesign.util.Stuff

class PowerFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.pref_power)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSwitchListeners()
        setClickListeners()
    }

    private fun setSwitchListeners() {
        val aospPower = findPreference("aosp_power_menu") as SwitchPreference
        aospPower.setOnPreferenceChangeListener { pref, newValue ->
            Settings.Global.putInt(context.contentResolver, pref.key, if (newValue.toString().toBoolean()) 1 else 0)
        }
        aospPower.isChecked = Settings.Global.getInt(context.contentResolver, aospPower.key, 1) != 0

        val confirmPower = findPreference("should_confirm_poweroff") as SwitchPreference
        confirmPower.setOnPreferenceChangeListener { preference, newValue ->
            Settings.Global.putInt(context.contentResolver, preference.key, if (newValue.toString().toBoolean()) 1 else 0)
        }
        confirmPower.isChecked = Settings.Global.getInt(context.contentResolver, confirmPower.key, 1) != 0
    }

    private fun setClickListeners() {
        val edit = findPreference("edit_power_buttons")
        edit.setOnPreferenceClickListener {
            startActivity(Intent(context, PowerMenuActivity::class.java))
            true
        }
    }
}
