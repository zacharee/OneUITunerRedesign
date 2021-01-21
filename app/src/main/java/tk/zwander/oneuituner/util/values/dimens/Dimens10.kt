package tk.zwander.oneuituner.util.values.dimens

import android.content.Context
import tk.zwander.overlaylib.ResourceData
import tk.zwander.overlaylib.ResourceFileData
import tk.zwander.overlaylib.makeResourceXml

fun Context.makeDefault10Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens",
            "values",
            makeResourceXml(
                    listOf<ResourceData>() + makeDefaultCommonDimens()
            )
    )
}

fun Context.makeLandscape10Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens",
            "values-land",
            makeResourceXml(
                    listOf<ResourceData>() + makeLandscapeCommonDimens()
            )
    )
}

fun Context.make41110Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens",
            "values-sw411dp",
            makeResourceXml(
                    listOf<ResourceData>() + make411CommonDimens()
            )
    )
}

fun Context.make411Landscape10Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens",
            "values-sw411dp",
            makeResourceXml(
                    listOf<ResourceData>() + makeLandscape411CommonDimens()
            )
    )
}