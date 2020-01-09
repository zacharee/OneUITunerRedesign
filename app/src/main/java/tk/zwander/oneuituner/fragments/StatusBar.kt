package tk.zwander.oneuituner.fragments

import android.os.Bundle
import tk.zwander.oneuituner.R

class StatusBar : Base() {
    override val title = R.string.status_bar

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.status_bar, rootKey)
    }
}