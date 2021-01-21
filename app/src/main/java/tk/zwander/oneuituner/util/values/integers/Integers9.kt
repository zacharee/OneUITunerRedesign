package tk.zwander.oneuituner.util.values.integers

import android.content.Context
import tk.zwander.oneuituner.util.prefs
import tk.zwander.overlaylib.ResourceData
import tk.zwander.overlaylib.ResourceFileData
import tk.zwander.overlaylib.makeResourceXml

fun Context.makeDefault9Integers(): ResourceFileData {
    return ResourceFileData(
            "integers.xml",
            "values",
            makeResourceXml(
                    listOf(
                            ResourceData(
                                    "integer",
                                    "quick_qs_tile_num",
                                    prefs.headerCountLandscape.toString()
                            ),
                            ResourceData(
                                    "integer",
                                    "quick_qs_tile_min_num",
                                    "2"
                            ),
                            ResourceData(
                                    "integer",
                                    "qspanel_screen_grid_columns_5",
                                    prefs.qsColCountLandscape.toString()
                            ),
                            ResourceData(
                                    "integer",
                                    "qspanel_screen_grid_rows",
                                    prefs.qsColCountLandscape.toString()
                            )
                    )
            )
    )
}

fun Context.makeLandscape9Integers(): ResourceFileData {
    return ResourceFileData(
            "integers.xml",
            "values-land",
            makeResourceXml(
                    listOf(
                            ResourceData(
                                    "integer",
                                    "quick_qs_tile_num",
                                    prefs.headerCountLandscape.toString()
                            ),
                            ResourceData(
                                    "integer",
                                    "quick_qs_tile_min_num",
                                    "2"
                            ),
                            ResourceData(
                                    "integer",
                                    "qspanel_screen_grid_columns_5",
                                    prefs.qsColCountLandscape.toString()
                            ),
                            ResourceData(
                                    "integer",
                                    "qspanel_screen_grid_rows",
                                    prefs.qsColCountLandscape.toString()
                            )
                    )
            )
    )
}