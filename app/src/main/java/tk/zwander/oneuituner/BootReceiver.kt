package tk.zwander.oneuituner

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.topjohnwu.superuser.Shell
import tk.zwander.oneuituner.util.findInstalledOverlays
import tk.zwander.oneuituner.util.prefs

class BootReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_DO_REBOOT = "${BuildConfig.APPLICATION_ID}.intent.action.DO_REBOOT"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_LOCKED_BOOT_COMPLETED,
            Intent.ACTION_BOOT_COMPLETED -> {
                val installed = context.findInstalledOverlays()
                if (installed.isNotEmpty()) {
                    installed.forEach {
                        Shell.su("cmd overlay enable $it").exec()
                    }

                    notifyForSecondReboot(context)
                }
            }
        }
    }

    private fun notifyForSecondReboot(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (context.prefs.needsAdditionalReboot) {
            val notification = Notification.Builder(context, "opfp_main")
                .setContentTitle(context.resources.getText(R.string.reboot))
                .setContentText(context.resources.getText(R.string.reboot_first_again_desc))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(Notification.BigTextStyle())
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        100,
                        Intent(ACTION_DO_REBOOT).apply {
                            `package` = context.packageName
                            component = ComponentName(context, BCRRebootActivity::class.java)
                        },
                        0
                    )
                )

            nm.notify(1000, notification.build())
        }
    }
}
