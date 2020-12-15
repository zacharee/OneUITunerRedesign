package tk.zwander.oneuituner.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.topjohnwu.superuser.Shell
import tk.zwander.oneuituner.MainActivity
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.*

class Main : Base() {
    override val title = R.string.app_name

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main, rootKey)

        with(findPreference<SwitchPreference>(PrefManager.USE_SYNERGY)!!) {
            if (!Shell.rootAccess() && needsSynergy && !requireContext().prefs.hasKey(PrefManager.USE_SYNERGY)) isChecked = true
            onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { pref, newValue ->
                    val enabled = newValue.toString().toBoolean()

                    if (enabled && !requireContext().isSynergyInstalled) {
                        mainHandler.post {
                            (pref as SwitchPreference).isChecked = false
                        }

                        requireContext().showSynergyInstallPrompt()
                    }
                    true
                }
        }

        with(findPreference<SwitchPreference>(Keys.clock)!!) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                isEnabled = false
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return run {
            val (ret, action) = when(preference?.key) {
                Keys.clock -> true to R.id.action_main_to_clock
                Keys.qs -> true to R.id.action_main_to_qs
                Keys.misc -> true to R.id.action_main_to_misc
                Keys.statusBar -> true to R.id.action_main_to_statusBar
                Keys.lockScreen -> true to R.id.action_main_to_lockScreen
                "uninstall_all" -> run {
                    (requireActivity() as MainActivity).performUninstall()
                    false to 0
                }
                "uninstall_legacy" -> run {
                    (requireActivity() as MainActivity).performLegacyUninstall()
                    false to 0
                }
                else -> super.onPreferenceTreeClick(preference) to 0
            }

            if (action != 0) {
                navController.navigate(
                    action,
                    null,
                    navOptions
                )
            }

            ret
        }
    }
}