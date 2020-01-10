package tk.zwander.oneuituner.fragments

import android.os.Build
import android.os.Bundle
import androidx.preference.Preference
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.*

class Misc : Base() {
    override val title = R.string.misc

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.misc, rootKey)

        findPreference<Preference>(PrefManager.STATUS_BAR_HEIGHT)?.apply {
            setDefaultValue(requireContext().run { pxAsDp(statusBarHeight) } * 10)
        }

        findPreference<Preference>(PrefManager.NAV_HEIGHT)?.apply {
            setDefaultValue(requireContext().run { pxAsDp(navigationBarHeight) } * 10)
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            setPreferenceEnabled(PrefManager.OLD_RECENTS, false)
        }
    }
}