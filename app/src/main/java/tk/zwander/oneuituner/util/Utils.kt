@file:Suppress("DeferredResultUnused")

package tk.zwander.oneuituner.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Document
import org.w3c.dom.Element
import tk.zwander.oneuituner.BuildConfig
import tk.zwander.oneuituner.MainActivity
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.values.dimens.*
import tk.zwander.oneuituner.util.values.integers.*
import tk.zwander.oneuituner.util.values.strings.make372CommonStrings
import tk.zwander.oneuituner.util.values.strings.make600CommonStrings
import tk.zwander.oneuituner.util.values.strings.make900CommonStrings
import tk.zwander.oneuituner.util.values.strings.makeDefaultCommonStrings
import tk.zwander.oneuituner.xml.*
import tk.zwander.overlaylib.*
import java.io.*
import java.util.*
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.collections.ArrayList

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
        SimpleDateFormat(this, Locale.getDefault())
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
    get() = (!Build.MODEL.run { contains("960") } || Build.VERSION.SDK_INT > Build.VERSION_CODES.P)

fun Document.flattenToString(): String {
    val source = DOMSource(this)
    val writer = StringWriter()
    val result = StreamResult(writer)
    val tf = TransformerFactory.newInstance()
    val transformer = tf.newTransformer()

    transformer.transform(source, result)

    return writer.toString()
}

