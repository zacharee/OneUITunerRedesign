package tk.zwander.oneuituner.fragments

import android.os.Build
import android.os.Bundle
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.PrefManager
import tk.zwander.oneuituner.util.setPreferenceEnabled

class QS : Base() {
    override val title = R.string.qs

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.qs, rootKey)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            setPreferenceEnabled(PrefManager.QS_ROW_COUNT_LANDSCAPE, false)
            setPreferenceEnabled(PrefManager.QS_ROW_COUNT_PORTRAIT, false)
        }
    }
}