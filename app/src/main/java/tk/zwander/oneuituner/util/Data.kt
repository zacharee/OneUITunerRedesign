package tk.zwander.oneuituner.util

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import tk.zwander.oneuituner.BuildConfig
import tk.zwander.overlaylib.constructOverlayPackage

const val SUFFIX_SYSTEMUI = "oneuituner.systemui"
const val SUFFIX_ANDROID = "oneuituner.android"

const val PACKAGE_SYSTEMUI = "com.android.systemui"
const val PACKAGE_ANDROID = "android"

val MAGISK_PATH = "/sbin/.magisk"
val MAGISK_MODULE_PATH = "$MAGISK_PATH/modules/opfpcontrol"

val OVERLAYS = arrayOf(
    constructOverlayPackage(PACKAGE_SYSTEMUI),
    constructOverlayPackage(PACKAGE_ANDROID)
)

val LEGACY_OVERLAYS = arrayOf(
    "$PACKAGE_SYSTEMUI.${BuildConfig.APPLICATION_ID}.overlay.clock",
    "$PACKAGE_SYSTEMUI.${BuildConfig.APPLICATION_ID}.overlay.qs",
    "$PACKAGE_SYSTEMUI.${BuildConfig.APPLICATION_ID}.overlay.status_bar",
    "$PACKAGE_SYSTEMUI.${BuildConfig.APPLICATION_ID}.overlay.lock_screen",
    "$PACKAGE_ANDROID.${BuildConfig.APPLICATION_ID}.overlay.misc"
)

fun Context.calculateQsWidthForColumnCount(count: Int, landscape: Boolean): Int {
    val displayWidth = dpAsPx(resources.configuration.screenWidthDp)
    val displayHeight = dpAsPx(resources.configuration.screenHeightDp)
    val statusBarHeight = statusBarHeight
    val isInLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val unit = when {
        landscape && isInLandscape -> displayWidth - statusBarHeight
        landscape && !isInLandscape -> displayHeight - statusBarHeight
        !landscape && isInLandscape -> displayHeight
        !landscape && !isInLandscape -> displayWidth
        else -> throw Exception("This shouldn't have happened!")
    }

    val num = unit - (unit * 0.044f).toInt()

    return (pxAsDp(num / count))
}

fun Context.dpAsPx(dp: Number): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
}

fun Context.pxAsDp(px: Number): Int {
    return (px.toFloat() / resources.displayMetrics.density).toInt()
}