fun Context.doCompile(listener: (List<File>) -> Unit) = MainScope().launch {
    withContext(Dispatchers.IO) {
        var count = 0
        val files = ArrayList<File>()

        count++

        doCompileAlignAndSign(
            PACKAGE_SYSTEMUI,
            SUFFIX_SYSTEMUI,
            resFiles = mutableListOf<ResourceFileData>().apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    add(
                        ResourceFileData(
                            "status_bar.xml",
                            "layout",
                            contents = (when {
                                Build.VERSION.SDK_INT <= Build.VERSION_CODES.P -> makeAndroid9StatusBar()
                                Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q -> makeAndroid10StatusBar()
                                else -> makeAndroid11StatusBar()
                            }).flattenToString()
                        )
                    )

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                        add(
                            ResourceFileData(
                                "qs_carrier.xml",
                                "layout",
                                contents = makeQSCarrier().flattenToString()
                            )
                        )
                    } else {
                        add(
                            ResourceFileData(
                                "qs_status_bar_clock.xml",
                                "layout",
                                contents = maxQSStatusBarClock().flattenToString()
                            )
                        )
                    }

                    add(
                        ResourceFileData(
                            "keyguard_status_bar.xml",
                            "layout",
                            contents = (when {
                                Build.VERSION.SDK_INT <= Build.VERSION_CODES.P -> makeAndroid9KeyguardStatusBar()
                                Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q -> makeAndroid10KeyguardStatusBar()
                                else -> makeAndroid11KeyguardStatusBar()
                            }).flattenToString()
                        )
                    )
                }

                add(
                    ResourceFileData(
                        "operator_name.xml",
                        "layout",
                        contents = makeOperatorName().flattenToString()
                    )
                )

                when {
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.P -> {
                        add(makeDefault9Integers())
                        add(makeLandscape9Integers())
                        add(makeDefault9Dimens())
                        add(make4119Dimens())
                    }
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q -> {
                        add(makeDefault10Integers())
                        add(makeLandscape10Integers())
                        add(makeDefault10Dimens())
                        add(make41110Dimens())
                        add(makeLandscape10Dimens())
                        add(make411Landscape10Dimens())
                    }
                    Build.VERSION.SDK_INT <= 30 -> {
                        add(makeDefault11Integers())
                        add(makeLandscape11Integers())
                        add(makeDefault11Dimens())
                        add(make41111Dimens())
                        add(makeLandscape11Dimens())
                        add(make411Landscape11Dimens())
                    }
                }

                add(
                    ResourceFileData(
                        "strings.xml",
                        "values",
                        makeResourceXml(
                            makeDefaultCommonStrings()
                        )
                    )
                )

                add(
                    ResourceFileData(
                        "strings.xml",
                        "values-sw600dp",
                        makeResourceXml(
                            make600CommonStrings()
                        )
                    )
                )

                add(
                    ResourceFileData(
                        "strings.xml",
                        "values-sw900dp",
                        makeResourceXml(
                            make900CommonStrings()
                        )
                    )
                )

                add(
                    ResourceFileData(
                        "strings.xml",
                        "values-sw372dp",
                        makeResourceXml(
                            make372CommonStrings()
                        )
                    )
                )

                if (prefs.hideQsTileBackground) {
                    add(
                        ResourceFileData(
                            "colors.xml",
                            "values",
                            makeResourceXml(
                                listOf(
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_background_on",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_background_off",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_background_dim",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_bottom_background",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_background_image",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_icon_on_tint_color",
                                        "@android:color/white"
                                    )
                                )
                            )
                        )
                    )
                    add(
                        ResourceFileData(
                            "colors.xml",
                            "values-night",
                            makeResourceXml(
                                listOf(
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_background_on",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_background_off",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_background_dim",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_bottom_background",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_round_background_image",
                                        "@android:color/transparent"
                                    ),
                                    ResourceData(
                                        "color",
                                        "qs_tile_icon_on_tint_color",
                                        "@android:color/white"
                                    )
                                )
                            )
                        )
                    )
                }
                if (prefs.disable5ge) {
                    add(
                        ResourceFileData(
                            "stat_sys_data_connected_5ge_att",
                            "drawable",
                            contents = StringBuilder()
                                .append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
                                .append("<bitmap xmlns:android=\"https://schemas.android.com/apk/res/android\" android:src=\"@drawable/stat_sys_data_connected_lte_att\" />\n")
                                .toString()
                        )
                    )
                }

                if (prefs.disableFlashingWiFi) {
                    add(
                        ResourceFileData(
                            "stat_sys_wifi_signal_checking",
                            "drawable",
                            contents = StringBuilder()
                                .append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
                                .append("<bitmap xmlns:android=\"https://schemas.android.com/apk/res/android\" android:src=\"@drawable/stat_sys_wifi_signal_checking_dim\" />\n")
                                .toString()
                        )
                    )
                }

                add(
                    ResourceFileData(
                        "bools.xml",
                        "values",
                        makeResourceXml(
                            listOf(
                                ResourceData(
                                    "bool",
                                    "config_enableLockScreenRotation",
                                    "${prefs.lockScreenRotation}"
                                )
                            )
                        )
                    )
                )
            },
            listener = {
                files.add(it)
                if (files.size >= count) listener(files)
            }
        )

        count++
        doCompileAlignAndSign(
            PACKAGE_ANDROID,
            SUFFIX_ANDROID,
            resFiles = mutableListOf<ResourceFileData>().apply {
                add(
                    ResourceFileData(
                        "config.xml",
                        "values",
                        makeResourceXml(
                            mutableListOf(
                                ResourceData(
                                    "dimen",
                                    "navigation_bar_height",
                                    "${prefs.navHeight}dp"
                                ),
                            ).apply {
                                if (prefs.adjustStatusHeight) {
                                    add(
                                        ResourceData(
                                            "dimen",
                                            "status_bar_height_portrait",
                                            "${prefs.statusBarHeight}dp"
                                        )
                                    )
                                }

                                if (prefs.adjustNavHeight) {
                                    add(
                                        ResourceData(
                                            "dimen",
                                            "navigation_bar_width",
                                            "${prefs.navHeight}dp"
                                        )
                                    )
                                }

                                if (prefs.oldRecents && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                                    add(
                                        ResourceData(
                                            "string",
                                            "config_recentsComponentName",
                                            "com.android.systemui/.recents.RecentsActivity",
                                            "translatable=\"false\""
                                        )
                                    )
                                }
                            }
                        )
                    )
                )
                add(
                    ResourceFileData(
                        "integers.xml",
                        "values",
                        makeResourceXml(
                            mutableListOf(
                                ResourceData(
                                    "integer",
                                    "multiwindow_freeform_max_count",
                                    "${prefs.freeformMax}"
                                ),
                                ResourceData(
                                    "integer",
                                    "multiwindow_desktop_freeform_max_count",
                                    "${prefs.desktopFreeformMax}"
                                )
                            )
                        )
                    )
                )
            },
            listener = {
                files.add(it)
                if (files.size >= count) listener(files)
            }
        )
    }
}

