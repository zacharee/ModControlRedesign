package com.zacharee1.modcontrolredesign

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.android.internal.R.id.date
import com.zacharee1.modcontrolredesign.fragments.MainFragment
import com.zacharee1.modcontrolredesign.util.Stuff
import com.zacharee1.modcontrolredesign.util.SuUtils
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class StarterActivity : AppCompatActivity() {
    companion object {
        const val REQ = 1001
    }

    private var date = ModDate()
    private var install = false

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

    fun checkVersion() {
        Toast.makeText(this, resources.getText(R.string.checking_for_updates), Toast.LENGTH_SHORT).show()
        Observable.fromCallable { loadAndParseAsync() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    try {
                        val currentDate = IOUtils.toString(FileInputStream("/system/mod_version_mdy"), StandardCharsets.UTF_8).split("_")
                        val currentModDate = ModDate()
                        currentModDate.month = currentDate[0].toInt()
                        currentModDate.day = currentDate[1].toInt()
                        currentModDate.year = currentDate[2].trim().toInt()

                        if (currentModDate < it) {
                            askToUpdate(it)
                        } else {
                            Toast.makeText(this, resources.getText(R.string.no_updates_found), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: FileNotFoundException) {
                        askToInstall(it)
                    } catch (e: NullPointerException) {
                        askToInstall(it)
                    }
                }
    }

    private fun loadAndParseAsync(): ModDate {
        val content = IOUtils.toString(URL("https://api.github.com/repos/zacharee/V20Mods_Releases/releases/latest"))
        val json = JSONObject(content)

        val tag = json["tag_name"].toString().split("_")

        val asset = json.getJSONArray("assets").getJSONObject(0)
        val file = asset["browser_download_url"].toString()
        val name = asset["name"].toString()

        val modDate = ModDate()
        modDate.month = tag[0].toInt()
        modDate.day = tag[1].toInt()
        modDate.year = tag[2].toInt()
        modDate.url = file
        modDate.fileName = name

        return modDate
    }

    private fun askToUpdate(date: ModDate) {
        val dateFormat = SimpleDateFormat("MMMM dd, YYYY", Locale.getDefault())

        AlertDialog.Builder(this)
                .setTitle(R.string.update_available)
                .setMessage(String.format(Locale.getDefault(), resources.getString(R.string.update_available_desc), dateFormat.format(date.time)))
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    openUrl(date, true)
                })
                .setNeutralButton(R.string.download_only, { _, _ ->
                    openUrl(date, false)
                })
                .setNegativeButton(android.R.string.no, null)
                .setCancelable(false)
                .show()
    }

    private fun askToInstall(date: ModDate) {
        AlertDialog.Builder(this)
                .setTitle(R.string.mods_not_installed)
                .setMessage(R.string.mods_not_installed_desc)
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    openUrl(date, true)
                })
                .setNeutralButton(R.string.download_only, { _, _ ->
                    openUrl(date, false)
                })
                .setNegativeButton(android.R.string.no, null)
                .setCancelable(false)
                .show()
    }

    private fun askToReboot() {
        AlertDialog.Builder(this)
                .setTitle(R.string.reboot)
                .setMessage(R.string.reboot_needed)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    SuUtils.sudo("reboot recovery")
                })
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    private fun openUrl(date: ModDate, install: Boolean) {
        this.date = date
        this.install = install

        if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQ)
        } else {
            startDownload(date, install)
        }
    }

    private fun startDownload(date: ModDate, install: Boolean) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                    unregisterReceiver(this)

                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id != -1L) {
                        val query = DownloadManager.Query()
                        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)
                        val cursor = downloadManager.query(query)
                        if (!cursor.moveToFirst()) {
                            cursor.close()
                            return
                        }
                        do {
                            val reference = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
                            if (id == reference) {
                                performInstall()
                            }
                        } while (cursor.moveToNext())
                        cursor.close()
                    }
                }
            }
        }
        makeDirectoryIfNeeded()

        if (!fileExists(date.fileName)) {
            if (install) registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

            val request = DownloadManager.Request(Uri.parse(date.url))

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setDestinationInExternalPublicDir("/V20Mods", date.fileName)
            downloadManager.enqueue(request)
        } else {
            if (install) performInstall()
        }
    }

    private fun performInstall() {
        SuUtils.sudo("echo '--update_package=/sdcard/0/V20Mods/${date.fileName}' >> /cache/recovery/command")
        askToReboot()
    }

    private fun makeDirectoryIfNeeded() {
        val directory = File(Environment.getExternalStorageDirectory().path + "/V20Mods")
        directory.mkdirs()
    }

    private fun fileExists(name: String): Boolean {
        val file = File(Environment.getExternalStorageDirectory().path + "/V20Mods/" + name)
        return file.exists()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQ) {
            if (permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[permissions.indexOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)] == PackageManager.PERMISSION_GRANTED) {
                startDownload(date, install)
            }
        }
    }

    class ModDate : GregorianCalendar() {
        var month: Int
            get() {
                return get(Calendar.MONTH) + 1
            }
            set(value) {
                set(Calendar.MONTH, value - 1)
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
                var v = value
                if (value < 2000) v += 2000
                set(Calendar.YEAR, v)
            }

        var fileName: String = ""
        var url: String = ""

        override fun toString(): String {
            return "Month: ${month + 1}, Day: $day, Year: $year, URL: $url"
        }
    }
}
