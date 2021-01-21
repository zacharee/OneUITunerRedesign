package tk.zwander.oneuituner.util.values.dimens

import android.content.Context
import tk.zwander.overlaylib.ResourceData
import tk.zwander.overlaylib.ResourceFileData
import tk.zwander.overlaylib.makeResourceXml

fun Context.makeDefault11Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens",
            "values",
            makeResourceXml(
                    listOf<ResourceData>() + makeDefaultCommonDimens()
            )
    )
}

fun Context.makeLandscape11Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens",
            "values-land",
            makeResourceXml(
                    listOf<ResourceData>() + makeLandscapeCommonDimens()
            )
    )
}

fun Context.make41111Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens",
            "values-sw411dp",
            makeResourceXml(
                    listOf<ResourceData>() + make411CommonDimens()
            )
    )
}

fun Context.make411Landscape11Dimens(): ResourceFileData {
    return ResourceFileData(
            "dimens",
            "values-sw411dp",
            makeResourceXml(
                    listOf<ResourceData>() + makeLandscape411CommonDimens()
            )
    )
}