private val installParams by lazy {
    PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL).apply {
        installFlags = installFlags or PackageManager.INSTALL_REPLACE_EXISTING
    }
}

val Context.statusBarHeight: Int
    get() = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))

val Context.navigationBarHeight: Int
    get() = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"))

fun Context.installNormally(source: File) {
    val installer = packageManager.packageInstaller

    val sessionId = installer.createSession(installParams)
    val session = installer.openSession(sessionId)

    val fileUri = FileProvider.getUriForFile(
        this,
        "$packageName.apkprovider", source
    )

    val output = session.openWrite("OneUITunerInstall:${source.nameWithoutExtension}", 0, -1)
    val input = contentResolver.openInputStream(fileUri)

    try {
        transfer(input, output)
    } catch (e: IOException) {
        Log.e("OneUITuner", "error", e)
    }

    session.fsync(output)
    input.close()
    output.close()
    session.commit(intentSender)
}

fun Context.uninstallNormally(`package`: String) {
    packageManager.packageInstaller.run {
        uninstall(`package`, intentSender)
    }
}

fun transfer(input: InputStream, output: OutputStream) {
    var read: Int
    val buffer = ByteArray(64 * 1024)

    while (true) {
        read = input.read(buffer)

        if (read <= 0) break

        output.write(buffer, 0, read)
    }

    output.flush()
}

fun PreferenceFragmentCompat.setPreferenceEnabled(key: CharSequence, enabled: Boolean) {
    findPreference<Preference>(key)?.isEnabled = enabled
}

val Context.updateIntent: Intent
    get() = Intent(this, MainActivity::class.java).apply {
        action = MainActivity.ACTION_INSTALL_STATUS_UPDATE
    }

val Context.intentSender: IntentSender
    get() = PendingIntent.getActivity(
        this,
        Random(System.currentTimeMillis()).nextInt(),
        updateIntent,
        0
    ).intentSender

fun Context.findInstalledOverlays(): Array<String> {
    return OVERLAYS.filter {
        try {
            packageManager.getApplicationInfo(it, 0)
            true
        } catch (e: Exception) {
            false
        }
    }.toTypedArray()
}

fun Context.findInstalledOverlayFiles(): Array<File> {
    return OVERLAYS.mapNotNull {
        try {
            File(packageManager.getApplicationInfo(it, 0).baseCodePath)
        } catch (e: Exception) {
            null
        }
    }.toTypedArray()
}

fun Context.findInstalledLegacyOverlays(): Array<String> {
    return LEGACY_OVERLAYS.filter {
        try {
            packageManager.getApplicationInfo(it, 0)
            true
        } catch (e: Exception) {
            false
        }
    }.toTypedArray()
}

fun Context.findInstalledLegacyOverlayFiles(): Array<File> {
    return LEGACY_OVERLAYS.mapNotNull {
        try {
            File(packageManager.getApplicationInfo(it, 0).baseCodePath)
        } catch (e: Exception) {
            null
        }
    }.toTypedArray()
}

val moduleExists: Boolean
    get() = SuFile(MAGISK_MODULE_PATH).exists()

val badModuleExists: Boolean
    get() = SuFile("${MAGISK_PATH}/modules/opfpcontrol").exists()

val moduleAppDir: File
    get() = SuFile(MAGISK_MODULE_PATH, "/system/app/").also {
        if (!it.exists()) {
            it.mkdirs()
        }
    }

