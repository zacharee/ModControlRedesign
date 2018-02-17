package com.zacharee1.modcontrolredesign.fragments

import android.os.Bundle
import android.graphics.Color
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
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
        setUpEnableDisableNavBarButtonColor()
        setUpBarColors()
    }

    private fun setQTColors() {
        if (Stuff.isV20) {
            val baseKeyRed = "redqt_"
            val baseKeyGreen = "greenqt_"
            val baseKeyBlue = "blueqt_"
            val prefKey = "qt_"

            for (i in 0..11) {
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

    private fun setUpEnableDisableNavBarButtonColor() {
        val navPref = findPreference("nav_0") as ColorPreference
        val enablePref = findPreference("enable_nav_color") as SwitchPreference

        val isEnabled = Settings.Global.getInt(activity.contentResolver, Stuff.NAV_COLOR_ENABLED, 0) != 0

        navPref.isEnabled = isEnabled
        enablePref.isChecked = isEnabled

        enablePref.setOnPreferenceChangeListener { _, any ->
            val enabled = java.lang.Boolean.valueOf(any.toString())

            Settings.Global.putInt(activity.contentResolver, Stuff.NAV_COLOR_ENABLED, if (enabled) 1 else 0)
            navPref.isEnabled = enabled
            true
        }
    }

    private fun setUpBarColors() {
        val opaque = findPreference("nav_bar_color_opaque") as ColorPreference
        val semiTrans = findPreference("nav_bar_color_semi_transparent") as ColorPreference
        val trans = findPreference("nav_bar_color_transparent") as ColorPreference

        val opS = findPreference("stat_bar_color_opaque") as ColorPreference
        val stS = findPreference("stat_bar_color_semi_transparent") as ColorPreference
        val trS = findPreference("stat_bar_color_transparent") as ColorPreference

        val forceOpaque = findPreference("force_nav_bar_color") as SwitchPreference

        val forceOpS = findPreference("force_stat_bar_color") as SwitchPreference

        val resetO = findPreference("reset_nav_bar_color_opaque")
        val resetS = findPreference("reset_nav_bar_color_semi_transparent")
        val resetT = findPreference("reset_nav_bar_color_transparent")

        val resOS = findPreference("reset_stat_bar_color_opaque")
        val resSS = findPreference("reset_stat_bar_color_semi_transparent")
        val resTS = findPreference("reset_stat_bar_color_transparent")

        val savedOpaque = Settings.Global.getInt(context.contentResolver, "nav_bar_color_opaque", Color.BLACK)
        val savedSemi = Settings.Global.getInt(context.contentResolver, "nav_bar_color_semi_transparent", Color.parseColor("#66000000"))
        val savedTransparent = Settings.Global.getInt(context.contentResolver, "nav_bar_color_transparent", Color.TRANSPARENT)

        val savedOS = Settings.Global.getInt(context.contentResolver, "stat_bar_color_opaque", Color.BLACK)
        val savedSS = Settings.Global.getInt(context.contentResolver, "stat_bar_color_semi_transparent", Color.parseColor("#66000000"))
        val savedTS = Settings.Global.getInt(context.contentResolver, "stat_bar_color_transparent", Color.TRANSPARENT)

        opaque.saveValue(savedOpaque)
        semiTrans.saveValue(savedSemi)
        trans.saveValue(savedTransparent)

        opS.saveValue(savedOS)
        stS.saveValue(savedSS)
        trS.saveValue(savedTS)

        val listener = Preference.OnPreferenceChangeListener { preference, newValue ->
            Settings.Global.putInt(context.contentResolver, preference.key, newValue.toString().toInt())
            true
        }

        opaque.onPreferenceChangeListener = listener
        semiTrans.onPreferenceChangeListener = listener
        trans.onPreferenceChangeListener = listener

        opS.onPreferenceChangeListener = listener
        stS.onPreferenceChangeListener = listener
        trS.onPreferenceChangeListener = listener

        val savedForce = Settings.Global.getInt(context.contentResolver, "force_nav_bar_color", 0) != 0

        val savedFS = Settings.Global.getInt(context.contentResolver, "force_stat_bar_color", 0) != 0

        forceOpaque.isChecked = savedForce

        forceOpS.isChecked = savedFS

        val forceListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            var toSave = 0
            if (newValue.toString().toBoolean()) {
                toSave = 1
            }

            Settings.Global.putInt(context.contentResolver, preference.key, toSave)
            true
        }

        forceOpaque.onPreferenceChangeListener = forceListener

        forceOpS.onPreferenceChangeListener = forceListener

        val resetListener = Preference.OnPreferenceClickListener { preference ->
            val key = preference.key.replace("reset_", "")
            Settings.Global.putString(context.contentResolver, key, null)

            if (key.contains("opaque")) {
                if (key.contains("nav")) {
                    opaque.saveValue(Color.BLACK)
                } else {
                    opS.saveValue(Color.BLACK)
                }
            } else if (key.contains("semi")) {
                if (key.contains("nav")) {
                    semiTrans.saveValue(Color.parseColor("#66000000"))
                } else {
                    stS.saveValue(Color.parseColor("#66000000"))
                }
            } else {
                if (key.contains("nav")) {
                    trans.saveValue(Color.TRANSPARENT)
                } else {
                    trS.saveValue(Color.TRANSPARENT)
                }
            }

            true
        }

        resetO.onPreferenceClickListener = resetListener
        resetS.onPreferenceClickListener = resetListener
        resetT.onPreferenceClickListener = resetListener

        resOS.onPreferenceClickListener = resetListener
        resSS.onPreferenceClickListener = resetListener
        resTS.onPreferenceClickListener = resetListener
    }
}
