package com.zacharee1.modcontrolredesign

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.widget.Toast
import com.zacharee1.modcontrolredesign.fragments.MainFragment
import com.zacharee1.modcontrolredesign.util.SuUtils

class StarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            Settings.Global.putInt(contentResolver, "Mod Control", 1)
            goHome()
        } catch (e: Exception) {
            if (SuUtils.testSudo()) {
                try {
                    SuUtils.sudo(Array(1) { "pm grant com.zacharee1.modcontrolredesign android.permission.WRITE_SECURE_SETTINGS" })
                    goHome()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                AlertDialog.Builder(this)
                        .setMessage(R.string.grant_perm_message)
                        .setPositiveButton("OK", null)
                        .setOnDismissListener({
                            recreate()
                        })
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