fun createMagiskModule(result: ((needsToReboot: Boolean) -> Unit)? = null) = MainScope().launch {
    val needsToUpdate = withContext(Dispatchers.IO) {
        val doesExist = moduleExists
        val currentVersion = try {
            BufferedReader(SuFileInputStream(SuFile("$MAGISK_MODULE_PATH/module.prop")).reader())
                .lines()
                .filter { it.startsWith("versionCode") }
                .findFirst()
                .get()
                .split("=")[1]
                .toInt()
        } catch (e: Exception) {
            0
        }

        val badExists = badModuleExists

        val needsToUpdate = !doesExist || currentVersion < BuildConfig.MODULE_VERSION || badExists

        if (needsToUpdate) {
            val prop = java.lang.StringBuilder()
                .appendLine("id=tk.zwander.oneuituner.magisk")
                .appendLine("name=OneUI Tuner Module")
                .appendLine("version=${BuildConfig.MODULE_VERSION}")
                .appendLine("versionCode=${BuildConfig.MODULE_VERSION}")
                .appendLine("author=Zachary Wander")
                .appendLine("description=Systemlessly install OneUI Tuner overlays")

            Shell.su(
                "mkdir -p $MAGISK_MODULE_PATH",
                "chmod -R 0755 $MAGISK_MODULE_PATH",
                "touch $MAGISK_MODULE_PATH/auto_mount",
                "touch $MAGISK_MODULE_PATH/update",
                "echo \"$prop\" > $MAGISK_MODULE_PATH/module.prop"
            ).exec()
        }

        if (badExists) {
            SuFile("$MAGISK_PATH/modules/opfpcontrol").deleteRecursive()
        }

        needsToUpdate
    }

    result?.invoke(needsToUpdate)
}

fun reboot() {
    Shell.su("/system/bin/svc power reboot").submit {
        Log.e("OneUITuner", "Reboot failed?! ${it.code} ${it.isSuccess} \n${it.out.joinToString("\n")} \n${it.err.joinToString(",")}")
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
@SuppressLint("SetWorldReadable")
fun installToModule(vararg files: SuFile, listener: ((needsSecondReboot: Boolean) -> Unit)? = null) = MainScope().launch {
    var needsSecondReboot = false

    withContext(Dispatchers.IO) {
        files.forEach {
            val folder = SuFile(moduleAppDir, it.nameWithoutExtension)
            if (!folder.exists()) folder.mkdirs()

            val dst = SuFile(folder, "${it.nameWithoutExtension}.apk")
            if (!dst.exists()) {
                needsSecondReboot = true
                dst.createNewFile()
            }

            folder.setWritable(true, true)
            @Suppress("BlockingMethodInNonBlockingContext")
            folder.setReadable(true, false)
            folder.setExecutable(true, false)

            dst.setWritable(true, true)
            dst.setReadable(true, false)
            dst.setExecutable(false)

            it.copyTo(dst, true)
        }
    }

    listener?.invoke(needsSecondReboot)
}

fun removeFromModule(vararg files: File, listener: (() -> Unit)? = null) = MainScope().launch {
    withContext(Dispatchers.IO) {
        files.forEach {
            Shell.su("rm -rf $moduleAppDir/${it.nameWithoutExtension}").exec()
        }
    }

    listener?.invoke()
}

fun Document.importElement(element: Element): Element = importNode(element, true) as Element

fun Context.showSynergyInstallPrompt() {
    AlertDialog.Builder(this)
        .setTitle(R.string.synergy_not_installed)
        .setMessage(R.string.synergy_not_installed_desc)
        .setPositiveButton(R.string.yes) { _, _ ->
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=projekt.samsung.theme.compiler")
            val listingIntent = Intent(Intent.ACTION_VIEW)

            listingIntent.`package` = "com.android.vending"
            listingIntent.data = uri
            listingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(listingIntent)
        }
        .setNegativeButton(R.string.no, null)
        .show()
}

//Safely launch a URL.
//If no matching Activity is found, silently fail.
fun Context.launchUrl(url: String) {
    try {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    } catch (e: Exception) {}
}

//Safely start an email draft.
//If no matching email client is found, silently fail.
fun Context.launchEmail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/plain"
        intent.data = Uri.parse("mailto:${Uri.encode(to)}?subject=${Uri.encode(subject)}")

        startActivity(intent)
    } catch (e: Exception) {}
}