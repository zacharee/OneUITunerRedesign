package tk.zwander.oneuituner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.elevation.ElevationOverlayProvider
import com.google.android.material.shape.MaterialShapeDrawable
import com.topjohnwu.superuser.Shell
import kotlinx.android.synthetic.main.activity_main.*
import tk.zwander.oneuituner.util.*
import tk.zwander.unblacklister.disableApiBlacklist
import java.io.File
import java.net.URLConnection

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    companion object {
        const val ACTION_INSTALL_STATUS_UPDATE = "INSTALL_STATUS_UPDATE"
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

    private val filesToInstall = ArrayList<File>()
    private val packagesToUninstall = ArrayList<String>()
    private val nm by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private var progressShown: Boolean
        get() = progress_wrapper.isVisible
        set(value) {
            progress_wrapper.fadedVisibility = if (value) View.VISIBLE else View.GONE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disableApiBlacklist()

        val channel = NotificationChannel(
            "oneuituner_reboot",
            resources.getText(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        nm.createNotificationChannel(channel)

        if (Shell.rootAccess()) {
            createMagiskModule {
                if (it) {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setCancelable(false)
                        .setTitle(R.string.magisk_module_installed)
                        .setMessage(R.string.magisk_module_installed_desc)
                        .setPositiveButton(R.string.reboot) { _, _ ->
                            reboot()
                        }
                        .setNegativeButton(R.string.later, null)
                        .show()
                }
            }
        }

        cancel_button.setOnClickListener {
            filesToInstall.clear()
            packagesToUninstall.clear()

            progressShown = false
        }

        bottom_bar.setNavigationOnClickListener {
            onBackPressed()
        }

        with(bottom_bar.background as MaterialShapeDrawable) {
            val color = ElevationOverlayProvider(this@MainActivity)
                .compositeOverlayWithThemeSurfaceColorIfNeeded(elevation)

            window.navigationBarColor = color
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
            progressShown = true
            doCompile {
                if (prefs.useSynergy) {
                    progressShown = false
                    installForSynergy(it)
                } else if (!moduleExists) {
                    filesToInstall.clear()
                    filesToInstall.addAll(it)

                    installNormally(filesToInstall.removeAt(0))
                } else {
                    progressShown = true
                    installToModule(*it.toTypedArray()) { needsExtraReboot ->
                        progressShown = false

                        prefs.needsAdditionalReboot = needsExtraReboot

                        MaterialAlertDialogBuilder(this@MainActivity)
                            .setTitle(R.string.reboot)
                            .setMessage(if (!needsExtraReboot) R.string.reboot_others_desc else R.string.reboot_first_desc)
                            .setPositiveButton(R.string.reboot) { _, _ -> reboot() }
                            .setNegativeButton(R.string.later, null)
                            .setCancelable(false)
                            .show()
                    }
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
                        handlePackageSuccessOrFailure()
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
                            .setOnDismissListener {
                                handlePackageSuccessOrFailure()
                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun handlePackageSuccessOrFailure() {
        if (filesToInstall.isEmpty() && packagesToUninstall.isEmpty()) {
            progressShown = false
        } else {
            if (filesToInstall.isNotEmpty()) installNormally(filesToInstall.removeAt(0))
            if (packagesToUninstall.isNotEmpty()) uninstallNormally(packagesToUninstall.removeAt(0))
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

    fun performUninstall() {
        if (!Shell.rootAccess()) {
            packagesToUninstall.clear()
            packagesToUninstall.addAll(findInstalledOverlays())

            if (packagesToUninstall.isNotEmpty()) {
                progressShown = true
                uninstallNormally(packagesToUninstall.removeAt(0))
            }
        } else {
            progressShown = true
            removeFromModule(*findInstalledOverlayFiles()) {
                progressShown = false
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(R.string.reboot)
                    .setMessage(R.string.reboot_uninstall_desc)
                    .setPositiveButton(R.string.reboot) { _, _ -> reboot() }
                    .setNegativeButton(R.string.later, null)
                    .setCancelable(false)
                    .show()
            }
        }
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

    private var View.fadedVisibility: Int
        get() = visibility
        set(value) {
            val hide = value != View.VISIBLE

            if (!hide) {
                visibility = value
            }

            clearAnimation()
            animate()
                .alpha(if (hide) 0f else 1f)
                .setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        //Dummy animation to prevent flicker
                        val dummy = AlphaAnimation(alpha, alpha)
                        dummy.duration = 1
                        startAnimation(dummy)
                        if (hide) visibility = value
                    }
                })
                .start()
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
