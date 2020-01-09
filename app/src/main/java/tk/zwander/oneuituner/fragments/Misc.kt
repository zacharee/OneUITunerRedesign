package tk.zwander.oneuituner.fragments

import android.os.Bundle
import androidx.preference.Preference
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.PrefManager
import tk.zwander.oneuituner.util.navigationBarHeight
import tk.zwander.oneuituner.util.pxAsDp
import tk.zwander.oneuituner.util.statusBarHeight

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
    }
}