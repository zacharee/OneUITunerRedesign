package tk.zwander.oneuituner.util

import android.app.Activity
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceFragmentCompat
import org.w3c.dom.Document
import tk.zwander.oneuituner.R
import java.io.PrintWriter
import java.io.StringWriter
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

enum class ClockType {
    DEFAULT,
    AOSP,
    CUSTOM
}

val mainHandler = Handler(Looper.getMainLooper())

val Context.prefs: PrefManager
    get() = PrefManager.getInstance(this)

val PreferenceFragmentCompat.navController: NavController
    get() = NavHostFragment.findNavController(this)

val navOptions =
    NavOptions.Builder()
        .setEnterAnim(android.R.anim.fade_in)
        .setExitAnim(android.R.anim.fade_out)
        .setPopEnterAnim(android.R.anim.fade_in)
        .setPopExitAnim(android.R.anim.fade_out)
        .build()

val Activity.navController: NavController
    get() = findNavController(R.id.nav_host)

val String.isValidClockFormat: Boolean
    get() = try {
        SimpleDateFormat(this)
        true
    } catch (e: Exception) {
        false
    }

fun Context.pxToDp(px: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics)

val Context.isSynergyInstalled: Boolean
    get() = try {
        packageManager.getPackageInfo("projekt.samsung.theme.compiler", 0)
        true
    } catch (e: java.lang.Exception) {
        false
    }

val needsSynergy: Boolean
    get() = !Build.MODEL.run { contains("960") } || Build.VERSION.SDK_INT > Build.VERSION_CODES.P

fun Document.flattenToString(): String {
    val source = DOMSource(this)
    val writer = StringWriter()
    val result = StreamResult(writer)
    val tf = TransformerFactory.newInstance()
    val transformer = tf.newTransformer()

    transformer.transform(source, result)

    return writer.toString()
}
