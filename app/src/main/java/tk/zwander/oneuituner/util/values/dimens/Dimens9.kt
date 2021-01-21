package tk.zwander.oneuituner.util.values.dimens

import android.content.Context
import tk.zwander.oneuituner.util.needsSynergy
import tk.zwander.oneuituner.util.prefs
import tk.zwander.overlaylib.ResourceData
import tk.zwander.overlaylib.ResourceFileData
import tk.zwander.overlaylib.makeResourceXml

fun Context.makeDefault9Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens.xml",
            "values",
            makeResourceXml(
                    listOf(
                            ResourceData(
                                    "dimen",
                                    "qs_tile_height_5x3_ratio",
                                    prefs.qsRowCountPortrait.run {
                                        when {
                                            this > 4 -> "9.5"
                                            this > 3 -> "8.0"
                                            else -> "7.1"
                                        }
                                    }
                            ),
                            ResourceData(
                                    "dimen",
                                    "qs_tile_icon_size_5x3_ratio",
                                    prefs.qsRowCountPortrait.run {
                                        when {
                                            this > 4 -> if (!needsSynergy) "21.0" else "3.0"
                                            else -> if (!needsSynergy) "19.43" else "2.73"
                                        }
                                    }
                            ),
                            ResourceData(
                                    "dimen",
                                    "qs_tile_text_size",
                                    prefs.qsRowCountPortrait.run {
                                        when {
                                            this > 4 -> "12sp"
                                            else -> "13sp"
                                        }
                                    }
                            )
                    ) + makeDefaultCommonDimens()
            )
    )
}

fun Context.make4119Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens.xml",
            "values-sw411dp",
            makeResourceXml(
                    make411CommonDimens()
            )
    )
}