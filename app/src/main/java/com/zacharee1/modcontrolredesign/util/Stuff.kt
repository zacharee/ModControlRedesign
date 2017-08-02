package com.zacharee1.modcontrolredesign.util

import android.os.Build

object Stuff {
    val V20 = "V20"
    val G5 = "G5"

    val V20_CODE = "elsa"
    val G5_CODE = "h1"

    val isV20 = Build.DEVICE == V20_CODE
    val isSupportedDevice = Build.DEVICE == V20_CODE || Build.DEVICE == G5_CODE

    val SS_CAPTURE = "sys.capture_signboard.enabled"

    val BAT_STAT = "minbatsui"
    val BAT_IMM = "battery_min_imm"
    val BAT_AOD = "min_bat_aod"

    val CLOCK_STAT = "minclocksui"
    val CLOCK_IMM = "minclockimm"
    val CLOCK_AOD = "minclockaod"

    val STOCK_SIGNAL = "wide_data"
    val WAKE_ON_PLUG = "wake_on_plug"
    val CHARGE_WARN = "charge_warning"
}