package com.zacharee1.modcontrolredesign.util

import android.util.Log
import java.io.*

object SuUtils {
    @JvmStatic fun sudo(vararg strings: String) {
        try {
            val su = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(su.outputStream)

            for (s in strings) {
                outputStream.writeBytes(s + "\n")
                outputStream.flush()
            }

            outputStream.writeBytes("exit\n")
            outputStream.flush()
            try {
                su.waitFor()
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Log.e("No Root?", e.message)
            }

            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic fun sudoForResult(vararg strings: String): String {
        return try {
            val su = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(su.outputStream)
            val reader = BufferedReader(InputStreamReader(su.inputStream))

            for (s in strings) {
                outputStream.writeBytes(s + "\n")
                outputStream.flush()
            }

            outputStream.writeBytes("exit\n")
            outputStream.flush()

            val builder = StringBuilder()

            reader.forEachLine {
                builder.append(it + "\n")
            }

            outputStream.close()
            reader.close()

            builder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun testSudo(): Boolean {
        var st: StackTraceElement? = null

        try {
            val su = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(su.outputStream)

            outputStream.writeBytes("exit\n")
            outputStream.flush()

            val inputStream = DataInputStream(su.inputStream)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            while (bufferedReader.readLine() != null) {
                bufferedReader.readLine()
            }

            su.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
            for (s in e.stackTrace) {
                st = s
                if (st != null) break
            }
        }

        return st == null
    }
}