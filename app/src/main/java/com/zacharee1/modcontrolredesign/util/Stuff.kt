package com.zacharee1.modcontrolredesign.util

import android.os.Build

object Stuff {
    const val V20 = "V20"
    const val G5 = "G5"

    const val V20_CODE = "elsa"
    const val G5_CODE = "h1"

    val isV20 = Build.DEVICE == V20_CODE
    val isSupportedDevice = Build.DEVICE == V20_CODE || Build.DEVICE == G5_CODE

    const val SS_CAPTURE = "sys.capture_signboard.enabled"

    const val BAT_STAT = "minbatsui"
    const val BAT_IMM = "battery_min_imm"
    const val BAT_AOD = "min_bat_aod"

    const val CLOCK_STAT = "minclocksui"
    const val CLOCK_IMM = "minclockimm"
    const val CLOCK_AOD = "minclockaod"

    const val STOCK_SIGNAL = "wide_data"
    const val WAKE_ON_PLUG = "wake_on_plug"
    const val CHARGE_WARN = "charge_warning"

    const val STATUS_BAR_HEIGHT = "status_bar_height"
    const val NAV_BAR_HEIGHT = "nav_bar_height"
    const val NAV_BAR_BUTTON_PADDING = "nav_bar_button_padding"

    const val NAV_COLOR_ENABLED = "nav_color_enabled"
    const val IMMERSIVE_SB_ENABLED = "allow_immersive_statusbar"

    const val ALLOW_HOME_SCREEN_WIDGETS = "allow_home_screen_widgets"
}