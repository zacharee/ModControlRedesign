package com.zacharee1.modcontrolredesign.util

object BuildProp {
    val c: Class<*> = Class.forName("android.os.SystemProperties")

    fun readValueOfKey(key: String): String {
        val method = c.getMethod("get", String::class.java)
        val value: String = method.invoke(null, key).toString()
        return value
    }

    fun setValueForKey(key: String, value: String) {
        val method = c.getMethod("set", String::class.java, String::class.java)
        method.invoke(null, key, value)
    }
}