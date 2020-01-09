package tk.zwander.oneuituner.fragments

import android.os.Bundle
import tk.zwander.oneuituner.R

class Misc : Base() {
    override val title = R.string.misc

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.misc, rootKey)
    }
}