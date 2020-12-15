package tk.zwander.oneuituner.fragments

import android.os.Build
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.PrefManager
import tk.zwander.oneuituner.util.prefs
import tk.zwander.oneuituner.util.setPreferenceEnabled

class Clock : Base() {
    override val title = R.string.clock

    private val clockType by lazy { findPreference<ListPreference>(PrefManager.CLOCK_TYPE) as ListPreference }
    private val clockFormat: Preference by lazy { findPreference<Preference>(PrefManager.CLOCK_FORMAT)!! }

    init {
        keysToSync.add(PrefManager.CLOCK_FORMAT)
        keysToSync.add(PrefManager.QS_DATE_FORMAT)
        keysToSync.add(PrefManager.CLOCK_TYPE)
        keysToSync.add(PrefManager.CLOCK_POSITION)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.clock, rootKey)

        updateFormatVisibility(clockType.value)

        clockType.setOnPreferenceChangeListener { _, newValue ->
            updateFormatVisibility(newValue.toString())
            true
        }

        setPreferenceEnabled(PrefManager.CLOCK_POSITION, Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
    }

    private fun updateFormatVisibility(newValue: String) {
        clockFormat.isVisible = newValue == requireContext().prefs.clockTypeCustom
    }
}