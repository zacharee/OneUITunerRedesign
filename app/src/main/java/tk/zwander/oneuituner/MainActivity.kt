package tk.zwander.oneuituner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.android.material.elevation.ElevationOverlayProvider
import com.google.android.material.shape.MaterialShapeDrawable
import com.topjohnwu.superuser.Shell
import kotlinx.android.synthetic.main.activity_main.*
import tk.zwander.oneuituner.util.*
import tk.zwander.overlaylib.ResourceFileData
import tk.zwander.overlaylib.compileOverlay
import tk.zwander.overlaylib.doCompileAlignAndSign
import java.io.File
import java.net.URLConnection

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val currentFrag: NavDestination?
        get() = navController.currentDestination

    private val navButton by lazy {
        run {
            Toolbar::class.java
                .getDeclaredField("mNavButtonView")
                .apply {
                    isAccessible = true
                }
                .get(bottom_bar) as View
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_bar.setNavigationOnClickListener {
            onBackPressed()
        }

        with(bottom_bar.background as MaterialShapeDrawable) {
            val color = ElevationOverlayProvider(this@MainActivity)
                .compositeOverlayWithThemeSurfaceColorIfNeeded(elevation)

            window.navigationBarColor = color
        }

        root.layoutTransition = LayoutTransition()
            .apply {
                enableTransitionType(LayoutTransition.CHANGING)
            }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        navButton.visibility = View.GONE

        val animDuration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

        title_switcher.inAnimation =
            AnimationUtils.loadAnimation(this, android.R.anim.fade_in).apply { duration = animDuration }
        title_switcher.outAnimation =
            AnimationUtils.loadAnimation(this, android.R.anim.fade_out).apply { duration = animDuration }

        navController.addOnDestinationChangedListener(this)

        apply.setOnClickListener {
            doCompileAlignAndSign(
                "com.android.systemui",
                "oneuituner",
                resFiles = arrayListOf(
                    ResourceFileData(
                        "status_bar.xml",
                        "layout",
                        makeAndroid10StatusBar(ClockType.AOSP).flattenToString()
                    )
                ),
                listener = {
                    installForSynergy(it)
                }
            )
        }
    }

    override fun setTitle(title: CharSequence?) {
        title_switcher.setText(title)
        super.setTitle(null)
    }

    override fun setTitle(titleId: Int) {
        title = getText(titleId)
    }

    override fun onDestroy() {
        super.onDestroy()

        navController.removeOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val id = currentFrag?.id
        val enabled = id != R.id.main

        navButton.animatedVisibility = if (enabled) View.VISIBLE else View.GONE
    }

    private var View.animatedVisibility: Int
        get() = visibility
        set(value) {
            val hide = value != View.VISIBLE

            if (!hide) visibility = value

            animate()
                .scaleX(if (hide) 0f else 1f)
                .scaleY(if (hide) 0f else 1f)
                .setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                .setInterpolator(if (hide) AnticipateInterpolator() else OvershootInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        if (hide) visibility = value
                    }
                })
        }

    private fun installForSynergy(file: File) {
        val fileUri = FileProvider.getUriForFile(this,
            "$packageName.apkprovider", file)
        Intent(Intent.ACTION_SEND).run {
            `package` = "projekt.samsung.theme.compiler"
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = URLConnection.guessContentTypeFromName(file.name)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(this)
        }

        Shell.sh("cp ${file.absolutePath} $externalCacheDir/").exec()
    }
}
