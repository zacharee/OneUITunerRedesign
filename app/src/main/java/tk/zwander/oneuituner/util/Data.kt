package tk.zwander.oneuituner.util

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue

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