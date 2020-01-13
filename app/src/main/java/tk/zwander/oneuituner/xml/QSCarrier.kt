package tk.zwander.oneuituner.xml

import android.content.Context
import org.w3c.dom.Document
import org.w3c.dom.Element
import tk.zwander.oneuituner.util.importElement
import tk.zwander.oneuituner.util.prefs
import tk.zwander.oneuituner.xml.QSCarrier.createMobileSignalGroupInclude
import tk.zwander.oneuituner.xml.QSCarrier.createQSCarrier
import tk.zwander.oneuituner.xml.QSCarrier.createQsCarrierText
import javax.xml.parsers.DocumentBuilderFactory

object QSCarrier {
    fun Document.createQSCarrier(): Element {
        return importElement(
            DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .newDocument()
                .createElement("com.android.systemui.qs.QSCarrier")
                .apply {
                    setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android")
                    setAttribute("android:id", "@*com.android.systemui:id/linear_carrier")
                    setAttribute("android:layout_width", "wrap_content")
                    setAttribute("android:layout_height", "match_parent")
                    setAttribute("android:background", "@android:color/transparent")
                    setAttribute("android:clickable", "false")
                    setAttribute("android:clipChildren", "false")
                    setAttribute("android:clipToPadding", "false")
                    setAttribute("android:focusable", "true")
                    setAttribute("android:gravity", "left|center_vertical|center_horizontal|center|start")
                    setAttribute("android:minWidth", "48dp")
                    setAttribute("android:orientation", "horizontal")
                }
        )
    }

    fun Document.createMobileSignalGroupInclude(): Element {
        return importElement(
            DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .newDocument()
                .createElement("include")
                .apply {
                    setAttribute("layout", "@*com.android.systemui:layout/mobile_signal_group")
                    setAttribute("android:layout_width", "wrap_content")
                    setAttribute("android:layout_height", "wrap_content")
                    setAttribute("android:layout_marginEnd", "@*com.android.systemui:dimen/qs_carrier_margin_width")
                    setAttribute("android:visibility", "gone")
                }
        )
    }

    fun Document.createQsCarrierText(isHidden: Boolean = false): Element {
        return importElement(
            DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .newDocument()
                .createElement("com.android.systemui.util.AutoMarqueeTextView")
                .apply {
                    setAttribute("android:id", "@*com.android.systemui:id/qs_carrier_text")
                    setAttribute("android:layout_width", if (isHidden) "0dp" else "wrap_content")
                    setAttribute("android:layout_height", "wrap_content")
                    if (!isHidden) setAttribute("android:layout_weight", "1")
                    setAttribute("android:marqueeRepeatLimit", "marquee_forever")
                    setAttribute("android:maxEms", "7")
                    setAttribute("android:singleLine", "true")
                    setAttribute("android:textAppearance", "@*com.android.systemui:style/TextAppearance.QS.Status")
                    setAttribute("android:textDirection", "locale")
                }
        )
    }
}

fun Context.makeQSCarrier(): Document {
    return DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .newDocument()
        .apply {
            appendChild(createQSCarrier()).apply {
                appendChild(createMobileSignalGroupInclude())
                appendChild(createQsCarrierText(prefs.hideStatusBarCarrier))
            }
        }
}