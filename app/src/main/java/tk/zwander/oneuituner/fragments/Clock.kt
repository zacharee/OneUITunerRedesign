package tk.zwander.oneuituner.fragments

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.PrefManager
import tk.zwander.oneuituner.util.prefs

class Clock : Base() {
    override val title = R.string.clock

    private val clockType by lazy { findPreference<ListPreference>(PrefManager.CLOCK_TYPE) as ListPreference }
    private val clockFormat: Preference by lazy { findPreference<Preference>(PrefManager.CLOCK_FORMAT)!! }

    init {
        keysToSync.add(PrefManager.CLOCK_FORMAT)
        keysToSync.add(PrefManager.QS_DATE_FORMAT)
        keysToSync.add(PrefManager.CLOCK_TYPE)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.clock, rootKey)

        updateFormatVisibility(clockType.value)

        clockType.setOnPreferenceChangeListener { _, newValue ->
            updateFormatVisibility(newValue.toString())
            true
        }
    }

    private fun updateFormatVisibility(newValue: String) {
        clockFormat.isVisible = newValue == context!!.prefs.clockTypeCustom
    }
}