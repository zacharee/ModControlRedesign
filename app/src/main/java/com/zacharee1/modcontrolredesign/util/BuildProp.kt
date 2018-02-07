package com.zacharee1.modcontrolredesign.util

object BuildProp {
    val c: Class<*> = Class.forName("android.os.SystemProperties")

    fun readValueOfKey(key: String): String {
        return try {
            val method = c.getMethod("get", String::class.java)
            method.invoke(null, key).toString()
        } catch (e: Exception) {
            SuUtils.sudoForResult("getprop $key")
        }
    }

    fun setValueForKey(key: String, value: String) {
        try {
            val method = c.getMethod("set", String::class.java, String::class.java)
            method.invoke(null, key, value)
        } catch (e: Exception) {
            SuUtils.sudo("setprop $key $value")
        }
    }
}