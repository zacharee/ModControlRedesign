package com.zacharee1.modcontrolredesign.fragments

import android.os.Bundle
import android.graphics.Color
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.jaredrummler.android.colorpicker.ColorPreference

import com.zacharee1.modcontrolredesign.R
import com.zacharee1.modcontrolredesign.util.Stuff

class ColorsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.pref_colors)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setQTColors()
        setSigColors()
        setNavBarColors()
    }

    private fun setQTColors() {
        if (Stuff.isV20) {
            val baseKeyRed = "redqt_"
            val baseKeyGreen = "greenqt_"
            val baseKeyBlue = "blueqt_"
            val prefKey = "qt_"

            for (i in 0..4) {
                val pref = findPreference(prefKey + i) as ColorPreference
                val colorRed = Settings.Global.getInt(activity.contentResolver, baseKeyRed + i, 0xff)
                val colorGreen = Settings.Global.getInt(activity.contentResolver, baseKeyGreen + i, 0xff)
                val colorBlue = Settings.Global.getInt(activity.contentResolver, baseKeyBlue + i, 0xff)
                val color = Color.argb(0xff, colorRed, colorGreen, colorBlue)
                pref.saveValue(color)

                pref.setOnPreferenceChangeListener { _, any ->
                    val colorInt = Integer.valueOf(any.toString())
                    val red = Color.red(colorInt)
                    val green = Color.green(colorInt)
                    val blue = Color.blue(colorInt)

                    Settings.Global.putInt(activity.contentResolver, baseKeyRed + i, red)
                    Settings.Global.putInt(activity.contentResolver, baseKeyGreen + i, green)
                    Settings.Global.putInt(activity.contentResolver, baseKeyBlue + i, blue)
                    true
                }
            }
        } else {
            findPreference("quick_tools").isEnabled = false
        }
    }

    private fun setSigColors() {
        val sig = findPreference("sig_color") as ColorPreference
        val sigAod = findPreference("aod_sig_color") as ColorPreference
        val baseRed = "redsig"
        val baseGreen = "greensig"
        val baseBlue = "bluesig"
        val aod = "aod"

        if (!Stuff.isV20) sig.isEnabled = false

        val colorRed = Settings.Global.getInt(activity.contentResolver, baseRed, 0xff)
        val colorGreen = Settings.Global.getInt(activity.contentResolver, baseGreen, 0xff)
        val colorBlue = Settings.Global.getInt(activity.contentResolver, baseBlue, 0xff)
        val colorRedAod = Settings.Global.getInt(activity.contentResolver, baseRed + aod, 0xff)
        val colorGreenAod = Settings.Global.getInt(activity.contentResolver, baseGreen + aod, 0xff)
        val colorBlueAod = Settings.Global.getInt(activity.contentResolver, baseBlue + aod, 0xff)

        if (Stuff.isV20) sig.saveValue(Color.argb(0xff, colorRed, colorGreen, colorBlue))
        sigAod.saveValue(Color.argb(0xff, colorRedAod, colorGreenAod, colorBlueAod))

        if (Stuff.isV20) sig.setOnPreferenceChangeListener { _, any ->
            val color = Integer.valueOf(any.toString())
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)

            Settings.Global.putInt(activity.contentResolver, baseRed, red)
            Settings.Global.putInt(activity.contentResolver, baseGreen, green)
            Settings.Global.putInt(activity.contentResolver, baseBlue, blue)
            true
        }

        sigAod.setOnPreferenceChangeListener { _, any ->
            val color = Integer.valueOf(any.toString())
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)

            Settings.Global.putInt(activity.contentResolver, baseRed + aod, red)
            Settings.Global.putInt(activity.contentResolver, baseGreen + aod, green)
            Settings.Global.putInt(activity.contentResolver, baseBlue + aod, blue)
            true
        }
    }

    private fun setNavBarColors() { //using same method as QuickTools to allow for future changes (ie individual button coloring if I ever figure that out)
        val baseKeyRed = "rednav_"
        val baseKeyGreen = "greennav_"
        val baseKeyBlue = "bluenav_"
        val prefKey = "nav_"

        for (i in 0..0) {
            val pref = findPreference(prefKey + i) as ColorPreference
            val colorRed = Settings.Global.getInt(activity.contentResolver, baseKeyRed + i, 0xff)
            val colorGreen = Settings.Global.getInt(activity.contentResolver, baseKeyGreen + i, 0xff)
            val colorBlue = Settings.Global.getInt(activity.contentResolver, baseKeyBlue + i, 0xff)
            val color = Color.argb(0xff, colorRed, colorGreen, colorBlue)
            pref.saveValue(color)

            pref.setOnPreferenceChangeListener { _, any ->
                val colorInt = Integer.valueOf(any.toString())
                val red = Color.red(colorInt)
                val green = Color.green(colorInt)
                val blue = Color.blue(colorInt)

                Settings.Global.putInt(activity.contentResolver, baseKeyRed + i, red)
                Settings.Global.putInt(activity.contentResolver, baseKeyGreen + i, green)
                Settings.Global.putInt(activity.contentResolver, baseKeyBlue + i, blue)
                true
            }
        }
    }
}
