package tk.zwander.oneuituner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.elevation.ElevationOverlayProvider
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.activity_main.*
import tk.zwander.oneuituner.util.doCompile
import tk.zwander.oneuituner.util.installNormally
import tk.zwander.oneuituner.util.navController
import tk.zwander.oneuituner.util.prefs
import tk.zwander.unblacklister.disableApiBlacklist
import java.io.File
import java.net.URLConnection

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    companion object {
        private const val ACTION_INSTALL_STATUS_UPDATE = "INSTALL_STATUS_UPDATE"
    }

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

    private val updateIntent by lazy {
        Intent(this, MainActivity::class.java).apply {
            action = ACTION_INSTALL_STATUS_UPDATE
        }
    }

    private val intentSender by lazy {
        PendingIntent.getActivity(
            this,
            100,
            updateIntent,
            0
        ).intentSender
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disableApiBlacklist()

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
            AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
                .apply { duration = animDuration }
        title_switcher.outAnimation =
            AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
                .apply { duration = animDuration }

        navController.addOnDestinationChangedListener(this)

        apply.setOnClickListener {
            doCompile {
                if (prefs.useSynergy) {
                    installForSynergy(it)
                } else {
                    installNormally(intentSender, *it.toTypedArray())
                }
            }
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        when (intent?.action) {
            ACTION_INSTALL_STATUS_UPDATE -> {
                when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -100)) {
                    PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                        startActivity(intent.getParcelableExtra(Intent.EXTRA_INTENT))
                    }
                    PackageInstaller.STATUS_SUCCESS -> {
                        Toast.makeText(this, R.string.succeeded, Toast.LENGTH_SHORT).show()
                    }
                    PackageInstaller.STATUS_FAILURE,
                    PackageInstaller.STATUS_FAILURE_ABORTED,
                    PackageInstaller.STATUS_FAILURE_BLOCKED,
                    PackageInstaller.STATUS_FAILURE_CONFLICT,
                    PackageInstaller.STATUS_FAILURE_INCOMPATIBLE,
                    PackageInstaller.STATUS_FAILURE_INVALID,
                    PackageInstaller.STATUS_FAILURE_STORAGE -> {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(R.string.installation_failed)
                            .setMessage(resources.getString(R.string.installation_failed_desc, intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)))
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                    }
                }
            }
        }
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

    private fun installForSynergy(files: List<File>) {
        val fileUris = ArrayList<Parcelable>(
            files.map {
                FileProvider.getUriForFile(
                    this,
                    "$packageName.apkprovider", it
                )
            }
        )
        Intent(Intent.ACTION_SEND_MULTIPLE).run {
            `package` = "projekt.samsung.theme.compiler"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris)
            type = URLConnection.guessContentTypeFromName(File(files[0].path).name)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(this)
        }
    }
}
