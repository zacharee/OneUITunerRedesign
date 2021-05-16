package tk.zwander.oneuituner.util.values.dimens

import android.content.Context
import android.os.Build
import tk.zwander.oneuituner.util.calculateQsWidthForColumnCount
import tk.zwander.oneuituner.util.calculateQsWidthForHeaderCount
import tk.zwander.oneuituner.util.prefs
import tk.zwander.overlaylib.ResourceData

fun Context.makeDefaultCommonDimens(): List<ResourceData> {
    return arrayListOf<ResourceData>().apply {
        if (prefs.hidePanelCarrier) {
            add(
                ResourceData(
                    "dimen",
                    "notification_panel_carrier_label_height",
                    "0dp"
                )
            )
            add(
                ResourceData(
                    "dimen",
                    "notification_panel_carrier_label_height_zvv",
                    "0dp"
                )
            )
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if (prefs.adjustQsGrid) {
                add(
                    ResourceData(
                        "dimen",
                        "sec_qs_tile_width",
                        "${
                            calculateQsWidthForColumnCount(
                                prefs.qsColCountPortrait,
                                false
                            )
                        }dp"
                    )
                )
            }
            if (prefs.adjustQsHeader) {
                add(
                    ResourceData(
                        "dimen",
                        "qs_quick_tile_size",
                        "${calculateQsWidthForHeaderCount(prefs.headerCountPortrait, false)}dp"
                    )
                )
            }
        }
    }
}

fun Context.make411CommonDimens(): List<ResourceData> {
    return arrayListOf<ResourceData>().apply {
        if (prefs.adjustQsGrid) {
            add(
                ResourceData(
                    "dimen",
                    "qs_tile_text_size",
                    prefs.qsRowCountPortrait.run {
                        when {
                            this > 4 -> "13sp"
                            else -> "14sp"
                        }
                    }
                )
            )
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if (prefs.adjustQsGrid) {
                add(
                    ResourceData(
                        "dimen",
                        "sec_qs_tile_width",
                        "${
                            calculateQsWidthForColumnCount(
                                prefs.qsColCountPortrait,
                                false
                            )
                        }dp"
                    )
                )
            }
            if (prefs.adjustQsHeader) {
                add(
                    ResourceData(
                        "dimen",
                        "qs_quick_tile_size",
                        "${
                            calculateQsWidthForHeaderCount(
                                prefs.headerCountPortrait,
                                false
                            )
                        }dp"
                    )
                )
            }
        }
    }
}

fun Context.makeLandscapeCommonDimens(): List<ResourceData> {
    return arrayListOf<ResourceData>().apply {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if (prefs.adjustQsGrid) {
                add(
                    ResourceData(
                        "dimen",
                        "sec_qs_tile_width",
                        "${
                            calculateQsWidthForColumnCount(
                                prefs.qsColCountLandscape,
                                true
                            )
                        }dp"
                    )
                )
            }
            if (prefs.adjustQsHeader) {
                add(
                    ResourceData(
                        "dimen",
                        "qs_quick_tile_size",
                        "${calculateQsWidthForHeaderCount(prefs.headerCountLandscape, true)}dp"
                    )
                )
            }
        }
    }
}

fun Context.makeLandscape411CommonDimens(): List<ResourceData> {
    return arrayListOf<ResourceData>().apply {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if (prefs.adjustQsGrid) {
                add(
                    ResourceData(
                        "dimen",
                        "sec_qs_tile_width",
                        "${
                            calculateQsWidthForColumnCount(
                                prefs.qsColCountLandscape,
                                true
                            )
                        }dp"
                    )
                )
            }
            if (prefs.adjustQsHeader) {
                add(
                    ResourceData(
                        "dimen",
                        "qs_quick_tile_size",
                        "${calculateQsWidthForHeaderCount(prefs.headerCountLandscape, true)}dp"
                    )
                )
            }
        }
    }
}