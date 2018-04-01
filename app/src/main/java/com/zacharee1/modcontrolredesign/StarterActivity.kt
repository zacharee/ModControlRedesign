package com.zacharee1.modcontrolredesign

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import com.android.internal.R.id.date
import com.zacharee1.modcontrolredesign.fragments.MainFragment
import com.zacharee1.modcontrolredesign.util.Stuff
import com.zacharee1.modcontrolredesign.util.SuUtils
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class StarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        checkVersion()

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

    private fun checkVersion() {
        Observable.fromCallable { loadAndParseAsync() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    try {
                        val currentDate = IOUtils.toString(FileInputStream("/system/mod_version_mdy"), StandardCharsets.UTF_8).split("_")
                        val currentModDate = ModDate()
                        currentModDate.month = currentDate[0].toInt() - 1
                        currentModDate.day = currentDate[1].toInt()
                        currentModDate.year = currentDate[2].trim().toInt() + 2000

                        if (currentModDate < it) {
                            askToUpdate(it.url, it)
                        }
                    } catch (e: FileNotFoundException) {
                        askToInstall(it.url)
                    } catch (e: NullPointerException) {
                        askToInstall(it.url)
                    }
                }
    }

    private fun loadAndParseAsync(): ModDate {
        val doc = Jsoup.connect("https://androidfilehost.com/?w=files&flid=203777&sort_by=date&sort_dir=DESC").followRedirects(true).get()
        val links = doc.select("div.file-name")

        val name = links[0].children()[0].children()[0]
        val date = "([^_]*)_(\\d+)_(\\d+)_(\\d+)\\.zip\\s*$".toRegex().matchEntire(name.text())?.groups!!

        val modDate = ModDate()
        modDate.year = date[4]!!.value.toInt() + 2000
        modDate.month = date[2]!!.value.toInt() - 1
        modDate.day = date[3]!!.value.toInt()
        modDate.url = name.attr("abs:href")

        return modDate
    }

    private fun askToUpdate(url: String, date: ModDate) {
        val dateFormat = SimpleDateFormat("MMMM dd, YYYY", Locale.getDefault())

        AlertDialog.Builder(this)
                .setTitle(R.string.update_available)
                .setMessage(String.format(Locale.getDefault(), resources.getString(R.string.update_available_desc), dateFormat.format(date.time)))
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    openUrl(url)
                })
                .setNegativeButton(android.R.string.no, null)
                .setCancelable(false)
                .show()
    }

    private fun askToInstall(url: String) {
        AlertDialog.Builder(this)
                .setTitle(R.string.mods_not_installed)
                .setMessage(R.string.mods_not_installed_desc)
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    openUrl(url)
                })
                .setNegativeButton(android.R.string.no, null)
                .setCancelable(false)
                .show()
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    class ModDate : GregorianCalendar() {
        var month: Int
            get() {
                return get(Calendar.MONTH)
            }
            set(value) {
                set(Calendar.MONTH, value)
            }

        var day: Int
            get() {
                return get(Calendar.DATE)
            }
            set(value) {
                set(Calendar.DATE, value)
            }

        var year: Int
            get() {
                return get(Calendar.YEAR)
            }
            set(value) {
                set(Calendar.YEAR, value)
            }

        var url: String = ""

        override fun toString(): String {
            return "Month: $month, Day: $day, Year: $year, URL: $url"
        }
    }
}
