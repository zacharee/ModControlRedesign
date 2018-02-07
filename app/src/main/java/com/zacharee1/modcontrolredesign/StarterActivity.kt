package com.zacharee1.modcontrolredesign

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import com.zacharee1.modcontrolredesign.fragments.MainFragment
import com.zacharee1.modcontrolredesign.util.Stuff
import com.zacharee1.modcontrolredesign.util.SuUtils

class StarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val useDark = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_mode", false)
        setTheme(if (useDark) R.style.AppTheme_Dark else R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Stuff.isSupportedDevice) {
            AlertDialog.Builder(this)
                    .setMessage(String.format(resources.getString(R.string.unsupported_device), Build.DEVICE, Stuff.V20_CODE, Stuff.G5_CODE))
                    .setPositiveButton("OK", null)
                    .setOnDismissListener {
                        finish()
                    }
                    .setCancelable(false)
                    .show()
        }

        try {
            Settings.Global.putInt(contentResolver, "Mod Control", 1)
            goHome()
        } catch (e: Exception) {
            if (SuUtils.testSudo()) {
                try {
                    SuUtils.sudo("pm grant com.zacharee1.modcontrolredesign android.permission.WRITE_SECURE_SETTINGS")
                    goHome()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                AlertDialog.Builder(this)
                        .setMessage(R.string.grant_perm_message)
                        .setPositiveButton("OK", null)
                        .setOnDismissListener {
                            recreate()
                        }
                        .show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            goHome()
            return true
        }

        return false
    }

    override fun onBackPressed() {
        if (fragmentManager.findFragmentByTag("home") == null) {
            goHome()
        } else {
            super.onBackPressed()
        }
    }

    private fun goHome() {
        fragmentManager.beginTransaction().replace(R.id.content_main, MainFragment(), "home").commit()
    }
}
