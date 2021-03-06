package com.zacharee1.modcontrolredesign.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.zacharee1.modcontrolredesign.R
import com.zacharee1.modcontrolredesign.util.Stuff
import com.zacharee1.modcontrolredesign.util.BuildProp
import com.zacharee1.modcontrolredesign.util.SuUtils
import com.zacharee1.sliderpreferenceembedded.SliderPreferenceEmbedded

class OthersFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.pref_others)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setSwitchListeners()
        setSliderListeners()
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
        val ssScreenshot = BuildProp.readValueOfKey(Stuff.SS_CAPTURE)

        val homeWidgetsInSettings = Settings.Global.getInt(activity.contentResolver, Stuff.ALLOW_HOME_SCREEN_WIDGETS, 0)

        val immersiveSBEnabled = Settings.Global.getInt(activity.contentResolver, Stuff.IMMERSIVE_SB_ENABLED, 1)

        val overrideFingerprint = Settings.Global.getInt(activity.contentResolver, "override_allow_fingerprint", 0)

        if (Stuff.isV20) {
            val minbatimmSwitch = findPreference("minbatimm") as SwitchPreference
            val minclockimmSwitch = findPreference("minclockimm") as SwitchPreference
            val ssScreenshotSwitch = findPreference("sbscreen") as SwitchPreference
            val immersiveSB = findPreference(Stuff.IMMERSIVE_SB_ENABLED) as SwitchPreference
            val homeWidgetsInSettignsSwitch = findPreference(Stuff.ALLOW_HOME_SCREEN_WIDGETS) as SwitchPreference

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

            ssScreenshotSwitch.isChecked = ssScreenshot.contains("true")
            ssScreenshotSwitch.setOnPreferenceChangeListener { _, any ->
                val newVal = any.toString()
                BuildProp.setValueForKey(Stuff.SS_CAPTURE, newVal)
                true
            }

            immersiveSB.isChecked = immersiveSBEnabled == 1
            immersiveSB.setOnPreferenceChangeListener { _, any ->
                Settings.Global.putInt(activity.contentResolver, Stuff.IMMERSIVE_SB_ENABLED, if (any.toString().toBoolean()) 1 else 0)
                true
            }

            homeWidgetsInSettignsSwitch.isChecked = homeWidgetsInSettings == 1
            homeWidgetsInSettignsSwitch.setOnPreferenceChangeListener { _, any ->
                Settings.Global.putInt(activity.contentResolver, Stuff.ALLOW_HOME_SCREEN_WIDGETS, if (any.toString().toBoolean()) 1 else 0)
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
            SuUtils.sudo("killall com.android.systemui")
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

        val forceBasicInfo = Settings.Global.getInt(context.contentResolver, "force_show_basic_info", 1) == 1
        val forceBasicInfoSwitch = findPreference("force_basic_info") as SwitchPreference

        forceBasicInfoSwitch.isChecked = forceBasicInfo
        forceBasicInfoSwitch.setOnPreferenceChangeListener { _, any ->
            val newVal = any.toString().toBoolean()
            Settings.Global.putInt(context.contentResolver, "force_show_basic_info", if (newVal) 1 else 0)
            true
        }

        val lockReplySwitch = findPreference("lock_screen_allow_remote_input") as SwitchPreference
        lockReplySwitch.isChecked = Settings.Secure.getInt(activity.contentResolver, lockReplySwitch.key, 0) == 1
        lockReplySwitch.setOnPreferenceChangeListener { _, newValue ->
            Settings.Secure.putInt(activity.contentResolver, lockReplySwitch.key, if (newValue.toString().toBoolean()) 1 else 0)
            true
        }

        val overrideFingerprintSwitch = findPreference("override_allow_fingerprint") as SwitchPreference
        overrideFingerprintSwitch.isChecked = overrideFingerprint != 0
        overrideFingerprintSwitch.setOnPreferenceChangeListener { preference, newValue ->
            Settings.Global.putInt(context.contentResolver, preference.key, if (newValue.toString().toBoolean()) 1 else 0)
            true
        }
    }

    private fun setSliderListeners() {
        val statBarSize = findPreference("status_bar_height") as SliderPreferenceEmbedded
        val navBarSize = findPreference("nav_bar_height") as SliderPreferenceEmbedded
        val navBarButtonPadding = findPreference("nav_bar_button_padding") as SliderPreferenceEmbedded

        statBarSize.progress = Settings.Global.getInt(activity.contentResolver, Stuff.STATUS_BAR_HEIGHT, 24)
        navBarSize.progress = Settings.Global.getInt(activity.contentResolver, Stuff.NAV_BAR_HEIGHT, 42)
        navBarButtonPadding.progress = Settings.Global.getInt(activity.contentResolver, Stuff.NAV_BAR_BUTTON_PADDING, 0)

        statBarSize.setOnPreferenceChangeListener { _, any ->
            Settings.Global.putInt(activity.contentResolver, Stuff.STATUS_BAR_HEIGHT, Integer.valueOf(any.toString()))
            true
        }

        navBarSize.setOnPreferenceChangeListener { _, any ->
            Settings.Global.putInt(activity.contentResolver, Stuff.NAV_BAR_HEIGHT, Integer.valueOf(any.toString()))
            true
        }

        navBarButtonPadding.setOnPreferenceChangeListener { _, any ->
            Settings.Global.putInt(activity.contentResolver, Stuff.NAV_BAR_BUTTON_PADDING, Integer.valueOf(any.toString()))
            true
        }
    }
}