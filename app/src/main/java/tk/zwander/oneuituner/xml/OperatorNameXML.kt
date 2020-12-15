package tk.zwander.oneuituner.xml

import android.content.Context
import android.os.Build
import org.w3c.dom.Document
import org.w3c.dom.Element
import tk.zwander.oneuituner.util.importElement
import tk.zwander.oneuituner.util.prefs
import tk.zwander.oneuituner.xml.OperatorNameXML.createOperatorNameRoot
import tk.zwander.oneuituner.xml.OperatorNameXML.createOperatorNameView
import javax.xml.parsers.DocumentBuilderFactory

object OperatorNameXML {
    fun Document.createOperatorNameRoot(hide: Boolean = false): Element {
        return importElement(
            DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .newDocument()
                .createElement("com.android.systemui.statusbar.AlphaOptimizedFrameLayout")
                .apply {
                    setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android")
                    setAttribute("android:id", "@${if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) "+" else "*com.android.systemui:"}id/operator_name_frame")
                    setAttribute("android:layout_width", if (hide) "0dp" else "wrap_content")
                    setAttribute("android:layout_height", "match_parent")
                }
        )
    }

    fun Document.createOperatorNameView(): Element {
        return importElement(
            DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .newDocument()
                .createElement("com.android.systemui.statusbar.OperatorNameView")
                .apply {
                    setAttribute("android:id", "@${if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) "+" else "*com.android.systemui:"}id/operator_name")
                    setAttribute("android:layout_width", "wrap_content")
                    setAttribute("android:layout_height", "match_parent")
                    setAttribute("android:gravity", "left|center_vertical|center_horizontal|center|start")
                    setAttribute("android:maxLength", "20")
                    setAttribute("android:paddingEnd", "5dp")
                    setAttribute("android:singleLine", "true")
                    setAttribute("android:textAppearance", "?android:attr/textAppearanceSmall")
                }
        )
    }
}

fun Context.makeOperatorName(): Document {
    return DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .apply {
            appendChild(createOperatorNameRoot(prefs.hideStatusBarCarrier)).apply {
                appendChild(createOperatorNameView())
            }
        }
}