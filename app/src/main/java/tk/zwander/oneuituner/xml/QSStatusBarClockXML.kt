package tk.zwander.oneuituner.xml

import android.content.Context
import org.w3c.dom.Document
import tk.zwander.oneuituner.util.prefs
import tk.zwander.oneuituner.xml.StatusBarXML.createAOSPClock
import tk.zwander.oneuituner.xml.StatusBarXML.createCustomClock
import tk.zwander.oneuituner.xml.StatusBarXML.createDefaultClock
import javax.xml.parsers.DocumentBuilderFactory


fun Context.maxQSStatusBarClock(): Document {
    val clockType = prefs.clockType

    return DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .apply {
            appendChild(
                when (clockType) {
                    prefs.clockTypeAosp -> createAOSPClock(defaultID = true)
                    prefs.clockTypeCustom -> createCustomClock(prefs.clockFormat, defaultID = true)
                    else -> createDefaultClock()
                }
            )

            documentElement.setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android")
        }
}