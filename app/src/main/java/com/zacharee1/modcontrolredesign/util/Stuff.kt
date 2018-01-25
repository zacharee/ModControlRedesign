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

    val STATUS_BAR_HEIGHT = "status_bar_height"
    val NAV_BAR_HEIGHT = "nav_bar_height"
    val NAV_BAR_BUTTON_PADDING = "nav_bar_button_padding"

    val NAV_COLOR_ENABLED = "nav_color_enabled"
    val IMMERSIVE_SB_ENABLED = "allow_immersive_statusbar"
}