package tk.zwander.oneuituner.fragments

import android.os.Build
import android.os.Bundle
import androidx.preference.SwitchPreference
import tk.zwander.oneuituner.R

class StatusBar : Base() {
    override val title = R.string.status_bar

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.status_bar, rootKey)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            findPreference<SwitchPreference>("left_system_icons")?.isEnabled = false
            findPreference<SwitchPreference>("hide_status_bar_carrier")?.isEnabled = false

        }
    }
}