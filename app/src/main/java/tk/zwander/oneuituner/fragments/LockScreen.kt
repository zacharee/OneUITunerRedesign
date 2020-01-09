package tk.zwander.oneuituner.fragments

import android.os.Bundle
import tk.zwander.oneuituner.R

class LockScreen : Base() {
    override val title = R.string.lock_screen

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.lock_screen, rootKey)
    }
}