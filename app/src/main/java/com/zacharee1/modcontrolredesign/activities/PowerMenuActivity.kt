package com.zacharee1.modcontrolredesign.activities

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import com.jmedeisis.draglinearlayout.DragLinearLayout
import com.zacharee1.modcontrolredesign.R
import com.zacharee1.modcontrolredesign.views.NavBarDragItemView
import java.util.*
import kotlin.collections.ArrayList

class PowerMenuActivity : AppCompatActivity() {
    companion object {
        val ALL_KEYS = arrayListOf(
                "power",
                "reboot",
                "recovery",
                "airplane",
                "settings",
                "lockdown",
                "bugreport",
                "users",
                "assist",
                "voiceassist",
                "silent"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpDragger()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                android.R.id.home -> finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpDragger() {
        val orderer = findViewById<DragLinearLayout>(R.id.content_main)
        val buttons = ArrayList<NavBarDragItemView>()

        val defaultOrder = getDefaultOrder()
        val customOrder = getCustomOrder()
        val order = ArrayList<PowerButton>()

        if (customOrder.isEmpty()) order.addAll(defaultOrder) else order.addAll(customOrder)

        ALL_KEYS
                .map { PowerButton(it, false) }
                .filterNot { order.contains(it) }
                .forEach { order.add(it) }

        order.forEach {
            val buttonView = LayoutInflater.from(this).inflate(R.layout.vh, orderer, false) as NavBarDragItemView
            buttonView.button = it
            buttonView.switch.setOnCheckedChangeListener { _, isChecked ->
                buttonView.button?.enabled = isChecked
                updateOrder(buttons)
            }
            buttons.add(buttonView)

            orderer.addDragView(buttonView, buttonView.findViewById(R.id.handle))
            orderer.setOnViewSwapListener { _, firstPosition, _, secondPosition ->
                Collections.swap(buttons, firstPosition, secondPosition)
                updateOrder(buttons)
            }
            orderer.setContainerScrollView(findViewById(R.id.container))
        }
    }

    private fun updateOrder(list: ArrayList<NavBarDragItemView>) {
        val enabled = ArrayList<String?>()

        list
                .filter { it.button?.enabled ?: false }
                .mapTo(enabled) { it.button?.key }

        Settings.Global.putString(contentResolver, "power_menu_buttons", TextUtils.join(",", enabled))
    }

    @SuppressLint("PrivateApi")
    private fun getDefaultOrder(): ArrayList<PowerButton> {
        val clazz = Class.forName("com.android.internal.R\$array")
        val arrayField = clazz.getField("config_globalActionsList")
        val arrayInt = arrayField.getInt(null)

        val ret = ArrayList<PowerButton>()
        resources.getStringArray(arrayInt).forEach {
            ret.add(PowerButton(it, true))
        }

        return ret
    }

    private fun getCustomOrder(): ArrayList<PowerButton> {
        val ret = ArrayList<PowerButton>()

        Settings.Global.getString(contentResolver, "power_menu_buttons")?.split(",")?.toTypedArray()?.filter { it.isNotEmpty() }?.forEach {
            ret.add(PowerButton(it, true))
        }

        return ret
    }

    class PowerButton(var key: String?, var enabled: Boolean) {
        var name = when (key) {
            "power" -> R.string.power
            "reboot" -> R.string.reboot
            "recovery" -> R.string.recovery
            "airplane" -> R.string.airplane
            "settings" -> R.string.settings
            "lockdown" -> R.string.lockdown
            "bugreport" -> R.string.bugreport
            "users" -> R.string.users
            "silent" -> R.string.silent
            "assist" -> R.string.assist
            "voiceassist" -> R.string.voiceassist
            else -> R.string.blank
        }

        override fun equals(other: Any?): Boolean {
            if (other is PowerButton) {
                return name == other.name && key == other.key
            }

            return false
        }

        override fun hashCode(): Int {
            return name.hashCode() + (key?.hashCode() ?: 0)
        }
    }
}
