package tk.zwander.oneuituner.util

import tk.zwander.oneuituner.BuildConfig

object Keys {
    const val systemuiPkg = "com.android.systemui"
    const val androidPkg = "android"

    const val overlay = "overlay"
    const val clock = "clock"
    const val qs = "qs"
    const val misc = "misc"
    const val statusBar = "status_bar"
    const val lockScreen = "lock_screen"

    const val clockPkg = "$systemuiPkg.${BuildConfig.APPLICATION_ID}.$overlay.$clock"
    const val qsPkg = "$systemuiPkg.${BuildConfig.APPLICATION_ID}.$overlay.$qs"
    const val miscPkg = "$androidPkg.${BuildConfig.APPLICATION_ID}.$overlay.$misc"
    const val statusBarPkg = "$systemuiPkg.${BuildConfig.APPLICATION_ID}.$overlay.$statusBar"
    const val lockScreenPkg = "$systemuiPkg.${BuildConfig.APPLICATION_ID}.$overlay.$lockScreen"
}