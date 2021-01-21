package tk.zwander.oneuituner.util.values.strings

import android.content.Context
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.isValidClockFormat
import tk.zwander.oneuituner.util.prefs
import tk.zwander.overlaylib.ResourceData

fun Context.makeDefaultCommonStrings(): List<ResourceData> {
    return arrayListOf<ResourceData>().apply {
        if (prefs.customQsDateFormat && prefs.qsDateFormat.isValidClockFormat) {
            add(
                    ResourceData(
                            "string",
                            "system_ui_quick_panel_date_pattern",
                            prefs.qsDateFormat
                    )
            )
        }

        if (prefs.navLayout.isNotEmpty()) {
            val keys = resources.getStringArray(R.array.nav_bar_layout_resource_keys)

            keys.forEach {
                add(
                        ResourceData(
                                "string",
                                it,
                                prefs.navLayout
                        )
                )
            }
        }
    }
}

fun Context.make600CommonStrings(): List<ResourceData> {
    return arrayListOf<ResourceData>().apply {
        if (prefs.navLayout.isNotEmpty()) {
            val keys = resources.getStringArray(R.array.nav_bar_layout_resource_keys)

            keys.forEach {
                add(
                        ResourceData(
                                "string",
                                it,
                                prefs.navLayout
                        )
                )
            }
        }
    }
}

fun Context.make900CommonStrings(): List<ResourceData> {
    return arrayListOf<ResourceData>().apply {
        if (prefs.navLayout.isNotEmpty()) {
            val keys = resources.getStringArray(R.array.nav_bar_layout_resource_keys)

            keys.forEach {
                add(
                        ResourceData(
                                "string",
                                it,
                                prefs.navLayout
                        )
                )
            }
        }
    }
}

fun Context.make372CommonStrings(): List<ResourceData> {
    return arrayListOf<ResourceData>().apply {
        if (prefs.navLayout.isNotEmpty()) {
            val keys = resources.getStringArray(R.array.nav_bar_layout_resource_keys)

            keys.forEach {
                add(
                        ResourceData(
                                "string",
                                it,
                                prefs.navLayout
                        )
                )
            }
        }
    }
}