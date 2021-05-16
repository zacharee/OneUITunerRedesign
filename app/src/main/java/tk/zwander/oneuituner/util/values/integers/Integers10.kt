package tk.zwander.oneuituner.util.values.integers

import android.content.Context
import tk.zwander.oneuituner.util.prefs
import tk.zwander.overlaylib.ResourceData
import tk.zwander.overlaylib.ResourceFileData
import tk.zwander.overlaylib.makeResourceXml

fun Context.makeDefault10Integers(): ResourceFileData {
    return ResourceFileData(
        "integers",
        "values",
        makeResourceXml(
            arrayListOf<ResourceData>().apply {
                if (prefs.adjustQsHeader) {
                    add(
                        ResourceData(
                            "integer",
                            "quick_qs_panel_max_columns",
                            prefs.headerCountPortrait.toString()
                        )
                    )
                    add(
                        ResourceData(
                            "integer",
                            "quick_qs_tile_min_num",
                            "2"
                        )
                    )
                    add(
                        ResourceData(
                            "integer",
                            "sec_quick_settings_max_columns",
                            "20"
                        )
                    )
                    add(
                        ResourceData(
                            "integer",
                            "sec_quick_settings_num_columns",
                            "20"
                        )
                    )
                    add(
                        ResourceData(
                            "integer",
                            "sec_quick_settings_max_rows",
                            "20"
                        )
                    )
                }
            }
        )
    )
}

fun Context.makeLandscape10Integers(): ResourceFileData {
    return ResourceFileData(
        "integers",
        "values-land",
        makeResourceXml(
            arrayListOf<ResourceData>().apply {
                if (prefs.adjustQsHeader) {
                    add(
                        ResourceData(
                            "integer",
                            "quick_qs_panel_max_columns",
                            prefs.headerCountLandscape.toString()
                        )
                    )
                    add(
                        ResourceData(
                            "integer",
                            "quick_qs_tile_min_num",
                            "2"
                        )
                    )
                    add(
                        ResourceData(
                            "integer",
                            "sec_quick_settings_max_columns",
                            "20"
                        )
                    )
                    add(
                        ResourceData(
                            "integer",
                            "sec_quick_settings_num_columns",
                            "20"
                        )
                    )
                    add(
                        ResourceData(
                            "integer",
                            "sec_quick_settings_max_rows",
                            "20"
                        )
                    )
                }
            }
        )
    )
}